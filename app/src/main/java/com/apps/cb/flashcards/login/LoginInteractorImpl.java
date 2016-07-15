package com.apps.cb.flashcards.login;

import com.facebook.AccessToken;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
public class LoginInteractorImpl implements LoginInteractor{


    LoginRepository repository;

    public LoginInteractorImpl(LoginRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(AccessToken accessToken) {
        repository.signIn(accessToken);
    }
}
