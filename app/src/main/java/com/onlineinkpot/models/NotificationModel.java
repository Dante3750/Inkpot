package com.onlineinkpot.models;

/**
 * Created by USER on 9/29/2017.
 */

public class NotificationModel {

    public String getHeader() {
        return header;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTopic() {
        return topic;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String flag, header, topic, chapter, unit, subject, semester, course, college, university, date;
    ;

    public NotificationModel() {

    }

    public NotificationModel(String header, String topic, String chapter, String unit, String subject, String semester, String course, String college, String university, String date) {

        this.header = header;
        this.topic = topic;
        this.chapter = chapter;
        this.unit = unit;
        this.subject = subject;
        this.semester = semester;
        this.course = course;
        this.college = college;
        this.university = university;
        this.date = date;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}