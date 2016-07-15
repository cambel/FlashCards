package com.apps.cb.flashcards.main.di;

import com.apps.cb.flashcards.FlashCardsAppModule;
import com.apps.cb.flashcards.domain.di.DomainModule;
import com.apps.cb.flashcards.lib.di.LibsModule;
import com.apps.cb.flashcards.main.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
@Singleton
@Component(modules = {MainModule.class, DomainModule.class, FlashCardsAppModule.class, LibsModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
