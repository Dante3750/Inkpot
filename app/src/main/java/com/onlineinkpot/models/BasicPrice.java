package com.onlineinkpot.models;

/**
 * Created by john on 9/9/2017.
 */

public class BasicPrice {
    private String price,subTotal,duration,subjectName;

    public BasicPrice() {

    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public BasicPrice(String price, String subTotal, String duration, String subjectName) {

        this.price = price;
        this.subTotal = subTotal;
        this.duration = duration;
        this.subjectName = subjectName;
    }
}
