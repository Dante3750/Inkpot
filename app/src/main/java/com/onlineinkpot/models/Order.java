package com.onlineinkpot.models;

/**
 * Created by USER on 9/3/2017.
 */

public class Order {
    private String order_number, order_price, course_name, course_valid, payment_mode, payment_status, order_status, purchase_date, order_id;

    public Order() {

    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_valid() {
        return course_valid;
    }

    public void setCourse_valid(String course_valid) {
        this.course_valid = course_valid;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Order(String order_number, String order_price, String course_name, String course_valid, String payment_mode, String payment_status, String order_status, String purchase_date, String order_id) {
        this.order_number = order_number;
        this.order_price = order_price;
        this.course_name = course_name;
        this.course_valid = course_valid;
        this.payment_mode = payment_mode;
        this.payment_status = payment_status;
        this.order_status = order_status;

        this.purchase_date = purchase_date;
        this.order_id = order_id;
    }
}