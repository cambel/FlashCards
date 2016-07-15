package com.apps.cb.flashcards.login.di;

import com.apps.cb.flashcards.FlashCardsAppModule;
import com.apps.cb.flashcards.domain.di.DomainModule;
import com.apps.cb.flashcards.lib.di.LibsModule;
import com.apps.cb.flashcards.login.ui.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
@Singleton
@Component(modules = {LoginModule.class, DomainModule.class, FlashCardsAppModule.class, LibsModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
