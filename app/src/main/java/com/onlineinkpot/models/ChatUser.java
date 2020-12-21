package com.onlineinkpot.models;

import java.io.Serializable;

/**
 * Created by USER on 8/16/2017.
 */

public class ChatUser implements Serializable {

    private String id, type, name, uName, uEmail, uMobile;

    public ChatUser(String id, String type, String name, String uName, String uEmail, String uMobile) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.uName = uName;
        this.uEmail = uEmail;
        this.uMobile = uMobile;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getuName() {
        return uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public String getuMobile() {
        return uMobile;
    }
}
