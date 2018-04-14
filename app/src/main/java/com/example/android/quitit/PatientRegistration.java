package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Ayush vaid on 23-03-2018.
 */

public class PatientRegistration extends AppCompatActivity {

    public String doctor_key;
    public String doctor_email;
    private EditText patientEmailEdt;
    private EditText patient_uname;
    private EditText patient_pword;
    private EditText patient_pwordconf;
    private ProgressBar spinner;
    private TextView spinner_text;
    private LinearLayout spinner_layout;
    private LinearLayout base_layout;
    private Button checkPatientBtn;
    private DatabaseReference mPatientsDatabaseReference;
    private DatabaseReference mDoctorsDatabaseReference;
    private ValueEventListener mValueEventListener;
    public Entry CurrentPatient;
    private boolean found;
    private String entry_key;
    private Patient patient;

    protected FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        //doctor_key = getIntent().getStringExtra("doc_key");
        //doctor_email = getIntent().getStringExtra("doc_id");
        //mDoctorsDatabaseReference =  FirebaseDatabase.getInstance().getReference().child("doctors").child(doctor_key).child("patients");
        patientEmailEdt = (EditText) findViewById(R.id.patient_id_edit_text);
        patient_uname = (EditText) findViewById(R.id.patient_username);
        patient_pword = (EditText) findViewById(R.id.patient_password);
        patient_pwordconf = (EditText) findViewById(R.id.patient_password_confirm);
        base_layout = (LinearLayout) findViewById(R.id.base_layout);

        spinner = (ProgressBar) findViewById(R.id.spinner);
        spinner_layout = (LinearLayout) findViewById(R.id.spinner_layout);
        spinner_text = (TextView) findViewById(R.id.spinner_text);
        spinner.setVisibility(View.GONE);
        spinner_text.setVisibility(View.GONE);
        spinner_layout.setVisibility(View.GONE);

        patient = new Patient();
        mAuth = FirebaseAuth.getInstance();
        mPatientsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("patients");
        mDoctorsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("doctors");

        //CREATE PATIENT HERE WITHOUT THE ENTRY_REFERENCE
        final String[] uniqueKey = new String[1];

        checkPatientBtn = (Button) findViewById(R.id.check_patient);
        checkPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (patientEmailEdt.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(), "Email-address is missing.", Toast.LENGTH_SHORT);
                else if (patient_uname.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(), "Please enter username.", Toast.LENGTH_SHORT);
                else if (patient_pword.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(), "Please enter password.", Toast.LENGTH_SHORT);
                else if (!patient_pword.getText().toString().equals(patient_pwordconf.getText().toString()))
                    Toast.makeText(getBaseContext(), "Passwords do not match.", Toast.LENGTH_SHORT);
                else {
                    startSpinner();
                    firebasedatafetch();
                    patient.setUsername(patient_uname.getText().toString());
                    patient.setEmailId(patientEmailEdt.getText().toString());
                    patient.setPassword(patient_pword.getText().toString());
                    if (found == true) {
                        //SET THE THE VALUE OF ENTRY_KEY in PATIENT OBJECT
                        //INTENT TO PATIENT-HOME-SCREEN
                        patient.setEntry_key(entry_key);
                        patient.setDoctor_key(doctor_key);
                        mPatientsDatabaseReference.push().setValue(null, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   DatabaseReference databaseReference) {
                                uniqueKey[0] = databaseReference.getKey();
                                final DatabaseReference UpdatePatientUserDB = FirebaseDatabase.getInstance().getReference().child("patients").child(uniqueKey[0]);
                                UpdatePatientUserDB.setValue(patient);
                                registerUser(patient.getEmailId(),patient.getPassword());
                                endSpinner();
                            }
                        });
                        Log.e("Entryfound","");
                        Intent i = new Intent(PatientRegistration.this, MainActivity.class);
                        Bundle B1 = new Bundle();
                        B1.putParcelable("patient_with_entry", (Parcelable) patient);
                        i.putExtras(B1);
                        startActivity(i);
                        finish();
                    } else {
                        //INTENT TO NEW-ENTRY-ACTIVITY WITH PATIENT-OBJECT IN EXTRAS
                        endSpinner();
                        Log.e("notfoundentry","");
                        Intent i = new Intent(PatientRegistration.this, AskDoctorAffiliationActivity.class);
                        Bundle B1 = new Bundle();
                        registerUser(patient.getEmailId(), patient.getPassword());
                        B1.putParcelable("new_patient", (Parcelable) patient);
                        i.putExtras(B1);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });
    }

    protected void registerUser(String email, String password){

        //VALIDATIONS IDHAR DAALNI HAIN

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createPatientUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(PatientRegistration.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void startSpinner(){
        patientEmailEdt.setVisibility(View.GONE);
        patient_pword.setVisibility(View.GONE);
        patient_uname.setVisibility(View.GONE);
        patient_pwordconf.setVisibility(View.GONE);

        spinner_text.setVisibility(View.VISIBLE);
        spinner_layout.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
    }

    private void endSpinner(){
        patientEmailEdt.setVisibility(View.VISIBLE);
        patient_pword.setVisibility(View.VISIBLE);
        patient_uname.setVisibility(View.VISIBLE);
        patient_pwordconf.setVisibility(View.VISIBLE);

        spinner_text.setVisibility(View.GONE);
        spinner_layout.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
    }

    private void firebasedatafetch(){
        mValueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                found = false;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Doctor currentDoctor = child.getValue(Doctor.class);
                    if (currentDoctor.getMail_id() != null) {
                        HashMap<String, Entry> patients = currentDoctor.getPatients();
                        if (patients != null) {
                            Set<String> ks = patients.keySet();
                            for (String key : ks) {
                                Entry e = patients.get(key);
                                if (e.getEmail().equals(patientEmailEdt.getText().toString())) {
                                    CurrentPatient = e;
                                    entry_key = key;
                                    found = true;
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDoctorsDatabaseReference.addValueEventListener(mValueEventListener);
    }
}
