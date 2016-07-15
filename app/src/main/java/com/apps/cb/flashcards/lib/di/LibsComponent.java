package com.apps.cb.flashcards.lib.di;

import com.apps.cb.flashcards.FlashCardsAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Fashr
 * Created by cristianbehe on 7/15/2016.
 */
@Singleton
@Component(modules = {LibsModule.class, FlashCardsAppModule.class})
public interface LibsComponent {
}
