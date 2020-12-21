package com.onlineinkpot.models;

/**
 * Created by USER on 9/29/2017.
 */

public class QuestionModel {

    private String  questionid, correctanswervalue, studentanswer,option1, option2, option3, option4, correctanswer, question, totalpricevalue, price, subTotal, duration, subjectName, studentId, semesterId, subjectId, courseId, totalprice;

    public QuestionModel() {

    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price)

    {
        this.price = price;
    }



    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getQuestionId()
    {
        return questionid;
    }

    public void setQuestionId(String questionid)
    {
        this.questionid = questionid;
    }





    public String getCorrectAnswer()
    {
        return correctanswer;
    }

    public void setCorrectAnswer(String correctanswer)
    {
        this.correctanswer = correctanswer;
    }



    public String getStudentAnswer()
    {
        return studentanswer;
    }

    public void setStudentAnswer(String studentanswer)
    {
        this.studentanswer = studentanswer;
    }




    public String getCorrectAnswerValue()
    {
        return correctanswervalue;
    }

    public void setCorrectAnswerValue(String correctanswervalue)
    {
        this.correctanswervalue = correctanswervalue;
    }













    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
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

    public QuestionModel(String price, String subTotal, String duration, String subjectName) {

        this.price = price;
        this.subTotal = subTotal;
        this.duration = duration;
        this.subjectName = subjectName;
    }
}