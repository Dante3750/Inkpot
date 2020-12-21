package com.onlineinkpot.models;

/**
 * Created by Akshay on 9/6/2017.
 */

public class MyCourse {
    private String mySubject_id, mySubject_name, myCourse_id, mySemester_id, mySemester_name, myCourseColor, myCourseIcon, myCourseName;

    public MyCourse() {

    }

    public String getMySubject_id() {
        return mySubject_id;
    }

    public void setMySubject_id(String mySubject_id) {
        this.mySubject_id = mySubject_id;
    }

    public String getMySubject_name() {
        return mySubject_name;
    }

    public void setMySubject_name(String mySubject_name) {
        this.mySubject_name = mySubject_name;
    }

    public String getMyCourse_id() {
        return myCourse_id;
    }

    public void setMyCourse_id(String myCourse_id) {
        this.myCourse_id = myCourse_id;
    }

    public String getMySemester_id() {
        return mySemester_id;
    }

    public void setMySemester_id(String mySemester_id) {
        this.mySemester_id = mySemester_id;
    }

    public String getMySemester_name() {
        return mySemester_name;
    }

    public void setMySemester_name(String mySemester_name) {
        this.mySemester_name = mySemester_name;
    }

    public String getMyCourseColor() {
        return myCourseColor;
    }

    public void setMyCourseColor(String myCourseColor) {
        this.myCourseColor = myCourseColor;
    }

    public String getMyCourseIcon() {
        return myCourseIcon;
    }

    public void setMyCourseIcon(String myCourseIcon) {
        this.myCourseIcon = myCourseIcon;
    }

    public String getMyCourseName() {
        return myCourseName;
    }

    public void setMyCourseName(String myCourseName) {
        this.myCourseName = myCourseName;
    }

    public MyCourse(String mySubject_id, String mySubject_name, String myCourse_id, String mySemester_id, String mySemester_name, String myCourseColor, String myCourseIcon, String myCourseName) {
        this.mySubject_id = mySubject_id;
        this.mySubject_name = mySubject_name;
        this.myCourse_id = myCourse_id;
        this.mySemester_id = mySemester_id;
        this.mySemester_name = mySemester_name;
        this.myCourseColor = myCourseColor;
        this.myCourseIcon = myCourseIcon;
        this.myCourseName = myCourseName;
    }
}