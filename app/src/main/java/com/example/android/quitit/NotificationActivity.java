package com.example.android.quitit;

/**
 * Created by pulkit gupta on 23/03/2018.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class NotificationActivity extends Activity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser user=null;
    Patient current_patient;
    Entry current_entry;
    EditText updated_val_smoke=null;
    EditText updated_val_chew = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        current_entry = this.getIntent().getParcelableExtra("entry");
                mFirebaseAuth=FirebaseAuth.getInstance();
                user=mFirebaseAuth.getCurrentUser();
                if(current_entry!=null){
                    //For dialogue box

                            AlertDialog.Builder mbuilder = new AlertDialog.Builder(NotificationActivity.this);

                            final View mview=getLayoutInflater().inflate(R.layout.update_dialogue,null);
                            TextView tv=(TextView) mview.findViewById(R.id.dialogue_textView);
                            updated_val_smoke=(EditText) mview.findViewById(R.id.dialogue_smoke_edt);
                            updated_val_chew=(EditText) mview.findViewById(R.id.dialogue_chew_edt);
                            updated_val_chew.setVisibility(View.GONE);
                            updated_val_smoke.setVisibility(View.GONE);
                            tv.setText("Daily Updates");
                            if(!current_entry.getSmokeText().equals("")) {
                                updated_val_smoke.setVisibility(View.VISIBLE);
                            }

                            if(!current_entry.getChewText().equals("")) {
                                updated_val_chew.setVisibility(View.VISIBLE);
                            }

                            Button save=(Button) mview.findViewById(R.id.dialogue_save);

                    mbuilder.setView(mview);
                    final AlertDialog dialog=mbuilder.create();
                    dialog.show();
                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if((updated_val_smoke!=null && !updated_val_smoke.getText().toString().isEmpty()) || (updated_val_chew!=null && !updated_val_chew.getText().toString().isEmpty()))
                                    {
                                        final String key=user.getUid();
                                        FirebaseDatabase.getInstance().getReference().child("patients").child(key).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                current_patient = dataSnapshot.getValue(Patient.class);
                                                if(current_patient!=null)
                                                {

                                                    Date cDate = new Date();
                                                    String fDate = new SimpleDateFormat("dd-MM-yyyy").format(cDate);
                                                    if(updated_val_smoke.getVisibility()==View.VISIBLE && !updated_val_smoke.getText().equals(""))
                                                        current_patient.getDay_map_smoke().put(fDate,Integer.parseInt(String.valueOf(updated_val_smoke.getText())));
                                                    if(updated_val_chew.getVisibility()==View.VISIBLE && !updated_val_chew.getText().equals(""))
                                                        current_patient.getDay_map_chew().put(fDate,Integer.parseInt(String.valueOf(updated_val_chew.getText())));

                                                    FirebaseMethods.updatePatient2(key,current_patient);
                                                    Toast.makeText(NotificationActivity.this,"Value Updated SuccessFully",Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                                                    intent.putExtra("displayName",current_patient.getUsername());
                                                    intent.putExtra("displayEmail",current_patient.getEmailId());
                                                    String displayImage = null;
                                                    intent.putExtra("displayImage",displayImage);
                                                    intent.putExtra("CurrentPatient",current_patient);
                                                    startActivity(intent);
                                                    finish();
                                                    return;

                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    }else
                                    {
                                        Toast.makeText(NotificationActivity.this,"Please Fill Some Value",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                            //dialogue end


                }else {
                    Toast.makeText(NotificationActivity.this,"Doesn't Exist Already",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NotificationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    //Some Intent
                }


    }
}
