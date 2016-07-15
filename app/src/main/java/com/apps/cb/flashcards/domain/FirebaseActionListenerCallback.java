package com.apps.cb.flashcards.domain;

public interface FirebaseActionListenerCallback {
    void onSuccess();

    void onError(String error);
}
