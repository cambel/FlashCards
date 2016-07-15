package com.apps.cb.flashcards.addflashcard.events;

import com.apps.cb.flashcards.entities.FlashCard;

/**
 * Fashr
 * Created by cristianbehe on 7/14/2016.
 */
public class AddFlashCardEvent {

    public static final int FLASHCARD_ADDED = 0;
    public static final int FLASHCARD_ERROR = 1;
    public static final int UPLOAD_COMPLETE = 2;
    public static final int UPLOAD_ERROR = 3;
    public static final int UPLOAD_INIT = 4;

    private int type;
    private String message;
    private FlashCard card;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public FlashCard getCard() {
        return card;
    }

    public void setCard(FlashCard card) {
        this.card = card;
    }
}
