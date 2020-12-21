package com.onlineinkpot.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by USER on 8/30/2017.
 */

public class ModChapter implements Serializable {

    private String chapId, chapName, chapDesc, chapSeq;
    private List<ModTopic> topList;

    public ModChapter(String chapId, String chapName, String chapDesc, String chapSeq, List<ModTopic> topList) {
        this.chapId = chapId;
        this.chapName = chapName;
        this.chapDesc = chapDesc;
        this.chapSeq = chapSeq;
        this.topList = topList;
    }

    public ModChapter() {
    }

    public String getChapId() {
        return chapId;
    }

    public String getChapName() {
        return chapName;
    }

    public String getChapDesc() {
        return chapDesc;
    }

    public String getChapSeq() {
        return chapSeq;
    }

    public List<ModTopic> getTopList() {
        return topList;
    }
}

