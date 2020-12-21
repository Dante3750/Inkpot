package com.onlineinkpot.models;

/**
 * Created by Akshay on 9/6/2017.
 */

public class OrderHistoryModel {

    private String subject_id, subject_name, semester, price, discount, tax, expiredate;

    public OrderHistoryModel() {

    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
    }

    public OrderHistoryModel(String subject_id, String subject_name, String semester, String price, String discount, String tax, String expiredate) {

        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.semester = semester;
        this.price = price;
        this.discount = discount;
        this.tax = tax;
        this.expiredate = expiredate;
    }

}