package com.onlineinkpot.models;

/**
 * Created by john on 9/18/2017.
 */

public class FreeSubMod {
    private String Vname,VtopicName,vtopicId;

    public FreeSubMod() {

    }

    public String getVname() {
        return Vname;
    }

    public void setVname(String vname) {
        Vname = vname;
    }

    public String getVtopicName() {
        return VtopicName;
    }

    public void setVtopicName(String vtopicName) {
        VtopicName = vtopicName;
    }

    public String getVtopicId() {
        return vtopicId;
    }

    public void setVtopicId(String vtopicId) {
        this.vtopicId = vtopicId;
    }

    public FreeSubMod(String vname, String vtopicName, String vtopicId) {

        Vname = vname;
        VtopicName = vtopicName;
        this.vtopicId = vtopicId;
    }
}
