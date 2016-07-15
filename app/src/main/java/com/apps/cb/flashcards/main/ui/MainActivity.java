package com.apps.cb.flashcards.main.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.apps.cb.flashcards.FlashCardsApp;
import com.apps.cb.flashcards.R;
import com.apps.cb.flashcards.addflashcard.ui.AddFlashCardActivity;
import com.apps.cb.flashcards.entities.FlashCard;
import com.apps.cb.flashcards.listflashcard.OnClickListener;
import com.apps.cb.flashcards.login.ui.LoginActivity;
import com.apps.cb.flashcards.main.MainPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
public class MainActivity extends AppCompatActivity implements MainView, OnClickListener {


    private static final int REQUEST_PERMISSION = 1;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.mainContainer)
    RelativeLayout mainContainer;
    @Bind(R.id.btnShare)
    ImageButton btnShare;


    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    MainPresenter presenter;
    @Inject
    MainPagerAdapter pagerAdapter;

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupInjection();
        setupNavigation();
        setupAdapter();
        setupFacebookShare();

        presenter.onCreate();
        presenter.subscribe();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setupFacebookShare() {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Snackbar.make(mainContainer,"Publicado exitosamente", Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                Snackbar.make(mainContainer,"Cancelado", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Snackbar.make(mainContainer,"Error Publicando: " + error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @OnClick(R.id.btnShare)
    public void onShare() {
        FlashCard card = pagerAdapter.getFlashCard(viewPager.getCurrentItem());

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(card.getTitle())
                    .setContentDescription(card.getNotes())
                    .setContentUrl(Uri.parse(card.getResourceUrl()))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    private void setupAdapter() {
        viewPager.setAdapter(pagerAdapter);
        btnShare.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        presenter.unsubscribe();
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupInjection() {
        FlashCardsApp app = (FlashCardsApp) getApplication();
        app.getMainComponent(this, this).inject(this);
    }

    private void setupNavigation() {
        String email = sharedPreferences.getString(FlashCardsApp.EMAIL_KEY, "");
        toolbar.setTitle(email);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
        } else if (id == R.id.action_add) {
            addCard();
        }

        return super.onOptionsItemSelected(item);
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


    public void addCard() {
        if (validatePermissions()) {
            goToAddFlashCardScreen();
        }
    }

    private void goToAddFlashCardScreen() {
        goToAddFlashCardScreen(null);
    }

    private void goToAddFlashCardScreen(FlashCard card) {
        Intent intent = new Intent(this, AddFlashCardActivity.class);
        intent.putExtra(AddFlashCardActivity.FLASH_CARD_KEY, card);
        startActivity(intent);
    }

    private boolean validatePermissions() {
        boolean allPermissionGranted = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            String permission[] = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET
            };

            for (String perm : permission) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(this,
                        perm) != PackageManager.PERMISSION_GRANTED) {
                    allPermissionGranted = false;
                    ActivityCompat.requestPermissions(this,
                            new String[]{perm}, REQUEST_PERMISSION);
                }
            }
        }
        return allPermissionGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToAddFlashCardScreen();
                } else {
                    Snackbar.make(mainContainer, R.string.main_error_permission_denied, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onFlashCardAdded(FlashCard card) {
        pagerAdapter.addItem(card);
        btnShare.setEnabled(true);
    }

    @Override
    public void onFlashCardRemoved(FlashCard card) {
        pagerAdapter.removeItem(card);
        if(pagerAdapter.getCount() == 0)
            btnShare.setEnabled(false);
    }

    @Override
    public void onFlashCardUpdated(FlashCard card) {
        pagerAdapter.updateItem(card);
    }

    @Override
    public void onRemoveClick(FlashCard card) {
        presenter.removeCard(card);
    }

    @Override
    public void onFavoriteClick(FlashCard card) {
        presenter.toggleFavorite(card);
    }

    @OnClick({R.id.imgRemove, R.id.imgFavorite})
    public void onClick(View view) {
        FlashCard card = pagerAdapter.getFlashCard(viewPager.getCurrentItem());

        if (card != null)
            switch (view.getId()) {
                case R.id.imgRemove:
                    presenter.removeCard(card);
                    break;
                case R.id.imgFavorite:
                    goToAddFlashCardScreen(card);
                    break;
            }
    }
}
