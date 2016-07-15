package com.apps.cb.flashcards.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.apps.cb.flashcards.FlashCardsApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Fashr
 * Created by cristianbehe on 7/14/2016.
 */
public class Utils {

    public static final String FOLDER_DIR = "/" + FlashCardsApp.APPLICATION_NAME;

    public static String getExternalFolderDir(Context ctx) {
        return ctx.getExternalFilesDir(null) + FOLDER_DIR;
    }

    public static String getInternalFolderDir(Context ctx) {
        return ctx.getFilesDir() + FOLDER_DIR;
    }

    public static String getApplicationFolder(Context ctx) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && checkFolder(getExternalFolderDir(ctx))) {
            return (getExternalFolderDir(ctx));
        } else {
            return (getInternalFolderDir(ctx));
        }
    }

    private static boolean checkFolder(String folderDir) {
        File folder = new File(folderDir);
        return folder.exists() || folder.mkdir();
    }

    /**
     * Used to return the camera File output.
     *
     * @return Returns a File where the photo will be stored
     */
    public static File getOutputMediaFile(Context ctx) {

        File mediaStorageDir;

        mediaStorageDir = new File(getApplicationFolder(ctx));

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Camera Guide", "Required media storage does not exist");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = String.valueOf(new Date().getTime());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");


        return mediaFile;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String result = null;
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            if (contentUri.toString().contains("mediaKey")) {
                cursor.close();

                try {
                    File file = File.createTempFile("tempImg", ".jpg", context.getCacheDir());
                    InputStream input = context.getContentResolver().openInputStream(contentUri);
                    result = saveImage(input, file);

                } catch (Exception e) {
                }
            } else if (contentUri.toString().startsWith("content://")) {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            } else {
                cursor.close();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    result = getRealPathFromURI_API19(context, contentUri);

                if (result == null)
                    result = getRealPathFromURI_API11to18(context, contentUri);
            }

        }
        return result;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public static String saveImage(InputStream inputStream, File file) throws IOException {
        OutputStream output = new FileOutputStream(file);

        try {
            byte[] buffer = new byte[4 * 1024];
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();

        } finally {
            output.close();
            inputStream.close();
        }

        return file.getAbsolutePath();
    }
}
