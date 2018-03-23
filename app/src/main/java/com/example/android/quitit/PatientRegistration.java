package com.example.android.quitit;

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

import java.util.HashMap;
import java.util.Set;

import static com.example.android.quitit.FirebaseMethods.getUserId;

/**
 * Created by Ayush vaid on 23-03-2018.
 */

public class PatientRegistration extends AppCompatActivity{

    public String doctor_key;
    public String doctor_email;
    private EditText patientEmailEdt;
    private EditText patient_uname;
    private EditText patient_pword;
    private EditText patient_pwordconf;
    private Button checkPatientBtn;
    private DatabaseReference mDoctorsDatabaseReference;
    private ValueEventListener mValueEventListener;
    public Entry CurrentPatient;
    boolean found;
    private String entry_key;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        doctor_key = getIntent().getStringExtra("doc_key");
        doctor_email = getIntent().getStringExtra("doc_id");
        //mDoctorsDatabaseReference =  FirebaseDatabase.getInstance().getReference().child("doctors").child(doctor_key).child("patients");
        patientEmailEdt = (EditText)findViewById(R.id.patient_id_edit_text);
        patient_uname = (EditText) findViewById(R.id.patient_username);
        patient_pword = (EditText) findViewById(R.id.patient_password);
        patient_pwordconf = (EditText) findViewById(R.id.patient_password_confirm);


        //CREATE PATIENT HERE WITHOUT THE ENTRY_REFERENCE

        checkPatientBtn = (Button)findViewById(R.id.check_patient);

        if(!doctor_key.equals("no-doctor"))
            firebaseDataFetch();
        checkPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(patientEmailEdt.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(), "Email-address is missing.",Toast.LENGTH_SHORT);
                else if(patient_uname.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(), "Please enter username.", Toast.LENGTH_SHORT);
                else if(patient_pword.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(), "Please enter password." ,Toast.LENGTH_SHORT);
                else if(!patient_pword.getText().toString().equals(patient_pwordconf.getText().toString()))
                    Toast.makeText(getBaseContext(), "Passwords do not match.",Toast.LENGTH_SHORT);
                else {
                    if(found == true){
                        //SET THE THE VALUE OF ENTRY_KEY in PATIENT OBJECT
                        //INTENT TO PATIENT-HOME-SCREEN
                    }
                    else{
                        //INTENT TO NEW-ENTRY-ACTIVITY WITH PATIENT-OBJECT IN EXTRAS
                    }
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
                    if (currentDoctor.getMail_id() != null) {
                        HashMap<String, Entry> patients = currentDoctor.getPatients();
                        if(currentDoctor.getMail_id().equals(getUserId())) {
                            found = true;
                            if (patients != null) {
                                Set<String> ks = patients.keySet();
                                for (String key : ks) {
                                    Entry e = patients.get(key);
                                    if(e.getEmail().equals(patientEmailEdt.getText().toString())) {
                                        CurrentPatient = e;
                                        entry_key = key;
                                        found = true;
                                    }
                                }
                            }
                            else{
                                Toast.makeText(getBaseContext(),"No Record could be found with this ",Toast.LENGTH_SHORT);
                                found = false;
                            }
                        }
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