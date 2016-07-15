package com.apps.cb.flashcards.main;

import com.apps.cb.flashcards.entities.FlashCard;
import com.apps.cb.flashcards.lib.base.EventBus;
import com.apps.cb.flashcards.main.events.MainEvent;
import com.apps.cb.flashcards.main.ui.MainView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Fashr
 * Created by cristianbehe on 7/13/2016.
 */
public class MainPresenterImpl implements MainPresenter {

    private EventBus eventBus;
    private MainInteractor interactor;
    private MainView view;

    public MainPresenterImpl(EventBus eventBus, MainInteractor interactor, MainView view) {
        this.eventBus = eventBus;
        this.interactor = interactor;
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
    public void logout() {
        interactor.logout();
    }

    @Override
    public void subscribe() {
        interactor.subscribe();
    }

    @Override
    public void unsubscribe() {
        interactor.unsubscribe();
    }

    @Override
    public void toggleFavorite(FlashCard card) {
        interactor.executeUpdate(card);
    }

    @Override
    public void removeCard(FlashCard card) {
        interactor.executeRemove(card);
    }

    @Override
    @Subscribe
    public void onEventMainThread(MainEvent event) {
        if (view != null) {
            switch (event.getType()) {
                case MainEvent.READ_EVENT:
                    if (event.getCard() != null)
                        view.onFlashCardAdded(event.getCard());
                    break;
                case MainEvent.DELETE_EVENT:
                    view.onFlashCardRemoved(event.getCard());
                    break;
                case MainEvent.UPDATE_EVENT:
                    view.onFlashCardUpdated(event.getCard());
            }
        }
    }
}
