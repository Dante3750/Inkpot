package com.onlineinkpot.models;

/**
 * Created by Akshay on 9/29/2017.
 */

public class ModTestSubject {
    private String subId,subName;

    public ModTestSubject() {

    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public ModTestSubject(String subId, String subName) {

        this.subId = subId;
        this.subName = subName;
    }
}
