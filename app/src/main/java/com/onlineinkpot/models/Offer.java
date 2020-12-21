package com.onlineinkpot.models;

/**
 * Created by john on 9/3/2017.
 */

public class Offer {
    private String coupon_id, coupon_name, coupon_price, coupon_front_img, coupon_back_img, coupon_des, coupon_code;

    public Offer() {
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public void setCoupon_id(String coupon_id) {

        this.coupon_id = coupon_id;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }

    public String getCoupon_front_img() {
        return coupon_front_img;
    }

    public void setCoupon_front_img(String coupon_front_img) {
        this.coupon_front_img = coupon_front_img;
    }

    public String getCoupon_back_img() {
        return coupon_back_img;
    }

    public void setCoupon_back_img(String coupon_back_img) {
        this.coupon_back_img = coupon_back_img;
    }

    public String getCoupon_des() {
        return coupon_des;
    }

    public void setCoupon_des(String coupon_des) {
        this.coupon_des = coupon_des;
    }

    public Offer(String coupon_id, String coupon_name, String coupon_price, String coupon_front_img, String coupon_back_img, String coupon_des, String coupon_code) {
        this.coupon_id = coupon_id;
        this.coupon_name = coupon_name;
        this.coupon_price = coupon_price;
        this.coupon_front_img = coupon_front_img;
        this.coupon_back_img = coupon_back_img;
        this.coupon_des = coupon_des;
        this.coupon_code = coupon_code;

    }
}