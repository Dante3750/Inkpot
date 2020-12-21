package com.onlineinkpot.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by USER on 8/31/2017.
 */

public class ModTopic implements Serializable {

    private int sId;
    private String uId;
    private String uName;
    private String uSubId;
    private List<ModChapter> chapList;

    public ModTopic() {
    }

    public ModTopic(int sId, String uId, String uName, List<ModChapter> chapList) {
        this.sId = sId;
        this.uId = uId;
        this.uName = uName;
        this.chapList = chapList;
    }

    public ModTopic(int sId, String uId, String uName, String uSubId) {
        this.sId = sId;
        this.uId = uId;
        this.uName = uName;
        this.uSubId = uSubId;
    }

    public int getsId() {
        return sId;
    }

    public String getuId() {
        return uId;
    }

    public String getuName() {
        return uName;
    }

    public String gettName() {
        return uName;
    }

    public String getuSubId() {
        return uSubId;
    }

    public List<ModChapter> getChapList() {
        return chapList;
    }
}
