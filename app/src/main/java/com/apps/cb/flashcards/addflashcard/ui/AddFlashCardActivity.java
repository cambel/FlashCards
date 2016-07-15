package com.apps.cb.flashcards.addflashcard.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.apps.cb.flashcards.FlashCardsApp;
import com.apps.cb.flashcards.R;
import com.apps.cb.flashcards.addflashcard.AddFlashCardPresenter;
import com.apps.cb.flashcards.entities.FlashCard;
import com.apps.cb.flashcards.lib.base.ImageLoader;
import com.apps.cb.flashcards.login.ui.LoginActivity;
import com.apps.cb.flashcards.utils.Utils;
import com.facebook.AccessToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFlashCardActivity extends AppCompatActivity implements AddFlashCardView {

    private static final String TAG = "AddFlashCardActivity";

    public static final String FLASH_CARD_KEY = "flash_card";

    private static final int REQUEST_PICTURE = 0;

    @Bind(R.id.imgFlashCard)
    ImageView imgFlashCard;
    @Bind(R.id.txtTitle)
    EditText txtTitle;
    @Bind(R.id.txtNotes)
    EditText txtNotes;
    @Bind(R.id.container)
    RelativeLayout container;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private String photoPath;
    private FlashCard currentCard;

    @Inject
    ImageLoader imageLoader;
    @Inject
    AddFlashCardPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flash_card);
        ButterKnife.bind(this);

        setupInjection();
        setupNavigation();

        presenter.onCreate();

        setupCurrentCard();
    }

    private void setupCurrentCard() {
        currentCard = (FlashCard) getIntent().getSerializableExtra(FLASH_CARD_KEY);

        if (currentCard != null) {
            photoPath = currentCard.getResourceUrl();
            imageLoader.load(imgFlashCard, photoPath);
            txtTitle.setText(currentCard.getTitle());
            txtNotes.setText(currentCard.getNotes());
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupInjection() {
        FlashCardsApp app = (FlashCardsApp) getApplication();
        app.getAddFlashCardComponent(this, this).inject(this);
    }


    private void setupNavigation() {
        String email = sharedPreferences.getString(FlashCardsApp.EMAIL_KEY, "");
        toolbar.setTitle(email);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_flashcard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
        } else if (id == R.id.action_save) {
            saveCard();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveCard() {
        if (txtTitle.getText().toString().isEmpty() || photoPath == null) {
            displayMessage("Required Photo and Title");
            return;
        }

        FlashCard card = currentCard != null ? currentCard : new FlashCard();
        card.setNotes(txtNotes.getText().toString());
        card.setTitle(txtTitle.getText().toString());
        card.setResourceUrl(photoPath);
        card.setUserId(AccessToken.getCurrentAccessToken().getUserId());
        presenter.saveCard(card);

    }

    private void logout() {
        presenter.logout();
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @OnClick(R.id.cardPicture)
    public void takePicture() {

        if (photoPath != null) {
            displayAlert();
            return;
        }
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        File photoFile = getFile();

        if (photoFile != null) {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                intentList = addIntentsToList(intentList, takePhotoIntent);
            }
        }

        if (pickIntent.resolveActivity(getPackageManager()) != null) {
            intentList = addIntentsToList(intentList, pickIntent);
        }

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    getString(R.string.main_message_picture_source));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        startActivityForResult(chooserIntent, REQUEST_PICTURE);
    }

    private void displayAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.add_flashcard_alert_title));
        alertDialog.setMessage(getString(R.string.add_flashcard_alert_message));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.add_flashcard_alert_button_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        photoPath = null;
                        takePicture();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.add_flashcard_alert_button_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private List<Intent> addIntentsToList(List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    private File getFile() {
        File photoFile = Utils.getOutputMediaFile(this);
        if (photoFile != null) {
            try {
                photoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            photoPath = photoFile.getAbsolutePath();
            Log.d(TAG, "Filename: " + photoFile.getName());
        } else {
            Snackbar.make(container, R.string.main_error_dispatch_camera, Snackbar.LENGTH_SHORT).show();
        }
        return photoFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_PICTURE) {
            boolean isCamera = (data == null ||
                    data.getData() == null);

            if (isCamera) {
                addPicToGallery();
            } else {
                photoPath = Utils.getRealPathFromURI(this, data.getData());
            }

            imageLoader.load(imgFlashCard, photoPath);
        }
    }

    private void addPicToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void displayMessage(String message) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void goToMainScreen() {
        finish();
    }

    @Override
    public void onUploadInit() {
        displayMessage(getString(R.string.add_flashcard_upload_init));
    }

    @Override
    public void onUploadComplete() {
        displayMessage(getString(R.string.add_flashcard_upload_completed));
    }

    @Override
    public void onUploadError(String error) {
        displayMessage(error);
    }

}
