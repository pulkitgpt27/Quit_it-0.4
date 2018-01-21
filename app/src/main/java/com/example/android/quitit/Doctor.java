package com.example.android.quitit;

import java.util.HashMap;
import java.util.List;
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

    public Doctor(){
    }

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

    public Doctor(String name, String mailId){
//        this.name=name;
//        this.mailId=mailId;
    }
}
