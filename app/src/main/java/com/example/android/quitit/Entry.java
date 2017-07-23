package com.example.android.quitit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Ayush vaid on 12-06-2017.
 */
public class Entry implements Parcelable {

    private  String name;
    private  int age;
    private String sex;
    private String contact;
    private String med_history;
    private int smoke_history;
    private String interest;
    private String marry_status;
    private int smoke_freq;
    private float cost;
    private String future;
    private String business;
    private int salary;
    private String time;
    private String formattedDate;
    private int id;
    public Entry(){

    }

    /*public Entry(String name,int age,String sex,String interest){
        this.name=name;
        this.age=age;
        this.sex=sex;
        this.interest=interest;
    }*/
   // Entry patient=new Entry(name,age,sex,interest,med_history,contact,days,freq,cost,m_status,future);

    public Entry(String name,int age,String sex,String interest,String med,String contact,int days,int freq,float cost,String marry_status,String future,String business,int salary,String time,String date,int id){
        this.name=name;
        this.age=age;
        this.sex=sex;
        this.interest=interest;
        this.med_history=med;
        this.contact=contact;
        this.smoke_history=days;
        this.smoke_freq=freq;
        this.cost=cost;
        this.marry_status=marry_status;
        this.future=future;
        this.business=business;
        this.salary=salary;
        this.time=time;
        this.formattedDate=date;
        this.id=id;
    }

    //getter methods
    public String getName(){return  name;}
    public int getAge(){return age;}
    public String getSex(){return sex;}
    public String getInterest(){return  interest;}
    public String getMed_history(){return  med_history;}
    public String getContact(){return  contact;}
    public int getSmokeHistory(){return  smoke_history;}
    public int getSmoke_freq(){return  smoke_freq;}
    public float getCost(){return  cost;}
    public String getMarry_status(){return  marry_status;}
    public String getFuture(){return  future;}
    public String getBusiness(){return business;}
    public int getSalary(){return salary;}
    public String getTime(){return time;}
    public String getFormattedDate(){return formattedDate;}
    public int getId(){return id;}


    //parcealable stuff
    public Entry(Parcel in) {
        name = in.readString();
        age = in.readInt();
        sex = in.readString();
        interest = in.readString();
        med_history = in.readString();
        contact = in.readString();
        smoke_history = in.readInt();     // Entry patient=new Entry(name,age,sex,interest,med_history,contact,days,freq,cost,m_status,future);
        smoke_freq=in.readInt();
        cost=in.readFloat();
        marry_status = in.readString();
        future=in.readString();
        business=in.readString();
        salary=in.readInt();
        formattedDate=in.readString();
        time=in.readString();
        id=in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Entry> CREATOR = new Parcelable.Creator<Entry>() {
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeInt(age);
        parcel.writeString(sex);
        parcel.writeString(interest);
        parcel.writeString(med_history);
        parcel.writeString(contact);
        parcel.writeInt(smoke_history);
        parcel.writeInt(smoke_freq);
        parcel.writeFloat(cost);
        parcel.writeString(marry_status);
        parcel.writeString(future);
        parcel.writeString(business);
        parcel.writeInt(salary);
        parcel.writeString(formattedDate);
        parcel.writeString(time);
        parcel.writeInt(id);
    }
}
