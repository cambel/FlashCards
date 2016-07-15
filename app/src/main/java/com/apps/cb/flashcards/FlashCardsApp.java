package com.apps.cb.flashcards;

import android.app.Activity;
import android.app.Application;

import com.apps.cb.flashcards.addflashcard.di.AddFlashCardComponent;
import com.apps.cb.flashcards.addflashcard.di.AddFlashCardModule;
import com.apps.cb.flashcards.addflashcard.di.DaggerAddFlashCardComponent;
import com.apps.cb.flashcards.addflashcard.ui.AddFlashCardView;
import com.apps.cb.flashcards.domain.di.DomainModule;
import com.apps.cb.flashcards.lib.di.LibsModule;
import com.apps.cb.flashcards.login.di.DaggerLoginComponent;
import com.apps.cb.flashcards.login.di.LoginComponent;
import com.apps.cb.flashcards.login.di.LoginModule;
import com.apps.cb.flashcards.login.ui.LoginView;
import com.apps.cb.flashcards.main.di.DaggerMainComponent;
import com.apps.cb.flashcards.main.di.MainComponent;
import com.apps.cb.flashcards.main.di.MainModule;
import com.apps.cb.flashcards.main.ui.MainActivity;
import com.apps.cb.flashcards.main.ui.MainView;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
public class FlashCardsApp extends Application {

    public final static String EMAIL_KEY = "email";
    public static final String APPLICATION_NAME = "FlashCards";
    private DomainModule domainModule;
    private FlashCardsAppModule flashCardsAppModule;
    private LibsModule libsModule;

    @Override
    public void onCreate() {
        super.onCreate();
        initFireBase();
        initFacebook();
        initModules();
        AppEventsLogger.activateApp(this);
    }


    private void initModules() {
        this.domainModule = new DomainModule();
        this.flashCardsAppModule = new FlashCardsAppModule(this);
    }

    private void initFireBase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private void initFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public LoginComponent getLoginComponent(LoginView loginView, Activity activity) {
        return DaggerLoginComponent
                .builder()
                .loginModule(new LoginModule(loginView))
                .domainModule(domainModule)
                .flashCardsAppModule(flashCardsAppModule)
                .libsModule(new LibsModule(activity))
                .build();

    }

    public MainComponent getMainComponent(MainView mainView, MainActivity activity) {
        return DaggerMainComponent
                .builder()
                .mainModule(new MainModule(mainView, activity))
                .domainModule(domainModule)
                .flashCardsAppModule(flashCardsAppModule)
                .libsModule(new LibsModule(activity))
                .build();
    }

    public AddFlashCardComponent getAddFlashCardComponent(AddFlashCardView addFlashCardView, Activity activity) {
        return DaggerAddFlashCardComponent
                .builder()
                .addFlashCardModule(new AddFlashCardModule(addFlashCardView))
                .domainModule(domainModule)
                .libsModule(new LibsModule(activity))
                .flashCardsAppModule(flashCardsAppModule)
                .build();
    }

}
