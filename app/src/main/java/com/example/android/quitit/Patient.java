package com.example.android.quitit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kakrya on 3/23/2018.
 */

public class Patient implements Parcelable{
    private String emailId;
    private String username;
    private String password;
    private String doctor_key;
    private String entry_key;

    //Constructors
    public Patient(Parcel in){
        emailId = in.readString();
        username = in.readString();
        password = in.readString();
        doctor_key = in.readString();
        entry_key = in.readString();
    }

    public Patient(){
        //EMPTY CONSTRUCTOR
    };

    //getters
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
    public String getEntry_key() {
        return entry_key;
    }

    //setters
    public void setDoctor_key(String doctor_key) {
        this.doctor_key = doctor_key;
    }
    public void setEntry_key(String entry_key) {
        this.entry_key = entry_key;
    }

    @Override
    public int describeContents() { return 0; }

    public static final Parcelable.Creator<Patient> CREATOR = new Parcelable.Creator<Patient>() {
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.emailId);
        parcel.writeString(this.username);
        parcel.writeString(this.password);
        parcel.writeString(this.doctor_key);
        parcel.writeString(this.entry_key);
    }
}
