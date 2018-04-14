package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
 * Created by Ayush vaid on 23-03-2018.
 */

public class AskDoctorAffiliationActivity extends AppCompatActivity {

    private DatabaseReference mDoctorsDatabaseReference;
    private ValueEventListener mValueEventListener;
    private EditText $doctorEmailId;
    private boolean found;
    private String doctor_key;
    private String doctor_email;
    private Patient patient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_doctor_affiliation);
        $doctorEmailId = (EditText) findViewById(R.id.doctor_id_edit_text);
        doctor_email = $doctorEmailId.getText().toString();
        Button checkDoctor = (Button) findViewById(R.id.check_doctor);
        Button noDoctor = (Button) findViewById(R.id.proceed_without_a_doctor);
        Bundle B = this.getIntent().getExtras();
        patient = B.getParcelable("new_patient");

        checkDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(doctor_email.equals("")){
                    Toast.makeText(getBaseContext(), "Email-address is missing. Please check.",Toast.LENGTH_SHORT);
                }
                else {
                    mDoctorsDatabaseReference = FirebaseMethods.getFirebaseReference("doctors");
                    firebaseDataFetch();
                    if (found && doctor_key != null) {
                        Toast.makeText(getBaseContext(),
                                "Doctor found.", Toast.LENGTH_LONG);
                        patient.setDoctor_key(doctor_key);
                        Intent intent2 = new Intent(AskDoctorAffiliationActivity.this, NewEntryActivity.class);
                        //intent2.putExtra("doc_key", doctor_key);
                        //intent2.putExtra("doc_email",doctor_email);
                        Bundle B1 = new Bundle();
                        B1.putParcelable("new_patient", (Parcelable) patient);
                        intent2.putExtras(B1);
                        startActivity(intent2);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(),
                                "Doctor not found. \nPlease check the email address", Toast.LENGTH_LONG);
                    }
                }
            }
        });

        noDoctor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AskDoctorAffiliationActivity.this, NewEntryActivity.class);
                patient.setDoctor_key("no-doctor");
                //i.putExtra("doc_key", "no-doctor");
                Bundle B1 = new Bundle();
                B1.putParcelable("new_patient", (Parcelable) patient);
                i.putExtras(B1);
                startActivity(i);
                finish();
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
                    if (currentDoctor.getMail_id()!=null && currentDoctor.getMail_id().equals(doctor_email)) {
                        doctor_key = child.getKey();
                        doctor_email = currentDoctor.getMail_id();
                        found = true;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDoctorsDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
    }
}
