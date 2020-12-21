package com.onlineinkpot.models;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Akshay on 9/27/2017.
 */

public class Notification {
    private String Message, description;

    public Notification(String message, String description) {
        Message = message;
        this.description = description;
    }

    public Notification() {

    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
