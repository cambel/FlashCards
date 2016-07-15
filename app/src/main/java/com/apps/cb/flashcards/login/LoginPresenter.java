package com.apps.cb.flashcards.login;

import com.apps.cb.flashcards.login.events.LoginEvent;
import com.facebook.AccessToken;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
public interface LoginPresenter {
    void onCreate();
    void onDestroy();
    void onEventMainThread(LoginEvent event);
    void login(AccessToken token);
}
