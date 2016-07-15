package com.apps.cb.flashcards.addflashcard.di;

import com.apps.cb.flashcards.FlashCardsAppModule;
import com.apps.cb.flashcards.addflashcard.ui.AddFlashCardActivity;
import com.apps.cb.flashcards.domain.di.DomainModule;
import com.apps.cb.flashcards.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
@Singleton
@Component(modules = {AddFlashCardModule.class, DomainModule.class, LibsModule.class, FlashCardsAppModule.class})
public interface AddFlashCardComponent {
    void inject(AddFlashCardActivity activity);
}
