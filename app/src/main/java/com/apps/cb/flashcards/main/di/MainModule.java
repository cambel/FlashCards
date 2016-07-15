package com.apps.cb.flashcards.main.di;

import com.apps.cb.flashcards.domain.FirebaseAPI;
import com.apps.cb.flashcards.lib.base.EventBus;
import com.apps.cb.flashcards.lib.base.ImageLoader;
import com.apps.cb.flashcards.main.MainInteractor;
import com.apps.cb.flashcards.main.MainInteractorImpl;
import com.apps.cb.flashcards.main.MainPresenter;
import com.apps.cb.flashcards.main.MainPresenterImpl;
import com.apps.cb.flashcards.main.MainRepository;
import com.apps.cb.flashcards.main.MainRepositoryImpl;
import com.apps.cb.flashcards.main.ui.MainActivity;
import com.apps.cb.flashcards.main.ui.MainPagerAdapter;
import com.apps.cb.flashcards.main.ui.MainView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
@Module
public class MainModule {
    MainView view;
    MainActivity activity;

    public MainModule(MainView view, MainActivity appCompatActivity) {
        this.view = view;
        this.activity = appCompatActivity;
    }

    @Provides
    @Singleton
    MainView providesMainView() {
        return this.view;
    }

    @Provides
    @Singleton
    MainPresenter providesMainPresenter(EventBus eventBus, MainInteractor interactor, MainView view) {
        return new MainPresenterImpl(eventBus, interactor, view);
    }

    @Provides
    @Singleton
    MainInteractor providesMainInteractor(MainRepository repository) {
        return new MainInteractorImpl(repository);
    }


    @Provides
    @Singleton
    MainRepository providesMainRepository(EventBus eventBus, FirebaseAPI firebase) {
        return new MainRepositoryImpl(eventBus, firebase);
    }

    @Provides
    @Singleton
    MainPagerAdapter providesMainPagerAdapter(ImageLoader imageLoader) {
        return new MainPagerAdapter(activity.getSupportFragmentManager(), imageLoader, activity);
    }
}
