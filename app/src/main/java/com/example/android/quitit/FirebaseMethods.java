package com.example.android.quitit;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Pulkit on 11-08-2017.
 */

public class FirebaseMethods {

    private static FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static void updatePatient(String id,Entry patient)
    {
        DatabaseReference mPatientDatabaseReference= FirebaseDatabase.getInstance().getReference().child("doctors").child(MainActivity.currentdoctorKey).child("patients").child(id);
        try {
            mPatientDatabaseReference.setValue(patient);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getUserId()
    {
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mFirebaseAuth.getCurrentUser();
        if(user!=null)
            return user.getEmail();
        else
            return null;
    }

    public static StorageReference getFirbaseStorageReference(String child){
        return FirebaseStorage.getInstance().getReference().child(child);
    }
    public static DatabaseReference getFirebaseReference(String fchild)
    {
        FirebaseDatabase mFirebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference childref=mFirebaseDatabase.getReference().child(fchild);
        return childref;
    }
}
