package com.example.android.quitit;

import java.io.Serializable;

/**
 * Created by kakrya on 3/23/2018.
 */

public class Patient implements Serializable{
    private String emailId;
    private String username;
    private String password;
    private String doctor_key;
    private String entry_key;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDoctor_key() {
        return doctor_key;
    }

    public void setDoctor_key(String doctor_key) {
        this.doctor_key = doctor_key;
    }

    public String getEntry_key() {
        return entry_key;
    }

    public void setEntry_key(String entry_key) {
        this.entry_key = entry_key;
    }
}
