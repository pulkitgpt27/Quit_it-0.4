package com.example.android.quitit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
            FirebaseDatabase.getInstance().getReference().child("patients").child(key).addValueEventListener(new ValueEventListener() {
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

}