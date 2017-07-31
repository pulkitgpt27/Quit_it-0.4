package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.ref.Reference;

import static android.R.attr.data;
import static android.R.attr.key;
import static android.R.attr.theme;
import static com.example.android.quitit.R.id.ageView;
import static com.example.android.quitit.R.id.nameView;
import static java.lang.Integer.parseInt;

/**
 * Created by Pulkit on 29-07-2017.
 */

public class UpdateActivity extends AppCompatActivity {

    Entry ClickedEntry;

    private Button mSaveButton;

    private CheckBox med1;
    private CheckBox med2;
    private CheckBox med3;
    private CheckBox med4;
    private CheckBox med5;
    private String med_history="";
    private EditText nameView;
    private EditText ageView;
    private RadioGroup rg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry);


        Bundle B = this.getIntent().getExtras();
        ClickedEntry = B.getParcelable("ClickedEntry");

        //For Name
        nameView=(EditText)findViewById(R.id.name_edit_text);
        nameView.setText(ClickedEntry.getName());

        //For Age
        ageView=(EditText)findViewById(R.id.age_edit_text);
        ageView.setText(""+ClickedEntry.getAge());

        //For Med History
        med_history=ClickedEntry.getMed_history();
        med1=(CheckBox) findViewById(R.id.disease_1);
        med2=(CheckBox) findViewById(R.id.disease_2);
        med3=(CheckBox) findViewById(R.id.disease_3);
        med4=(CheckBox) findViewById(R.id.disease_4);
        med5=(CheckBox) findViewById(R.id.disease_5);

        if(med_history.contains(med1.getText().toString()))
        {
            med1.setChecked(true);
        }

        if(med_history.contains(med2.getText().toString()))
        {
            med2.setChecked(true);
        }

        if(med_history.contains(med3.getText().toString()))
        {
            med3.setChecked(true);
        }

        if(med_history.contains(med4.getText().toString()))
        {
            med4.setChecked(true);
        }
        if(med_history.contains(med5.getText().toString()))
        {
            med5.setChecked(true);
        }

        //For Sex
        rg = (RadioGroup)findViewById(R.id.sex_group);
        String sex = ClickedEntry.getSex();
        if(sex.contains("M")){
            rg.check(R.id.sex_male);
        }
        else if(sex.contains("F")){
            rg.check(R.id.sex_female);
        }
        else {
            rg.check(R.id.sex_other);
        }

        //For Contact No
        EditText contactView=(EditText)findViewById(R.id.contact_edit_text);
        contactView.setText(ClickedEntry.getContact());

        mSaveButton=(Button) findViewById(R.id.save);

        mSaveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                                    //For Sex
                                    rg = (RadioGroup)findViewById(R.id.sex_group);
                                    String s= ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                                    //For Med History
                                    med_history="";
                                    med1=(CheckBox) findViewById(R.id.disease_1);
                                    med2=(CheckBox) findViewById(R.id.disease_2);
                                    med3=(CheckBox) findViewById(R.id.disease_3);
                                    med4=(CheckBox) findViewById(R.id.disease_4);
                                    med5=(CheckBox) findViewById(R.id.disease_5);
                                    if(med1.isChecked())
                                    {
                                        med_history+=med1.getText().toString()+" ";
                                    }
                                    if(med2.isChecked())
                                    {
                                        med_history+=med2.getText().toString()+" ";
                                    }
                                    if(med3.isChecked())
                                    {
                                        med_history+=med3.getText().toString()+" ";
                                    }
                                    if(med4.isChecked())
                                    {
                                        med_history+=med4.getText().toString()+" ";
                                    }
                                    if(med5.isChecked())
                                    {
                                        med_history+=med5.getText().toString()+" ";
                                    }
                                    Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
                                    //Toast.makeText(this, "hello" ,Toast.LENGTH_LONG).show();
                                    DatabaseReference mPatientDatabaseReference=FirebaseDatabase.getInstance().getReference().child("patient").child(ClickedEntry.getId());


                try {
                    Entry patient = new Entry(nameView.getText().toString(), Integer.parseInt(ageView.getText().toString()), s, ClickedEntry.getInterest(), med_history, ClickedEntry.getContact(), ClickedEntry.getChew_history(),
                            ClickedEntry.getChew_freq(), ClickedEntry.getChew_cost(), ClickedEntry.getSmokeHistory(), ClickedEntry.getSmoke_freq(), ClickedEntry.getSmoke_cost(), ClickedEntry.getMarry_status(),
                            ClickedEntry.getBusiness(), ClickedEntry.getSalary(), ClickedEntry.getTime(), ClickedEntry.getFormattedDate(), ClickedEntry.getMorning_status(), ClickedEntry.getFamily_status(),
                            ClickedEntry.getHabit_reason(), ClickedEntry.getHabit(), ClickedEntry.getAware_status(), ClickedEntry.getAware_disease(), ClickedEntry.getQuit_status(), ClickedEntry.getQuit_reason(), ClickedEntry.getQuit_before_status(),
                            ClickedEntry.getCraving_time(), ClickedEntry.getId());

                    mPatientDatabaseReference.setValue(patient);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }

        });



    }
}
