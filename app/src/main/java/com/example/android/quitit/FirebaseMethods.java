package com.example.android.quitit;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.android.quitit.R.id.nameView;

/**
 * Created by Pulkit on 11-08-2017.
 */

public class FirebaseMethods {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static void updatePatient(String id,Entry patient)
    {
        DatabaseReference mPatientDatabaseReference= FirebaseDatabase.getInstance().getReference().child("patient").child(id);
        try {
            mPatientDatabaseReference.setValue(patient);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getUserId()
    {
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mFirebaseAuth.getCurrentUser();
        return user.getEmail();
    }

    public static DatabaseReference getFirebaseReference(String fchild)
    {
        FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference childref=mFirebaseDatabase.getReference().child("doctors").child("doctor1").child(fchild);
        return childref;
    }
}
