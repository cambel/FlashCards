package com.apps.cb.flashcards.lib.di;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.apps.cb.flashcards.lib.CloudinaryImageStorage;
import com.apps.cb.flashcards.lib.GlideImageLoader;
import com.apps.cb.flashcards.lib.GreenRobotEventBus;
import com.apps.cb.flashcards.lib.base.EventBus;
import com.apps.cb.flashcards.lib.base.ImageLoader;
import com.apps.cb.flashcards.lib.base.ImageStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class LibsModule {
    Activity activity;
    Fragment fragment;

    public LibsModule() {
    }

    public LibsModule(Activity activity) {
        this.activity = activity;
    }

    public LibsModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return new GreenRobotEventBus();
    }

    @Provides
    @Singleton
    ImageLoader provideImageLoader(Activity activity) {
        GlideImageLoader imageLoader = new GlideImageLoader();
        if (activity != null) {
            imageLoader.setLoaderContext(activity);
        }
        return imageLoader;
    }
    @Provides
    @Singleton
    ImageStorage providesImageStorage(Context context, EventBus eventBus) {
        ImageStorage imageStorage = new CloudinaryImageStorage(context, eventBus);
        return imageStorage;
    }

    @Provides
    @Singleton
    Activity provideActivity() {
        return this.activity;
    }

}
