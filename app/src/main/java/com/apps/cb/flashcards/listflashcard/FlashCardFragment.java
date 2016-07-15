package com.apps.cb.flashcards.listflashcard;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.apps.cb.flashcards.R;
import com.apps.cb.flashcards.entities.FlashCard;
import com.apps.cb.flashcards.lib.base.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlashCardFragment extends Fragment {


    @Bind(R.id.imgFlashCard)
    ImageView imgFlashCard;
    @Bind(R.id.txtTitle)
    TextView txtTitle;
    @Bind(R.id.txtNotes)
    TextView txtNotes;
    @Bind(R.id.viewSwitcher)
    ViewSwitcher viewSwitcher;

    private ImageLoader imageLoader;
    private FlashCard card;
    private OnClickListener onClickListener;

    public FlashCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flashcard_content, null);
        ButterKnife.bind(this, view);

        loadCard();

        return view;
    }


    private void loadCard() {
        if (imageLoader != null && card != null) {
            imageLoader.load(imgFlashCard, card.getResourceUrl());
            txtTitle.setText(card.getTitle());
            txtNotes.setText(card.getNotes());
        }
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @OnClick({R.id.viewSwitcher})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.viewSwitcher:
                viewSwitcher.showNext();
                break;
        }
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public void setCard(FlashCard card) {
        this.card = card;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
