package com.apps.cb.flashcards.login.events;

/**
 * Fashr
 * Created by cristianbehe on 7/12/2016.
 */
public class LoginEvent {

    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_ERROR = 1;

    private int type;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
