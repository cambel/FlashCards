package com.apps.cb.flashcards.listflashcard;

import com.apps.cb.flashcards.entities.FlashCard;

/**
 * Fashr
 * Created by cristianbehe on 7/15/2016.
 */
public interface OnClickListener {
    void onRemoveClick(FlashCard card);

    void onFavoriteClick(FlashCard card);
}
