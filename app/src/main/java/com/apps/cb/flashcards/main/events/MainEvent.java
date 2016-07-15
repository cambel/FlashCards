package com.apps.cb.flashcards.main.events;

import com.apps.cb.flashcards.entities.FlashCard;

/**
 * Fashr
 * Created by cristianbehe on 7/13/2016.
 */
public class MainEvent {

    public static final int UPDATE_EVENT = 1;
    public static final int READ_EVENT = 2;
    public static final int DELETE_EVENT = 3;

    private int type;
    private String message;
    private FlashCard card;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FlashCard getCard() {
        return card;
    }

    public void setCard(FlashCard card) {
        this.card = card;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
