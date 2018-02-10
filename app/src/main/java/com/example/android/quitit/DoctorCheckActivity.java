package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pulkit gupta on 27/01/2018.
 */

public class DoctorCheckActivity extends AppCompatActivity {
    private String doctor_id = "";
    private DatabaseReference mDoctorsDatabaseReference;
    private ValueEventListener mValueEventListener;
    private EditText $doctorEmailId;
    private boolean found;
    private String checked_doctor_key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_doctor);
        $doctorEmailId = (EditText) findViewById(R.id.doctor_id_edit_text);
        doctor_id = $doctorEmailId.getText().toString();
        Button check = (Button) findViewById(R.id.check_doctor);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDoctorsDatabaseReference = FirebaseMethods.getFirebaseReference("doctors");
                firebaseDataFetch();
                if (found && checked_doctor_key != null) {
                    Intent intent2 = new Intent(DoctorCheckActivity.this, NewEntryActivity.class);
                    intent2.putExtra("doc_key",checked_doctor_key);
                    startActivity(intent2);
                    finish();
                } else{
                    Toast.makeText(getBaseContext(),
                            "Doctor not found. \nPlease check the email address", Toast.LENGTH_LONG);
                }
            }
        });
    }

    public void firebaseDataFetch() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                found = false;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Doctor currentDoctor = child.getValue(Doctor.class);
                    if (currentDoctor.getMail_id()!=null && currentDoctor.getMail_id().equals(doctor_id)) {
                        checked_doctor_key = child.getKey();
                        found = true;
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