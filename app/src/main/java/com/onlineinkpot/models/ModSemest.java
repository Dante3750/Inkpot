package com.onlineinkpot.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by USER on 8/26/2017.
 */

public class ModSemest implements Serializable {
    private String semName;

    public String getSemName() {
        return semName;
    }

    public void setSemName(String semName) {
        this.semName = semName;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public List<ModSubject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<ModSubject> subjectList) {
        this.subjectList = subjectList;
    }

    public String getResemid() {
        return resemid;
    }

    public void setResemid(String resemid) {
        this.resemid = resemid;
    }

    public ModSemest(String semName, String semesterId,String resemid ,List<ModSubject> subjectList) {
        this.semName = semName;
        this.semesterId = semesterId;
        this.subjectList = subjectList;
        this.resemid = resemid;
    }

    private String semesterId;
    private List<ModSubject> subjectList;
    private String resemid;
}