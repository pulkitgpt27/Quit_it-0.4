package com.example.android.quitit;

import java.util.HashMap;
/**
 * Created by pulkit gupta on 20/01/2018.
 */

public class Doctor {
    private HashMap<String,String> name;
    private HashMap<String,Integer> date_of_birth;
    private String mail_id;
    private long phone_no;
    private HashMap<String,Entry> patients;
    private String degree;

    //*********Constructors***********
    public Doctor(){
    }
    public Doctor(String mailId){
        this.mail_id=mailId;
        this.name = new HashMap<String,String>();
        this.date_of_birth = new HashMap<String,Integer>();
        this.patients = new HashMap<String,Entry>();
    }

    //**********Getters*************
    public String getMail_id() {
        return mail_id;
    }
    public HashMap<String,String> getName() {
        return name;
    }
    public HashMap<String,Integer> getDate_of_birth() {
        return date_of_birth;
    }
    public long getPhone_no() {
        return phone_no;
    }
    public HashMap<String,Entry> getPatients() {
        return patients;
    }
    public String getDegree() {
        return degree;
    }


    //***********Setters*************
    public void setMail_id(String mail_id) {
        this.mail_id = mail_id;
    }
}