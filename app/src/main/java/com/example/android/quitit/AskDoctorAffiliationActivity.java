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
import com.google.firebase.database.FirebaseDatabase;
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
    private String doctor_id;
    private Patient patient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_doctor_affiliation);
        $doctorEmailId = (EditText) findViewById(R.id.doctor_id_edit_text);
        Button checkDoctor = (Button) findViewById(R.id.check_doctor);
        Button noDoctor = (Button) findViewById(R.id.proceed_without_a_doctor);
        patient = getIntent().getExtras().getParcelable("patient");

        checkDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctor_id = $doctorEmailId.getText().toString();
                if(doctor_id.equals("")){
                    Toast.makeText(getBaseContext(), "Email-address is missing. Please check.",Toast.LENGTH_SHORT);
                }
                else if(!ValidateEntry.validateEmail(doctor_id.toString())) {
                    Toast.makeText(getBaseContext(), "Invalid Email.",Toast.LENGTH_SHORT);
                }
                else {
                    mDoctorsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("doctors");
                    firebaseDataFetch();
                }
            }
        });

        noDoctor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AskDoctorAffiliationActivity.this, NewEntryActivity.class);
                patient.setDoctor_key("no-doctor");
                Bundle b = new Bundle();
                b.putParcelable("patient",(Parcelable) patient);
                i.putExtras(b);
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
                    if (currentDoctor.getMail_id()!=null && currentDoctor.getMail_id().equals(doctor_id)) {
                        doctor_key = child.getKey();
                        doctor_id = currentDoctor.getMail_id();
                        found = true;
                        break;
                    }
                }
                if (found && doctor_key != null) {
                    Intent intent2 = new Intent(AskDoctorAffiliationActivity.this, NewEntryActivity.class);
                    patient.setDoctor_key(doctor_key);
                    Bundle B = new Bundle();
                    B.putParcelable("patient",(Parcelable) patient);
                    intent2.putExtras(B);
                    startActivity(intent2);
                    finish();
                }
                else{
                    Toast.makeText(getBaseContext(),
                            "Doctor not found. \nPlease check the email address", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDoctorsDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
    }
}
