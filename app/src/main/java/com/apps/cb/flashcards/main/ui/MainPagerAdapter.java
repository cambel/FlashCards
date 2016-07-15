package com.apps.cb.flashcards.main.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseBooleanArray;

import com.apps.cb.flashcards.entities.FlashCard;
import com.apps.cb.flashcards.lib.base.ImageLoader;
import com.apps.cb.flashcards.listflashcard.FlashCardFragment;
import com.apps.cb.flashcards.listflashcard.OnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Fashr
 * Created by cristianbehe on 7/14/2016.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {


    private List<FlashCard> flashCards;
    private ImageLoader imageLoader;
    private OnClickListener onClickListener;
    private SparseBooleanArray pendingUpdate;

    public MainPagerAdapter(FragmentManager fm, ImageLoader imageLoader, OnClickListener onClickListener) {
        super(fm);
        this.imageLoader = imageLoader;
        this.onClickListener = onClickListener;
        flashCards = new ArrayList<>();
        pendingUpdate = new SparseBooleanArray();
    }

    @Override
    public Fragment getItem(int position) {

        FlashCardFragment fragment;

        fragment = new FlashCardFragment();
        fragment.setCard(flashCards.get(position));
        fragment.setImageLoader(imageLoader);
        fragment.setOnClickListener(onClickListener);

        return fragment;
    }

    @Override
    public int getCount() {
        return flashCards.size();
    }

    @Override
    public int getItemPosition(Object object) {
        int index = flashCards.indexOf(object);
        if (pendingUpdate.get(index, false)) {
            pendingUpdate.delete(index);
            return POSITION_NONE;
        } else
            return index == -1 ? POSITION_NONE : index;
    }

    public void addItem(FlashCard card) {
        flashCards.add(card);
        notifyDataSetChanged();
    }

    public void removeItem(FlashCard card) {
        flashCards.remove(card);
        notifyDataSetChanged();
    }

    public FlashCard getFlashCard(int position) {
        if (flashCards.isEmpty())
            return null;
        else
            return flashCards.get(position);
    }

    public void updateItem(FlashCard card) {
        if (flashCards.contains(card)) {
            int index = flashCards.indexOf(card);
            flashCards.set(index, card);
            pendingUpdate.put(index, true);
        }
        notifyDataSetChanged();
    }
}
