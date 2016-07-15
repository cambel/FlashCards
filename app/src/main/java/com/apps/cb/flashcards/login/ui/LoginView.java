package com.apps.cb.flashcards.login.ui;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
public interface LoginView {

    void handleSignIn();
    void loginError(String error);
    void setUserEmail(String email);

    void hideUIElements();
    void showUIElements();
    void hideProgressBar();
    void showProgressBar();
}
