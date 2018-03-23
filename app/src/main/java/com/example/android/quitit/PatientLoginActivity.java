package com.example.android.quitit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ayush vaid on 23-03-2018.
 */

public class PatientLoginActivity extends AppCompatActivity {

    private String username;
    private String password;
    private DatabaseReference mPatientDatabaseReference;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);
        EditText unameEdt = (EditText) findViewById(R.id.username);
        EditText pwordEdt = (EditText) findViewById(R.id.password);

        username = unameEdt.getText().toString();
        password = pwordEdt.getText().toString();

        mPatientDatabaseReference = FirebaseMethods.getFirebaseReference("patient");
        if(mPatientDatabaseReference == null)
            Toast.makeText(getBaseContext(),"Patients are null. Please register.",Toast.LENGTH_SHORT);
        else {
            firebaseDataFetch();

        }
    }

    public void firebaseDataFetch() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //HERE CHECK PATIENT
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mPatientDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
    }
}
