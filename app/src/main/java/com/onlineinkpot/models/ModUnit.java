package com.onlineinkpot.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by USER on 8/26/2017.
 */

public class ModUnit implements Serializable {

    private int sId;
    private String uId;
    private String uName;
    private String uSubId;
    private String contentidnew;
    private String filename;
    private List<ModChapter> chapList;

    public ModUnit(int sId, String uId, String uName, String uSubId) {
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

    public String getuSubId() {
        return uSubId;
    }

    public String getContentidnew() {
        return contentidnew;
    }

    public String getFilename() {
        return filename;
    }

    public List<ModChapter> getChapList() {
        return chapList;
    }
}
