package com.apps.cb.flashcards.login;

import com.apps.cb.flashcards.lib.base.EventBus;
import com.apps.cb.flashcards.login.events.LoginEvent;
import com.apps.cb.flashcards.login.ui.LoginView;
import com.facebook.AccessToken;

import org.greenrobot.eventbus.Subscribe;


/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
public class LoginPresenterImpl implements LoginPresenter{

    private EventBus eventBus;
    private LoginInteractor interactor;
    private LoginView view;

    public LoginPresenterImpl(EventBus eventBus, LoginInteractor interactor, LoginView view) {
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
    }

    @Override
    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        if(view != null){
            switch (event.getType()){
                case LoginEvent.LOGIN_SUCCESS:
                    onLoginSuccess();
                    break;
                case LoginEvent.LOGIN_ERROR:
                    onLoginFail(event);
                    break;
            }
        }
    }

    private void onLoginFail(LoginEvent event) {
        view.hideProgressBar();
        view.showUIElements();
        view.loginError(event.getMessage());
    }

    private void onLoginSuccess() {
        view.hideProgressBar();
        view.setUserEmail(null);
        view.handleSignIn();
    }

    @Override
    public void login(AccessToken token) {
        view.hideUIElements();
        view.showProgressBar();
        interactor.execute(token);
    }
}
