package com.apps.cb.flashcards.addflashcard.ui;

/**
 * Fashr
 * Created by cristianbehe on 7/14/2016.
 */
public interface AddFlashCardView {

    void displayMessage(String message);

    void goToMainScreen();

    void onUploadInit();
    void onUploadComplete();
    void onUploadError(String error);
}
