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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by Pulkit on 07-08-2017.
 */

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mUsername;
    public static final String ANONYMOUS = "anonymous";

    public static final int RC_SIGN_IN = 2;
    private GoogleApiClient mGoogleApiClient;
    private String displayName,displayEmail;
    private String imageUri;
    public static boolean isGmailSigned = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    toastmessage("Successfylly signed in with mail:"+ user.getEmail());
                    displayName = user.getDisplayName();
                    displayEmail = user.getEmail();
                    if(user.getPhotoUrl() != null)
                        imageUri = user.getPhotoUrl().toString();


                    onSignedInInitialize(user.getDisplayName());
                    displayName = user.getDisplayName();
                    displayEmail = user.getEmail();
                    onSignedInInitialize(user.getDisplayName());



                }else {
                    onSignedOutCleanUp();
                }
            }
        };
        final EditText email =(EditText) findViewById(R.id.input_email);
        final EditText password =(EditText) findViewById(R.id.input_password);
        final TextView guestentry = (TextView) findViewById(R.id.guestentry);
        guestentry.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,NewEntryActivity.class);
                startActivity(intent);
            }
        });
        Button login = (Button) findViewById(R.id.btn_login);

        SignInButton googleButton = (SignInButton) findViewById(R.id.google_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(LoginActivity.this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        toastmessage("you got an error");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();




        googleButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                signIn();

            }
        });
        login.setOnClickListener(new Button.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         String mail = email.getText().toString();
                                         String pass = password.getText().toString();
                                         if(mail.equals("") || pass.equals("")){
                                             toastmessage("You didnt filled all the feilds");
                                         }
                                         else
                                         {
                                             mFirebaseAuth.signInWithEmailAndPassword(mail, pass)
                                                     .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<AuthResult> task) {
                                                             // If sign in fails, display a message to the user. If sign in succeeds
                                                             // the auth state listener will be notified and logic to handle the
                                                             // signed in user can be handled in the listener.
                                                             if (!task.isSuccessful()) {
                                                                 // there was an error
                                                                 toastmessage("Credentials are wrong");
                                                             } else {

                                                                 Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                 startActivity(intent);
                                                                 finish();
                                                             }
                                                         }
                                                     });
                                         }


                                     }
                                 }
        );
    }

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                isGmailSigned = true;
                firebaseAuthWithGoogle(account);
            } else {
                toastmessage("Google Sign in Failed");
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            onSignedInInitialize(user.getDisplayName());
                        } else {
                            // If sign in fails, display a message to the user.
                            toastmessage("Sign In Failed");
                        }
                        // ...
                    }
                });
    }

    private void toastmessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener!=null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void onSignedInInitialize(String username){
        mUsername=username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanUp(){
        mUsername=ANONYMOUS;
        //FirebaseAuth.getInstance().signOut();
        detachchDatabaseReadListener();
    }

    private void attachDatabaseReadListener(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("displayName",displayName);
        intent.putExtra("displayEmail",displayEmail);
        intent.putExtra("displayImage",imageUri);
        startActivity(intent);
        finish();
    }

    private void detachchDatabaseReadListener(){

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
