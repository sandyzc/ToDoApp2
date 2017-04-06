package com.sandyzc.todoApp.beans;

//Created by Santosh Sc

public class beans {

    private String Title;
    private String Descp;
    private String Date;
    private int Status;
    private int id;

    public beans(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescp() {
        return Descp;
    }

    public void setDescp(String descp) {
        Descp = descp;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
