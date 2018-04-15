package com.example.android.quitit;

/**
 * Created by pulkit gupta on 23/03/2018.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class NotificationActivity extends Activity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        mFirebaseAuth= FirebaseAuth.getInstance();
                FirebaseUser user=mFirebaseAuth.getCurrentUser();
                if(user!=null){
                    //For dialogue box
                            AlertDialog.Builder mbuilder = new AlertDialog.Builder(NotificationActivity.this);

                            final View mview=getLayoutInflater().inflate(R.layout.update_dialogue,null);
                            TextView tv=(TextView) mview.findViewById(R.id.dialogue_textView);
                            tv.setText("Daily Updates");
                            final EditText updated_val=(EditText) mview.findViewById(R.id.dialogue_editText);
                            Button save=(Button) mview.findViewById(R.id.dialogue_save);
                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(!updated_val.getText().toString().isEmpty())
                                    {
                                        Toast.makeText(NotificationActivity.this,"Value Updated SuccessFully",Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(NotificationActivity.this,"Please Fill Some Value",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                            mbuilder.setView(mview);
                            AlertDialog dialog=mbuilder.create();
                            dialog.show();
                            //dialogue end


                }else {
                    Toast.makeText(NotificationActivity.this,"Doesn't Exist Already",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NotificationActivity.this, PatientLoginActivity.class);
                    startActivity(intent);

                    //Some Intent
                }


    }
}
