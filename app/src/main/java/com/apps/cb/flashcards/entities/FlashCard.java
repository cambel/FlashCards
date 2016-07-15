package com.apps.cb.flashcards.entities;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
@IgnoreExtraProperties
public class FlashCard implements Serializable{

    @Exclude
    private String id;

    private String userId;
    private String resourceUrl;
    private String category;
    private String title;
    private String notes;
    private boolean favorite;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Exclude
    public boolean equals(Object object) {
        boolean equals = false;
        if (object instanceof FlashCard) {
            FlashCard card = (FlashCard) object;
            if (card.id.equals(this.id))
                equals = true;
        }
        return equals;
    }
}
