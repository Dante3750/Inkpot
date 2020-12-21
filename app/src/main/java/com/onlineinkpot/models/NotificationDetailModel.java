package com.onlineinkpot.models;

/**
 * Created by USER on 10/5/2017.
 */

public class NotificationDetailModel {
    public String topic, chapter, unit, subject, course, university, date;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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

    NotificationDetailModel(String topic, String chapter, String subject, String unit, String course, String university, String date) {
        this.topic = topic;
        this.chapter = chapter;
        this.subject = subject;
        this.unit = unit;
        this.course = course;
        this.university = university;
        this.date = date;

    }

    public NotificationDetailModel() {

    }
}
