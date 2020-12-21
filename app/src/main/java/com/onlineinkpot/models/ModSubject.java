package com.onlineinkpot.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by USER on 8/26/2017.
 */

public class ModSubject implements Serializable {

    private String subId, subName, classId, semId, courseId;
    private List<ModUnit> unitList;
    public boolean isExpanded;

    public ModSubject() {
    }

    public ModSubject(String subId, String subName, String classId, String semId, String courseId, List<ModUnit> unitList) {
        this.subId = subId;
        this.subName = subName;
        this.classId = classId;
        this.semId = semId;
        this.courseId = courseId;
        this.unitList = unitList;
        this.isExpanded = false;
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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSemId() {
        return semId;
    }

    public void setSemId(String semId) {
        this.semId = semId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public List<ModUnit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<ModUnit> unitList) {
        this.unitList = unitList;
    }
    }