package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText patient_email;
    private EditText patient_uname;
    private EditText patient_pword;
    private EditText patient_pwordconf;
    private TextInputLayout $email_layout;
    private TextInputLayout $username_layout;
    private TextInputLayout $password_layout;
    private TextInputLayout $password_confirm_layout;

    private Button checkPatientBtn;
    private DatabaseReference mPatientsDatabaseReference;
    private DatabaseReference mDoctorsDatabaseReference;
    private ValueEventListener mValueEventListener;
    private boolean found;
    private String entry_key;
    private Patient patient;
    protected FirebaseAuth mAuth;
    public HashMap<String,Boolean> validation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        //doctor_key = getIntent().getStringExtra("doc_key");
        //doctor_email = getIntent().getStringExtra("doc_id");
        //mDoctorsDatabaseReference =  FirebaseDatabase.getInstance().getReference().child("doctors").child(doctor_key).child("patients");
        mAuth = FirebaseAuth.getInstance();

        validation = new HashMap<String,Boolean>();
        validation.put("email",false);
        validation.put("username",false);
        validation.put("password",false);
        validation.put("password_confirm",false);

        patient_email = (EditText) findViewById(R.id.patient_id_edit_text);
        patient_uname = (EditText) findViewById(R.id.patient_username);
        patient_pword = (EditText) findViewById(R.id.patient_password);
        patient_pwordconf = (EditText) findViewById(R.id.patient_password_confirm);

        $email_layout = (TextInputLayout) findViewById(R.id.email_layout);
        $username_layout = (TextInputLayout) findViewById(R.id.username_layout);
        $password_layout = (TextInputLayout) findViewById(R.id.password_layout);
        $password_confirm_layout = (TextInputLayout) findViewById(R.id.password_confirm_layout);

        patient_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(!ValidateEntry.validateEmpty(patient_email.getText().toString())){
                        $email_layout.setErrorEnabled(true);
                        $email_layout.setError("Email can not be empty.");
                        validation.put("email",false);
                    }
                    else if(!ValidateEntry.validateEmail(patient_email.getText().toString())){
                        $email_layout.setErrorEnabled(true);
                        $email_layout.setError("Email is invalid.");
                        validation.put("email",false);
                    }
                    else{
                        $email_layout.setError("");
                        $email_layout.setErrorEnabled(false);
                        validation.put("email",true);
                    }
                }
            }
        });

        patient_uname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(!ValidateEntry.validateEmpty(patient_uname.getText().toString())){
                        $username_layout.setErrorEnabled(true);
                        $username_layout.setError("Username can not be empty.");
                        validation.put("username",false);
                    }
                    else{
                        $username_layout.setError("");
                        $username_layout.setErrorEnabled(false);
                        validation.put("username",true);
                    }
                }
            }
        });

        patient_pword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(!ValidateEntry.validateEmpty(patient_pword.getText().toString())){
                        $password_layout.setErrorEnabled(true);
                        $password_layout.setError("Password can not be empty.");
                        validation.put("password",false);
                    }
                    else if(!ValidateEntry.validatePasswordLength(patient_pword.getText().toString())){
                        $password_layout.setErrorEnabled(true);
                        $password_layout.setError("Password length : Insufficient.");
                        validation.put("password",false);
                    }
                    else{
                        $password_layout.setError("");
                        $password_layout.setErrorEnabled(false);
                        validation.put("password",true);
                    }
                }
            }
        });

        patient_pwordconf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(!ValidateEntry.validateEmpty(patient_pword.getText().toString())){
                        $password_confirm_layout.setErrorEnabled(true);
                        $password_confirm_layout.setError("This field can not be empty.");
                        validation.put("password_confirm",false);
                    }
                    else if(!ValidateEntry.validateConfirmPassword(patient_pword.getText().toString(),patient_pwordconf.getText().toString())){
                        $password_confirm_layout.setErrorEnabled(true);
                        $password_confirm_layout.setError("Passwords do not match");
                        validation.put("password_confrim",false);
                    }
                    else{
                        $password_confirm_layout.setError("");
                        $password_confirm_layout.setErrorEnabled(false);
                        validation.put("password_confirm",true);
                    }
                }
            }
        });

        final String uname = patient_uname.getText().toString();
        final String pword = patient_pword.getText().toString();
        patient = new Patient();
        mPatientsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("patients");
        mDoctorsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors");

        //CREATE PATIENT HERE WITHOUT THE ENTRY_REFERENCE

        checkPatientBtn = (Button) findViewById(R.id.check_patient);
        checkPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient_email.clearFocus();
                patient_uname.clearFocus();
                patient_pword.clearFocus();
                patient_pwordconf.clearFocus();
                if (!validation.get("email"))
                    Toast.makeText(getBaseContext(), "Email-address error", Toast.LENGTH_SHORT);
                else if (!validation.get("username"))
                    Toast.makeText(getBaseContext(), "Username error.", Toast.LENGTH_SHORT);
                else if (!validation.get("password"))
                    Toast.makeText(getBaseContext(), "Password error.", Toast.LENGTH_SHORT);
                else if (!validation.get("passowrd_confirm"))
                    Toast.makeText(getBaseContext(), "Passwords do not match.", Toast.LENGTH_SHORT);
                else {
                    firebasedatafetch();
                    patient.setUsername(patient_uname.getText().toString());
                    patient.setEmailId(patient_email.getText().toString());
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
                                final DatabaseReference UpdatePatientDatabaseReference = FirebaseDatabase.getInstance().getReference().child("patients");
                                UpdatePatientDatabaseReference.setValue(patient);
                            }
                        });
                        startActivity((new Intent(PatientRegistration.this, MainActivity.class)));
                        finish();
                    } else {
                        registerUser(uname, pword);
                        startActivity((new Intent(PatientRegistration.this, AskDoctorAffiliationActivity.class)));
                        finish();
                    }
                }
            }
        });
    }

    protected void registerUser(String username, String password){

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(PatientRegistration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d("", "createPatientUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(PatientRegistration.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                                if (e.getEmail().equals(patient_email.getText().toString())) {
                                    //CurrentPatient = e;
                                    doctor_key = child.getKey();
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
