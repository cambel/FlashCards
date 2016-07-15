package com.apps.cb.flashcards.login.di;

import com.apps.cb.flashcards.domain.FirebaseAPI;
import com.apps.cb.flashcards.lib.base.EventBus;
import com.apps.cb.flashcards.login.LoginInteractor;
import com.apps.cb.flashcards.login.LoginInteractorImpl;
import com.apps.cb.flashcards.login.LoginPresenter;
import com.apps.cb.flashcards.login.LoginPresenterImpl;
import com.apps.cb.flashcards.login.LoginRepository;
import com.apps.cb.flashcards.login.LoginRepositoryImpl;
import com.apps.cb.flashcards.login.ui.LoginView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
@Module
public class LoginModule {
    LoginView view;

    public LoginModule(LoginView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    LoginView providesLoginView() {
        return this.view;
    }

    @Provides
    @Singleton
    LoginPresenter providesLoginPresenter(EventBus eventBus, LoginInteractor interactor, LoginView view) {
        return new LoginPresenterImpl(eventBus, interactor, view);
    }

    @Provides
    @Singleton
    LoginInteractor providesLoginInteractor(LoginRepository repository) {
        return new LoginInteractorImpl(repository);
    }


    @Provides
    @Singleton
    LoginRepository providesLoginRepository(EventBus eventBus, FirebaseAPI firebase) {
        return new LoginRepositoryImpl(eventBus, firebase);
    }
}
