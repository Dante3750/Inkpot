package com.onlineinkpot.models;

/**
 * Created by USER on 9/14/2017.
 */

public class BasicPriceModel {
    private String totalpricevalue, price, subTotal, duration, subjectName, studentId, semesterId, subjectId, courseId, totalprice;

    public BasicPriceModel() {

    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price)

    {
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

    public String getCourseId()

    {
        return courseId;
    }

    public void setCourseId(String courseId)

    {
        this.courseId = courseId;
    }

    public String getTotalPrice()

    {
        return totalprice;
    }

    public void setTotalPrice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getTotalPriceValue()

    {
        return totalpricevalue;
    }

    public void setTotalPriceValue(String totalpricevalue) {
        this.totalpricevalue = totalpricevalue;
    }

    public BasicPriceModel(String price, String subTotal, String duration, String subjectName) {

        this.price = price;
        this.subTotal = subTotal;
        this.duration = duration;
        this.subjectName = subjectName;
    }
}
