package com.example.android.quitit;

import android.os.Parcel;
import android.os.Parcelable;

public class Entry implements Parcelable {

    private  String name;
    private  int age;
    private String sex;
    private String contact;
    private String email;
    private String address;
    private String med_history;
    private String chewText;
    private int chew_history;
    private int chew_freq;
    private float chew_cost;
    private String smokeText;
    private int smoke_history;
    private String interest;
    private String marry_status;
    private int smoke_freq;
    private float smoke_cost;
    private String business;
    private int salary;
    private String time;
    private String formattedDate;
    private String id;
    private String morning_status;
    private String family_status;
    private String habit_reason;
    private String habit;
    private String aware_status;
    private String aware_disease;
    private String quit_status;
    private String quit_reason;
    private String quit_before_status;
    private String craving_time;
    private String message;

    public Entry() {
    }

    public Entry(String name, int age, String sex, String interest, String med, String contact, String email, String address, String chewText, int chew_days, int chew_freq, float chew_cost, String smokeText, int smoke_days, int smoke_freq, float smoke_cost, String marry_status, String business, int salary, String time, String date,
                 String morning_status, String family_status, String habit_reason, String habit, String aware_status, String aware_diseases, String quit_status, String quit_reason, String quit_before_status, String craving_time, String id,
                 String message){
        this.name=name;
        this.age=age;
        this.sex=sex;
        this.interest=interest;
        this.med_history=med;
        this.contact=contact;
        this.email=email;
        this.address=address;
        this.chewText=chewText;
        this.chew_history=chew_days;
        this.chew_freq=chew_freq;
        this.chew_cost=chew_cost;
        this.smokeText=smokeText;
        this.smoke_history=smoke_days;
        this.smoke_freq=smoke_freq;
        this.smoke_cost=smoke_cost;
        this.marry_status=marry_status;
        this.business=business;
        this.salary=salary;
        this.time=time;
        this.formattedDate=date;
        this.id=id;
        this.morning_status=morning_status;
        this.family_status=family_status;
        this.habit_reason=habit_reason;
        this.habit=habit;
        this.aware_status=aware_status;
        this.aware_disease=aware_diseases;
        this.quit_status=quit_status;
        this.quit_reason=quit_reason;
        this.quit_before_status=quit_before_status;
        this.craving_time=craving_time;
        this.message=message;
    }

    //getter methods

    public String getName(){return  name;}
    public int getAge(){return age;}
    public String getSex(){return sex;}
    public String getInterest(){return  interest;}
    public String getMed_history(){return  med_history;}
    public String getContact(){return  contact;}
    public String getEmail(){return email;}
    public String getAddress(){return address;}
    public String getChewText(){return chewText;}

    public int getChew_history(){return  chew_history;}
    public int getChew_freq(){return  chew_freq;}
    public float getChew_cost(){return  chew_cost;}

    public String getSmokeText(){return smokeText;}
    public int getSmokeHistory(){return  smoke_history;}
    public int getSmoke_freq(){return  smoke_freq;}
    public float getSmoke_cost(){return  smoke_cost;}
    public String getMarry_status(){return  marry_status;}
    public String getBusiness(){return business;}
    public int getSalary(){return salary;}
    public String getTime(){return time;}
    public String getFormattedDate() {return formattedDate;}
    public String getMorning_status(){return morning_status;}
    public String getFamily_status(){return family_status;}
    public String getHabit_reason(){return habit_reason;}
    public String getHabit(){return habit;}
    public String getAware_status(){return aware_status;}
    public String getAware_disease(){return aware_disease;}
    public String getQuit_status(){return quit_status;}
    public String getQuit_reason(){return quit_reason;}
    public String getQuit_before_status(){return quit_before_status;}
    public String getCraving_time(){return craving_time;}
    public String getId(){return id;}
    public String getMessage(){return message;}

    //setter
    public void setId(String val){ this.id=val; }
    public void setMessage(String message){ this.message = message; }


    //parcealable stuff
    public Entry(Parcel in) {
        name = in.readString();
        age = in.readInt();
        sex = in.readString();
        interest = in.readString();
        med_history = in.readString();
        contact = in.readString();
        email=in.readString();
        address=in.readString();

        chewText=in.readString();
        chew_history = in.readInt();     // Entry patient=new Entry(name,age,sex,interest,med_history,contact,days,freq,cost,m_status,future);
        chew_freq=in.readInt();
        chew_cost=in.readFloat();

        smokeText=in.readString();
        smoke_history = in.readInt();     // Entry patient=new Entry(name,age,sex,interest,med_history,contact,days,freq,cost,m_status,future);
        smoke_freq=in.readInt();
        smoke_cost=in.readFloat();
        marry_status = in.readString();
        business=in.readString();
        salary=in.readInt();
        formattedDate=in.readString();
        time=in.readString();
        morning_status=in.readString();
        family_status=in.readString();
        habit_reason=in.readString();
        habit=in.readString();
        aware_status=in.readString();
        aware_disease=in.readString();
        quit_status=in.readString();
        quit_reason=in.readString();
        quit_before_status=in.readString();
        craving_time=in.readString();
        id=in.readString();
        message=in.readString();
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
        parcel.writeString(email);
        parcel.writeString(address);

        parcel.writeString(chewText);
        parcel.writeInt(chew_history);
        parcel.writeInt(chew_freq);
        parcel.writeFloat(chew_cost);


        parcel.writeString(smokeText);
        parcel.writeInt(smoke_history);
        parcel.writeInt(smoke_freq);
        parcel.writeFloat(smoke_cost);

        parcel.writeString(marry_status);
        parcel.writeString(business);
        parcel.writeInt(salary);
        parcel.writeString(formattedDate);
        parcel.writeString(time);
        parcel.writeString(morning_status);
        parcel.writeString(family_status);
        parcel.writeString(habit_reason);
        parcel.writeString(habit);
        parcel.writeString(aware_status);
        parcel.writeString(aware_disease);
        parcel.writeString(quit_status);
        parcel.writeString(quit_reason);
        parcel.writeString(quit_before_status);
        parcel.writeString(craving_time);
        parcel.writeString(id);
        parcel.writeString(message);
    }
}