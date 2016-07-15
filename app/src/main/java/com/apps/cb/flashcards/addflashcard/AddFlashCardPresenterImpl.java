package com.apps.cb.flashcards.addflashcard;

import com.apps.cb.flashcards.addflashcard.events.AddFlashCardEvent;
import com.apps.cb.flashcards.addflashcard.ui.AddFlashCardView;
import com.apps.cb.flashcards.entities.FlashCard;
import com.apps.cb.flashcards.lib.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Fashr
 * Created by cristianbehe on 7/14/2016.
 */
public class AddFlashCardPresenterImpl implements AddFlashCardPresenter {

    private EventBus eventBus;
    private SessionInteractor sessionInteractor;
    private AddFlashCardInteractor addFlashCardInteractor;
    private AddFlashCardView view;


    public AddFlashCardPresenterImpl(AddFlashCardInteractor addFlashCardInteractor, EventBus eventBus,
                                     SessionInteractor sessionInteractor, AddFlashCardView view) {
        this.addFlashCardInteractor = addFlashCardInteractor;
        this.eventBus = eventBus;
        this.sessionInteractor = sessionInteractor;
        this.view = view;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void saveCard(FlashCard card) {
        addFlashCardInteractor.executeCreate(card);
    }

    @Override
    public void logout() {
        sessionInteractor.executeLogout();
    }

    @Override
    @Subscribe
    public void onMainThread(AddFlashCardEvent event) {
        if (view != null) {
            switch (event.getType()) {
                case AddFlashCardEvent.FLASHCARD_ADDED:
                    view.goToMainScreen();
                    break;
                case AddFlashCardEvent.FLASHCARD_ERROR:
                    view.displayMessage(event.getMessage());
                    break;
                case AddFlashCardEvent.UPLOAD_INIT:
                    view.onUploadInit();
                    break;
                case AddFlashCardEvent.UPLOAD_COMPLETE:
                    view.onUploadComplete();
                    break;
                case AddFlashCardEvent.UPLOAD_ERROR:
                    view.onUploadError(event.getMessage());
                    break;
            }
        }
    }
}
