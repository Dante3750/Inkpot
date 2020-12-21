package com.onlineinkpot.models;

/**
 * Created by USER on 9/14/2017.
 */

public class Coupon {
    private String totalpricevalue, couponimage, couponsdes, couponid, couponname, couponprice, couponcode, couponexpire, subTotal, duration, subjectName, studentId, semesterId, subjectId, courseId, totalprice;
    private boolean isSelected;

    public Coupon() {

    }

    public String getCouponId() {
        return couponid;
    }

    public void setCouponId(String couponid)


    {
        this.couponid = couponid;
    }

    public String getCouponName() {
        return couponname;
    }

    public void setCouponName(String couponnname)

    {
        this.couponname = couponnname;
    }

    public String getCouponPrice() {
        return couponprice;
    }

    public void setCouponPrice(String couponprice) {
        this.couponprice = couponprice;
    }

    public String getCouponImage() {
        return couponimage;
    }

    public void setCouponImage(String couponimage) {
        this.couponimage = couponimage;
    }

    public String getCouponSdes() {
        return couponsdes;
    }

    public void setCouponSdes(String couponsdes) {
        this.couponsdes = couponsdes;
    }

    public String getTotalPriceValue()

    {
        return totalpricevalue;
    }

    public void setTotalPriceValue(String totalpricevalue) {
        this.totalpricevalue = totalpricevalue;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getCouponCode() {
        return couponcode;
    }

    public void setCouponCode(String couponcode) {
        this.couponcode = couponcode;
    }

    public String getCouponExpire() {
        return couponexpire;
    }

    public void setCouponExpire(String couponexpire) {
        this.couponexpire = couponexpire;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getSubjectId()

    {
        return subjectId;
    }

    public void setSubjectId(String subjectId
    ) {
        this.subjectId = subjectId;
    }

    public String getSubjectName()

    {
        return subjectName;
    }

    public void setSubjectName(String subjectName)

    {
        this.subjectName = subjectName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTotalPrice() {
        return totalprice;
    }

    public void setTotalPrice(String totalprice) {
        this.totalprice = totalprice;
    }

}