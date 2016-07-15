package com.apps.cb.flashcards.addflashcard.di;

import com.apps.cb.flashcards.addflashcard.AddFlashCardInteractor;
import com.apps.cb.flashcards.addflashcard.AddFlashCardInteractorImpl;
import com.apps.cb.flashcards.addflashcard.AddFlashCardPresenter;
import com.apps.cb.flashcards.addflashcard.AddFlashCardPresenterImpl;
import com.apps.cb.flashcards.addflashcard.AddFlashCardRepository;
import com.apps.cb.flashcards.addflashcard.AddFlashCardRepositoryImpl;
import com.apps.cb.flashcards.addflashcard.SessionInteractor;
import com.apps.cb.flashcards.addflashcard.SessionInteractorImpl;
import com.apps.cb.flashcards.addflashcard.ui.AddFlashCardView;
import com.apps.cb.flashcards.domain.FirebaseAPI;
import com.apps.cb.flashcards.lib.base.EventBus;
import com.apps.cb.flashcards.lib.base.ImageStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
@Module
public class AddFlashCardModule {
    AddFlashCardView view;

    public AddFlashCardModule(AddFlashCardView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    AddFlashCardView providesAddFlashCardView() {
        return this.view;
    }

    @Provides
    @Singleton
    AddFlashCardPresenter providesAddFlashCardPresenter(AddFlashCardInteractor addFlashCardInteractor, EventBus eventBus,
                                                        SessionInteractor sessionInteractor, AddFlashCardView view) {
        return new AddFlashCardPresenterImpl(addFlashCardInteractor, eventBus, sessionInteractor, view);
    }

    @Provides
    @Singleton
    AddFlashCardInteractor providesAddFlashCardInteractor(AddFlashCardRepository repository) {
        return new AddFlashCardInteractorImpl(repository);
    }

    @Provides
    @Singleton
    SessionInteractor providesSessionInteractor(AddFlashCardRepository repository) {
        return new SessionInteractorImpl(repository);
    }


    @Provides
    @Singleton
    AddFlashCardRepository providesAddFlashCardRepository(EventBus eventBus, FirebaseAPI firebase, ImageStorage storage) {
        return new AddFlashCardRepositoryImpl(eventBus, firebase, storage);
    }
}
