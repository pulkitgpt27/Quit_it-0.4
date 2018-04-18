package com.example.android.quitit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by pulkit gupta on 23/03/2018.
 */

public class AlarmReceiver extends BroadcastReceiver{
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser user=null;
    Patient current_patient;
    Entry current_entry;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //what happens when we click on notification
        mFirebaseAuth=FirebaseAuth.getInstance();
        user=mFirebaseAuth.getCurrentUser();
        if(user==null)
            return;
        final String key=user.getUid();
        //for getting patient
        if(user!=null) {

            FirebaseDatabase.getInstance().getReference().child("patients").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    current_patient = dataSnapshot.getValue(Patient.class);
                    //condition to be checked else return
                    if (current_patient != null && !current_patient.getDoctor_key().equals(" ") && !current_patient.getEntry_key().equals(" ")) {
                        //For getting entry
                        FirebaseDatabase.getInstance().getReference().child("doctors").child(current_patient.getDoctor_key()).child("patients").child(current_patient.getEntry_key()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                current_entry = dataSnapshot.getValue(Entry.class);
                                //for transfer of data
                                if(isFirstDayofMonth(Calendar.getInstance()))
                                {
                                    Calendar cal=Calendar.getInstance();
                                    SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                                    cal.add(Calendar.MONTH,-1);
                                    String month_name = month_date.format(cal.getTime());

                                    Patient temp=new Patient();
                                    if(!current_entry.getSmokeText().equals(""))
                                        temp.setDay_map_smoke(current_patient.getDay_map_smoke());
                                    if(!current_entry.getChewText().equals(""))
                                        temp.setDay_map_chew(current_patient.getDay_map_chew());
                                    final DatabaseReference mPatientdatabaseReference = FirebaseDatabase.getInstance().getReference().child("patients").child(key);

                                    mPatientdatabaseReference.child("monthlydata").child(month_name).setValue(temp, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if(!current_entry.getSmokeText().equals(""))
                                                mPatientdatabaseReference.child("day_map_smoke").setValue(null, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                    }
                                                });
                                            if(!current_entry.getChewText().equals(""))
                                                mPatientdatabaseReference.child("day_map_chew").setValue(null, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                    }
                                                });
                                        }
                                    });



                                }
                                //end
                                Intent notificationIntent = new Intent(context, NotificationActivity.class);
                                notificationIntent.putExtra("entry", current_entry);

                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                                stackBuilder.addParentStack(NotificationActivity.class);
                                stackBuilder.addNextIntent(notificationIntent);

                                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                                Notification.Builder builder = new Notification.Builder(context);

                                //All notification part
                                Notification notification = builder.setContentTitle("Quit It")//title
                                        .setContentText("Give us your daily status")//text
                                        .setTicker("New Message Alert!")
                                        .setSmallIcon(R.mipmap.icon)
                                        .setAutoCancel(true)
                                        .setContentIntent(pendingIntent).build();

                                //to display notification
                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(0, notification);
                                return;
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            return;
        }

    }

    public boolean isFirstDayofMonth(Calendar calender){

        if(calender == null)
            return false;
        int dayOfMonth = calender.get(Calendar.DAY_OF_MONTH);
        return (dayOfMonth == 1);
    }

}