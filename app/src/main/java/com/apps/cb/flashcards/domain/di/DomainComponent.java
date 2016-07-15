package com.apps.cb.flashcards.domain.di;

import com.apps.cb.flashcards.FlashCardsAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
@Singleton
@Component(modules = {DomainModule.class, FlashCardsAppModule.class})
public interface DomainComponent {

}
