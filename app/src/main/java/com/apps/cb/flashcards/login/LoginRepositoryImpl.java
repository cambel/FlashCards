package com.apps.cb.flashcards.login;

import com.apps.cb.flashcards.domain.FirebaseAPI;
import com.apps.cb.flashcards.domain.FirebaseActionListenerCallback;
import com.apps.cb.flashcards.lib.base.EventBus;
import com.apps.cb.flashcards.login.events.LoginEvent;
import com.facebook.AccessToken;


/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
public class LoginRepositoryImpl implements LoginRepository {

    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;

    public LoginRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
    }

    @Override
    public void signIn(AccessToken accessToken) {
        firebaseAPI.login(accessToken, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                post(LoginEvent.LOGIN_SUCCESS);
            }

            @Override
            public void onError(String error) {
                post(LoginEvent.LOGIN_ERROR, error);
            }
        });
    }

    private  void post(int type){
        post(type, null);
    }
    private void post(int type, String message) {
        LoginEvent event = new LoginEvent();
        event.setType(type);
        event.setMessage(message);
        eventBus.post(event);
    }

}
