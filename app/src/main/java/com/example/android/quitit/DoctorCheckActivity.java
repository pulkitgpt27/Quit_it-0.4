package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Set;

import static com.example.android.quitit.FirebaseMethods.getUserId;
import static com.example.android.quitit.R.id.spinner;

/**
 * Created by pulkit gupta on 27/01/2018.
 */

public class DoctorCheckActivity extends AppCompatActivity {
    String doctor_id="";
    private DatabaseReference mDoctorsDatabaseReference;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry);
        EditText doctor_id_edit_text=(EditText) findViewById(R.id.doctor_id_edit_text);
        doctor_id= String.valueOf(doctor_id_edit_text.getText());
        Button check=(Button) findViewById(R.id.check_button);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDoctorsDatabaseReference=FirebaseMethods.getFirebaseReference("doctors");

                firebaseDataFetch();
                if(MainActivity.currentdoctorKey!=null)
                {
                    Intent intent2 = new Intent(DoctorCheckActivity.this,NewEntryActivity.class);
                    startActivity(intent2);
                    finish();
                }
            }
        });
    }

    public void firebaseDataFetch()
    {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Doctor currentDoctor = child.getValue(Doctor.class);
                    if (currentDoctor.getMail_id().equals(doctor_id)) {
                        MainActivity.currentdoctorKey = child.getKey();
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDoctorsDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
        //mPatientDatabaseReference.addChildEventListener(mChildEventListener);
    }

}

