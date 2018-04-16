package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    private boolean found;
    private Patient CurrentPatient;
    private EditText unameEdt;
    private EditText pwordEdt;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mUsername;
    public static final String ANONYMOUS = "anonymous";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        unameEdt = (EditText) findViewById(R.id.username);
        pwordEdt = (EditText) findViewById(R.id.password);
        TextView pregister = (TextView) findViewById(R.id.patient_register);
        Button loginButton = (Button) findViewById(R.id.btn_login);

        mFirebaseAuth=FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = unameEdt.getText().toString();
                password = pwordEdt.getText().toString();
                mPatientDatabaseReference = FirebaseMethods.getFirebaseReference("patients");
                if(mPatientDatabaseReference == null)
                    Toast.makeText(getBaseContext(),"Patients are null. Please register.",Toast.LENGTH_SHORT);
                else {
                    if(username.equals("") || password.equals("")){
                        toastMessage("You didnt filled all the feilds");
                    }
                    else {
                        mFirebaseAuth.signInWithEmailAndPassword(username, password)
                                .addOnCompleteListener(PatientLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            // there was an error
                                            toastMessage(task.getException().getMessage());
                                        } else {
                                            firebaseDataFetch();

                                        }
                                    }
                                });
                    }
                }
            }
        });

        pregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientLoginActivity.this,PatientRegistration.class));
                finish();
            }
        });
    }

    public void firebaseDataFetch() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //HERE CHECK PATIENT
                    Patient currentPatient = child.getValue(Patient.class);
                    if (currentPatient.getUsername().equals(username) && currentPatient.getPassword().equals(password)) {
                        CurrentPatient = currentPatient;
                        String displayImage = null;
                        Toast.makeText(PatientLoginActivity.this, "Successful Login!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PatientLoginActivity.this,MainActivity.class);
                        i.putExtra("displayName",CurrentPatient.getUsername());
                        i.putExtra("displayEmail",CurrentPatient.getEmailId());
                        i.putExtra("displayImage",displayImage);
                        i.putExtra("CurrentPatient",CurrentPatient);
                        startActivity(i);
                        finish();
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mPatientDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
    }

    private void toastMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
