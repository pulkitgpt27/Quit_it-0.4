package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.android.quitit.R.id.update;

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
    private CheckBox start1;
    private CheckBox start2;
    private CheckBox start3;
    private CheckBox start4;
    private String med_history="";
    private EditText nameView;
    private RadioGroup rg;
    private EditText contactView;
    private EditText emailView;
    private EditText addressView;
    private EditText businessView;
    private RadioGroup rg1;
    private String m_status="";
    private EditText salaryView;
    private String startConsumingReason;
    private CheckBox otherHabit1;
    private CheckBox otherHabit2;
    private String otherHabit;
    private String tobaccoConsumptionDiseases;
    private CheckBox awareDisease1;
    private CheckBox awareDisease2;
    private CheckBox awareDisease3;
    private CheckBox awareDisease4;
    private CheckBox awareDisease5;
    private CheckBox awareDisease6;
    private CheckBox awareDisease7;
    private CheckBox awareDisease8;
    private CheckBox awareDisease9;
    private CheckBox awareDisease10;
    private String reasonForQuitting;
    private CheckBox reasonQuitting1;
    private CheckBox reasonQuitting2;
    private CheckBox reasonQuitting3;
    private CheckBox reasonQuitting4;
    private String cravingTime;
    private CheckBox craving1;
    private CheckBox craving2;
    private CheckBox craving3;
    private CheckBox craving4;
    private CheckBox craving5;
    private CheckBox craving6;
    private CheckBox craving7;
    private CheckBox craving8;
    private CheckBox craving9;
    private RadioButton m_married;
    private RadioButton m_unmarried;
    private RadioButton m_soon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_view);


        Bundle B = this.getIntent().getExtras();
        ClickedEntry = B.getParcelable("ClickedEntry");


        //For Name
        nameView=(EditText)findViewById(R.id.name_edit_text);
        nameView.setText(ClickedEntry.getName(), TextView.BufferType.EDITABLE);

        //For contact
        contactView=(EditText)findViewById(R.id.contact_edit_text);
        contactView.setText(ClickedEntry.getContact(),TextView.BufferType.EDITABLE);

        //For email
        emailView = (EditText) findViewById(R.id.email_edit_text);
        emailView.setText(ClickedEntry.getEmail(),TextView.BufferType.EDITABLE);

        //For Address
        addressView = (EditText) findViewById(R.id.address_edit_text);
        addressView.setText(ClickedEntry.getAddress(),TextView.BufferType.EDITABLE);

        //For salary
        salaryView = (EditText) findViewById(R.id.salary_edit_text);
        salaryView.setText(String.valueOf(ClickedEntry.getSalary()),TextView.BufferType.EDITABLE);

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



        //For marital status
        rg1 = (RadioGroup)findViewById(R.id.m_status_group);
        m_married = (RadioButton) findViewById(R.id.m_married);
        m_unmarried = (RadioButton) findViewById(R.id.m_unmarried);
        m_soon = (RadioButton) findViewById(R.id.m_soon);
        m_status = ClickedEntry.getMarry_status();
        if(m_status.equals("Married")){
            m_married.setChecked(true);
        }
        else if(m_status.equals("Unmarried")){
            m_unmarried.setChecked(true);
        }
        else if(m_status.equals("Soon to be married")){
            m_soon.setChecked(true);
        }

        //For Business
        businessView = (EditText) findViewById(R.id.business_edit_text);
        businessView.setText(ClickedEntry.getBusiness(),TextView.BufferType.EDITABLE);

        startConsumingReason = ClickedEntry.getHabit_reason();
        start1=(CheckBox) findViewById(R.id.habit_started_with_friends);
        start2=(CheckBox) findViewById(R.id.habit_started_for_hunger);
        start3=(CheckBox) findViewById(R.id.habit_started_social_issues);
        start4=(CheckBox) findViewById(R.id.habit_started_other);

        if(startConsumingReason.contains(start1.getText().toString()))
        {
            start1.setChecked(true);
        }

        if(startConsumingReason.contains(start2.getText().toString()))
        {
            start2.setChecked(true);
        }

        if(startConsumingReason.contains(start3.getText().toString()))
        {
            start3.setChecked(true);
        }

        if(startConsumingReason.contains(start4.getText().toString()))
        {
            start4.setChecked(true);
        }

        otherHabit = ClickedEntry.getHabit();
        otherHabit1 = (CheckBox) findViewById(R.id.alchohol_habit);
        otherHabit2 = (CheckBox) findViewById(R.id.drugs_habit);
        if(otherHabit.contains(otherHabit1.getText().toString()))
        {
            otherHabit1.setChecked(true);
        }

        if(otherHabit.contains(otherHabit2.getText().toString()))
        {
            otherHabit2.setChecked(true);
        }

        tobaccoConsumptionDiseases = ClickedEntry.getAware_disease();
        awareDisease1 = (CheckBox) findViewById(R.id.disease_aware_1);
        awareDisease2 = (CheckBox) findViewById(R.id.disease_aware_2);
        awareDisease3 = (CheckBox) findViewById(R.id.disease_aware_3);
        awareDisease4 = (CheckBox) findViewById(R.id.disease_aware_4);
        awareDisease5 = (CheckBox) findViewById(R.id.disease_aware_5);
        awareDisease6 = (CheckBox) findViewById(R.id.disease_aware_6);
        awareDisease7 = (CheckBox) findViewById(R.id.disease_aware_7);
        awareDisease8 = (CheckBox) findViewById(R.id.disease_aware_8);
        awareDisease9 = (CheckBox) findViewById(R.id.disease_aware_9);
        awareDisease10 = (CheckBox) findViewById(R.id.disease_aware_10);
        if(tobaccoConsumptionDiseases.contains(awareDisease1.getText().toString()))
        {
            awareDisease1.setChecked(true);
        }

        if(tobaccoConsumptionDiseases.contains(awareDisease2.getText().toString()))
        {
            awareDisease2.setChecked(true);
        }

        if(tobaccoConsumptionDiseases.contains(awareDisease3.getText().toString()))
        {
            awareDisease3.setChecked(true);
        }

        if(tobaccoConsumptionDiseases.contains(awareDisease4.getText().toString()))
        {
            awareDisease4.setChecked(true);
        }
        if(tobaccoConsumptionDiseases.contains(awareDisease5.getText().toString()))
        {
            awareDisease5.setChecked(true);
        }
        if(tobaccoConsumptionDiseases.contains(awareDisease6.getText().toString()))
        {
            awareDisease6.setChecked(true);
        }

        if(tobaccoConsumptionDiseases.contains(awareDisease7.getText().toString()))
        {
            awareDisease7.setChecked(true);
        }

        if(tobaccoConsumptionDiseases.contains(awareDisease8.getText().toString()))
        {
            awareDisease8.setChecked(true);
        }

        if(tobaccoConsumptionDiseases.contains(awareDisease9.getText().toString()))
        {
            awareDisease9.setChecked(true);
        }
        if(tobaccoConsumptionDiseases.contains(awareDisease10.getText().toString()))
        {
            awareDisease10.setChecked(true);
        }

        reasonForQuitting = ClickedEntry.getQuit_reason();
        reasonQuitting1 = (CheckBox) findViewById(R.id.quitting_1);
        reasonQuitting2 = (CheckBox) findViewById(R.id.quitting_2);
        reasonQuitting3 = (CheckBox) findViewById(R.id.quitting_3);
        reasonQuitting4 = (CheckBox) findViewById(R.id.quitting_4);
        if(reasonForQuitting.contains(reasonQuitting1.getText().toString()))
        {
            reasonQuitting1.setChecked(true);
        }

        if(reasonForQuitting.contains(reasonQuitting2.getText().toString()))
        {
            reasonQuitting2.setChecked(true);
        }

        if(reasonForQuitting.contains(reasonQuitting3.getText().toString()))
        {
            reasonQuitting3.setChecked(true);
        }

        if(reasonForQuitting.contains(reasonQuitting4.getText().toString()))
        {
            reasonQuitting4.setChecked(true);
        }

        cravingTime = ClickedEntry.getCraving_time();
        craving1 = (CheckBox) findViewById(R.id.craving_1);
        craving2 = (CheckBox) findViewById(R.id.craving_2);
        craving3 = (CheckBox) findViewById(R.id.craving_3);
        craving4 = (CheckBox) findViewById(R.id.craving_4);
        craving5 = (CheckBox) findViewById(R.id.craving_5);
        craving6 = (CheckBox) findViewById(R.id.craving_6);
        craving7 = (CheckBox) findViewById(R.id.craving_7);
        craving8 = (CheckBox) findViewById(R.id.craving_8);
        craving9 = (CheckBox) findViewById(R.id.craving_9);
        if(cravingTime.contains(craving1.getText().toString()))
        {
            craving1.setChecked(true);
        }

        if(cravingTime.contains(craving2.getText().toString()))
        {
            craving2.setChecked(true);
        }

        if(cravingTime.contains(craving3.getText().toString()))
        {
            craving3.setChecked(true);
        }

        if(cravingTime.contains(craving4.getText().toString()))
        {
            craving4.setChecked(true);
        }
        if(cravingTime.contains(craving5.getText().toString()))
        {
            craving5.setChecked(true);
        }
        if(cravingTime.contains(craving6.getText().toString()))
        {
            craving6.setChecked(true);
        }

        if(cravingTime.contains(craving7.getText().toString()))
        {
            craving7.setChecked(true);
        }

        if(cravingTime.contains(craving8.getText().toString()))
        {
            craving8.setChecked(true);
        }

        if(cravingTime.contains(craving9.getText().toString()))
        {
            craving9.setChecked(true);
        }

        //For Sex
        /*rg = (RadioGroup)findViewById(R.id.sex_group);
        String sex = ClickedEntry.getSex();
        if(sex.contains("M")){
            rg.check(R.id.sex_male);
        }
        else if(sex.contains("F")){
            rg.check(R.id.sex_female);
        }
        else {
            rg.check(R.id.sex_other);
        }*/

        //For Contact No


        mSaveButton=(Button) findViewById(R.id.save);

        mSaveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                                    m_status = ((RadioButton)findViewById(rg1.getCheckedRadioButtonId())).getText().toString();

                                    //For Med History
                                    med_history="";
                                    med1=(CheckBox) findViewById(R.id.disease_1);
                                    med2=(CheckBox) findViewById(R.id.disease_2);
                                    med3=(CheckBox) findViewById(R.id.disease_3);
                                    med4=(CheckBox) findViewById(R.id.disease_4);
                                    med5=(CheckBox) findViewById(R.id.disease_5);
                                    if(med1.isChecked())
                                    {
                                        med_history+=med1.getText().toString()+"/";
                                    }
                                    if(med2.isChecked())
                                    {
                                        med_history+=med2.getText().toString()+"/";
                                    }
                                    if(med3.isChecked())
                                    {
                                        med_history+=med3.getText().toString()+"/";
                                    }
                                    if(med4.isChecked())
                                    {
                                        med_history+=med4.getText().toString()+"/";
                                    }
                                    if(med5.isChecked())
                                    {
                                        med_history+=med5.getText().toString()+"/";
                                    }

                                    startConsumingReason="";
                                    if(start1.isChecked()){
                                        startConsumingReason += start1.getText() + "/";
                                    }
                                    if(start2.isChecked()){
                                        startConsumingReason += start2.getText() + "/";
                                    }
                                    if(start3.isChecked()){
                                        startConsumingReason += start3.getText() + "/";
                                    }
                                    if(start4.isChecked()){
                                        startConsumingReason += start4.getText() + "/";
                                    }

                                    otherHabit = "";
                                    if(otherHabit1.isChecked()){
                                        otherHabit += otherHabit1.getText() + "/";
                                    }
                                    if(otherHabit2.isChecked()){
                                        otherHabit += otherHabit2.getText() + "/";
                                    }

                                    tobaccoConsumptionDiseases = "";
                                    if(awareDisease1.isChecked()){
                                        tobaccoConsumptionDiseases += awareDisease1.getText() + "/";
                                    }
                                    if(awareDisease2.isChecked()){
                                        tobaccoConsumptionDiseases += awareDisease2.getText() + "/";
                                    }
                                    if(awareDisease3.isChecked()){
                                        tobaccoConsumptionDiseases += awareDisease3.getText() + "/";
                                    }
                                    if(awareDisease4.isChecked()){
                                        tobaccoConsumptionDiseases += awareDisease4.getText() + "/";
                                    }
                                    if(awareDisease5.isChecked()){
                                        tobaccoConsumptionDiseases += awareDisease5.getText() + "/";
                                    }
                                    if(awareDisease6.isChecked()){
                                        tobaccoConsumptionDiseases += awareDisease6.getText() + "/";
                                    }
                                    if(awareDisease7.isChecked()){
                                        tobaccoConsumptionDiseases += awareDisease7.getText() + "/";
                                    }
                                    if(awareDisease8.isChecked()){
                                        tobaccoConsumptionDiseases += awareDisease8.getText() + "/";
                                    }
                                    if(awareDisease9.isChecked()){
                                        tobaccoConsumptionDiseases += awareDisease9.getText() + "/";
                                    }
                                    if(awareDisease10.isChecked()){
                                        tobaccoConsumptionDiseases += awareDisease10.getText() + "/";
                                    }

                                    reasonForQuitting = "";
                                    if(reasonQuitting1.isChecked()){
                                        reasonForQuitting += reasonQuitting1.getText();
                                    }
                                    if(reasonQuitting2.isChecked()){
                                        reasonForQuitting += reasonQuitting2.getText();
                                    }
                                    if(reasonQuitting3.isChecked()){
                                        reasonForQuitting += reasonQuitting3.getText();
                                    }
                                    if(reasonQuitting4.isChecked()){
                                        reasonForQuitting += reasonQuitting4.getText();
                                    }

                                    cravingTime = "";
                                    if(craving1.isChecked()){
                                        cravingTime += craving1.getText() + "/";
                                    }
                                    if(craving2.isChecked()){
                                        cravingTime += craving2.getText() + "/";
                                    }
                                    if(craving3.isChecked()){
                                        cravingTime += craving3.getText() + "/";
                                    }
                                    if(craving4.isChecked()){
                                        cravingTime += craving4.getText() + "/";
                                    }
                                    if(craving5.isChecked()){
                                        cravingTime += craving5.getText() + "/";
                                    }
                                    if(craving6.isChecked()){
                                        cravingTime += craving6.getText() + "/";
                                    }
                                    if(craving7.isChecked()){
                                        cravingTime += craving7.getText() + "/";
                                    }
                                    if(craving8.isChecked()){
                                        cravingTime += craving8.getText() + "/";
                                    }
                                    if(craving9.isChecked()){
                                        cravingTime += craving9.getText() + "/";
                                    }


                Entry patient = new Entry(nameView.getText().toString(), ClickedEntry.getAge(),ClickedEntry.getSex(), ClickedEntry.getInterest(), med_history, contactView.getText().toString() ,emailView.getText().toString(),addressView.getText().toString(),
                        ClickedEntry.getChewText(),ClickedEntry.getChew_history(),
                        ClickedEntry.getChew_freq(), ClickedEntry.getChew_cost(),ClickedEntry.getSmokeText(), ClickedEntry.getSmokeHistory(), ClickedEntry.getSmoke_freq(), ClickedEntry.getSmoke_cost(), m_status,
                        businessView.getText().toString(), Integer.parseInt(salaryView.getText().toString()), ClickedEntry.getTime(), ClickedEntry.getFormattedDate(), ClickedEntry.getMorning_status(), ClickedEntry.getFamily_status(),
                        startConsumingReason, otherHabit, ClickedEntry.getAware_status(), tobaccoConsumptionDiseases, ClickedEntry.getQuit_status(), reasonForQuitting, ClickedEntry.getQuit_before_status(),
                        cravingTime, ClickedEntry.getId(),ClickedEntry.getMessage());

                //For updation
                FirebaseMethods.updatePatient(ClickedEntry.getId(),patient);

                Intent intent = new Intent(UpdateActivity.this,MainActivity.class);

                Toast.makeText(getBaseContext(), "Successfully updated" , Toast.LENGTH_SHORT).show();
                startActivity(intent);


            }

        });



    }


}
