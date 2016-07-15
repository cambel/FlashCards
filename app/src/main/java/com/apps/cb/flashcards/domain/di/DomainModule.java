package com.apps.cb.flashcards.domain.di;


import android.app.Activity;
import android.support.v4.app.Fragment;

import com.apps.cb.flashcards.domain.FirebaseAPI;
import com.apps.cb.flashcards.lib.GlideImageLoader;
import com.apps.cb.flashcards.lib.GreenRobotEventBus;
import com.apps.cb.flashcards.lib.base.EventBus;
import com.apps.cb.flashcards.lib.base.ImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
@Module
public class DomainModule {

    @Provides
    @Singleton
    FirebaseAPI providesFirebaseAPI(FirebaseAuth auth, DatabaseReference databaseReference) {
        return new FirebaseAPI(auth, databaseReference);
    }

    @Provides
    @Singleton
    FirebaseAuth providesFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    DatabaseReference providesDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

}
