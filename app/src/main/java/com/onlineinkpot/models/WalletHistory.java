package com.onlineinkpot.models;

/**
 * Created by mindz on 24/2/17.
 */


public class WalletHistory {

    private String orderNumber,cId,orderId,studentId,promocId,cbType,cbAmt,cbStatus,createdon,modifiedon,modifiedby;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCId() {
        return cId;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPromocId() {
        return promocId;
    }

    public void setPromocId(String promocId) {
        this.promocId = promocId;
    }

    public String getCbType() {
        return cbType;
    }

    public void setCbType(String cbType) {
        this.cbType = cbType;
    }

    public String getCbAmt() {
        return cbAmt;
    }

    public void setCbAmt(String cbAmt) {
        this.cbAmt = cbAmt;
    }

    public String getCbStatus() {
        return cbStatus;
    }

    public void setCbStatus(String cbStatus) {
        this.cbStatus = cbStatus;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public String getModifiedon() {
        return modifiedon;
    }

    public void setModifiedon(String modifiedon) {
        this.modifiedon = modifiedon;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }
}
