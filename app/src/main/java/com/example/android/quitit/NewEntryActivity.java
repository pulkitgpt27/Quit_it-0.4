package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.view.View.GONE;
import static java.lang.Integer.parseInt;


/**
 * Created by Ayush vaid on 12-06-2017.
 */
public class NewEntryActivity  extends AppCompatActivity {

    /*this is where the code for adding the data to the DATABASE will go */
    public static final String ANONYMOUS = "anonymous";

    private String mUsername;
    private int id=1;

    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPatientDatabaseReference;
    //private ListView mPatientListView;
    //private EntriesListAdapter mPatientAdapter;
    private Button mSaveButton;
    private ChildEventListener mChildEventListener;
    private int chew_days;
    private int chew_freq;
    private int smoke_days;
    private int smoke_freq;
    private float chew_cost;
    private float smoke_cost;
    private String morning_status="";
    private String family_status="";
    private String habit_reason="";
    private String habbit="";
    private String aware_status="";
    private String aware_diseases="";
    private String quit_status="";
    private String quit_reason="";
    private String quit_before_status="";
    private String craving_time="";
    private String name="";
    private int age;
    private String sex="";
    private String contact="";
    private String m_status="";
    private String business="";
    private int salary=1;
    private String email="";
    private String address="";
    private String chewText="";
    private String smokeText="";



    //for validation
    boolean[] validation;
    private String message="";
    private String med_history="";
    private Entry patient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry);

        mUsername=ANONYMOUS;
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mPatientDatabaseReference=mFirebaseDatabase.getReference().child("patient");



        //**************VALIDATIONS**************
        //************ALL START WITH SYMBOL $************
        validation = new boolean[5];

        final EditText $name = (EditText) findViewById(R.id.name_edit_text);
        final EditText $phone = (EditText) findViewById(R.id.contact_edit_text);
        final EditText $age = (EditText) findViewById(R.id.age_edit_text);
        final EditText $email = (EditText) findViewById(R.id.email_edit_text);
        final EditText $salary = (EditText) findViewById(R.id.salary_edit_text);


        final TextInputLayout $name_layout = (TextInputLayout) findViewById(R.id.name_layout);
        final TextInputLayout $age_layout = (TextInputLayout) findViewById(R.id.age_layout);
        final TextInputLayout $phone_layout = (TextInputLayout) findViewById(R.id.contact_layout);
        final TextInputLayout $email_layout = (TextInputLayout) findViewById(R.id.email_layout);
        final TextInputLayout $salary_layout = (TextInputLayout) findViewById(R.id.salary_layout);

        $name_layout.setBackgroundColor(getResources().getColor(R.color.magnitude9));
        $age_layout.setBackgroundColor(getResources().getColor(R.color.magnitude9));
        $phone_layout.setBackgroundColor(getResources().getColor(R.color.magnitude9));
        $email_layout.setBackgroundColor(getResources().getColor(R.color.magnitude9));
        $salary_layout.setBackgroundColor(getResources().getColor(R.color.magnitude9));

        $name_layout.getBackground().setAlpha(0);
        $phone_layout.getBackground().setAlpha(0);
        $age_layout.getBackground().setAlpha(0);
        $email_layout.getBackground().setAlpha(0);
        $salary_layout.getBackground().setAlpha(0);

        $name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b){
                if(!b) {
                    if (!ValidateEntry.validateEmpty($name.getText().toString())) {
                        $name_layout.setErrorEnabled(true);
                        $name_layout.setError("Name is Empty!");
                        $name_layout.getBackground().setAlpha(51);
                        validation[0] = false;
                    }
                    else if (!ValidateEntry.validateNameDigit($name.getText().toString())){  //checks if name contains a number{
                        $name_layout.setError("Name contains a number!");
                        $name_layout.setErrorEnabled(true);
                        $name_layout.getBackground().setAlpha(51);
                        validation[0]=false;
                    }
                    else {
                        $name_layout.getBackground().setAlpha(0);
                        $name_layout.setErrorEnabled(false);
                        validation[0] = true;
                    }
                }
            }
        });


        $age.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    if (!ValidateEntry.validateEmpty($age.getText().toString())) {
                        $age_layout.setErrorEnabled(true);
                        $age_layout.setError("Age is empty");
                        $age_layout.getBackground().setAlpha(51);
                        validation[1] = false;
                    }
                    else if (!ValidateEntry.validateAge($age.getText().toString())){
                        $age_layout.setErrorEnabled(true);
                        $age_layout.setError("Age is invalid");
                        $age_layout.getBackground().setAlpha(51);
                        validation[1] = false;
                    }
                    else {
                        $age_layout.getBackground().setAlpha(0);
                        $age_layout.setErrorEnabled(true);
                        validation[1] = true;
                    }
                }
            }
        });


        $email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    if (!ValidateEntry.validateEmail($email.getText().toString())) {

                        $email_layout.setErrorEnabled(true);
                        $email_layout.setError("Invalid Email");
                        $email.setError("Invalid Email");
                        $email_layout.getBackground().setAlpha(51);
                        validation[2] = false;
                    }
                    else
                    {    $email_layout.getBackground().setAlpha(0);
                        $email_layout.setErrorEnabled(false);
                        validation[2] = true;
                    }
                }
            }
        });


        $phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(!ValidateEntry.validatePhone($phone.getText().toString())) {
                        $phone_layout.setErrorEnabled(true);
                        $phone_layout.setError("Number Invalid");
                        $phone_layout.getBackground().setAlpha(51);
                        validation[3] = false;
                    }
                    else
                    {   $phone_layout.getBackground().setAlpha(0);
                        $phone_layout.setErrorEnabled(false);
                        validation[3] = true;
                    }
                }
            }
        });

        $salary.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(!ValidateEntry.validateInteger($salary.getText().toString())) {
                        $salary_layout.setError("Salary amount is invalid");
                        $salary_layout.getBackground().setAlpha(51);
                        $salary_layout.setErrorEnabled(true);
                        validation[4] = false;
                    }
                    else {
                        $salary_layout.getBackground().setAlpha(0);
                        $salary_layout.setErrorEnabled(false);
                        validation[4] = true;
                    }
                }
            }
        });

        //**************DISSAPPEARING VIEWS*****************
        final CheckBox chewer = (CheckBox) findViewById(R.id.chewer);
        final CheckBox smoker = (CheckBox) findViewById(R.id.smoker);

        //final TextView chewing_heading = (TextView) findViewById(R.id.chewing_history_heading);
        final TextView time_chewing = (TextView) findViewById(R.id.chewing_time_text_view);
        final EditText years_chewing_input = (EditText) findViewById(R.id.years_chewing_edit_text);
        final EditText months_chewing_input = (EditText) findViewById(R.id.months_chewing_edit_text);
        final TextView often_chewing = (TextView) findViewById(R.id.often_chewing_text_view);
        final EditText often_chewing_input = (EditText) findViewById(R.id.often_chewing_edit_text);
        final TextView cost_packet = (TextView) findViewById(R.id.cost_of_one_packet); //textView
        final EditText cost_packet_input = (EditText) findViewById(R.id.cost_chewing_edit_text);


        //final TextView smoking_heading = (TextView) findViewById(R.id.smoking_history_heading);
        final TextView time_smoking = (TextView) findViewById(R.id.smoking_time_text_view);
        final EditText years_smoking_input = (EditText) findViewById(R.id.smoking_years_edit_text);
        final EditText months_smoking_input = (EditText) findViewById(R.id.smoking_months_edit_text);
        final TextView often_smoking = (TextView) findViewById(R.id.often_smoking_text_view);
        final EditText often_smoking_input = (EditText) findViewById(R.id.often_smoking_edit_text);
        final TextView cost_ciggartte = (TextView) findViewById(R.id.cost_smoking_text_view); //textView
        final EditText cost_ciggartte_input = (EditText) findViewById(R.id.cost_smoking_edit_text);

        final LinearLayout smoking_history= (LinearLayout) findViewById(R.id.smoking_history_layout);
        final LinearLayout chewing_history= (LinearLayout) findViewById(R.id.chewing_history_layout);

        chewer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(!isChecked){
                    //chewing_heading.setVisibility(View.GONE);
                    time_chewing.setVisibility(View.GONE);
                    years_chewing_input.setVisibility(View.GONE);
                    months_chewing_input.setVisibility(View.GONE);
                    often_chewing.setVisibility(View.GONE);
                    often_chewing_input.setVisibility(View.GONE);
                    cost_packet.setVisibility(View.GONE);
                    cost_packet_input.setVisibility(View.GONE);
                    chewing_history.setVisibility(GONE);
                }
                else
                {
                    //chewing_heading.setVisibility(View.VISIBLE);
                    time_chewing.setVisibility(View.VISIBLE);
                    years_chewing_input.setVisibility(View.VISIBLE);
                    months_chewing_input.setVisibility(View.VISIBLE);
                    often_chewing.setVisibility(View.VISIBLE);
                    often_chewing_input.setVisibility(View.VISIBLE);
                    cost_packet.setVisibility(View.VISIBLE);
                    cost_packet_input.setVisibility(View.VISIBLE);
                    chewing_history.setVisibility(View.VISIBLE);
                }
            }
        });

        smoker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(!isChecked){
                    //smoking_heading.setVisibility(View.GONE);
                    time_smoking.setVisibility(View.GONE);
                    years_smoking_input.setVisibility(View.GONE);
                    months_smoking_input.setVisibility(View.GONE);
                    often_smoking.setVisibility(View.GONE);
                    often_smoking_input.setVisibility(View.GONE);
                    cost_ciggartte.setVisibility(View.GONE);
                    cost_ciggartte_input.setVisibility(View.GONE);
                    smoking_history.setVisibility(View.GONE);
                }
                else {
                    //smoking_heading.setVisibility(View.VISIBLE);
                    time_smoking.setVisibility(View.VISIBLE);
                    years_smoking_input.setVisibility(View.VISIBLE);
                    months_smoking_input.setVisibility(View.VISIBLE);
                    often_smoking.setVisibility(View.VISIBLE);
                    often_smoking_input.setVisibility(View.VISIBLE);
                    cost_ciggartte.setVisibility(View.VISIBLE);
                    cost_ciggartte_input.setVisibility(View.VISIBLE);
                    smoking_history.setVisibility(View.VISIBLE);
                }
            }
        });


        //********************FILLMORE LAYOUTS***************************
        final Switch S = (Switch) findViewById(R.id.mySwitch);
        final LinearLayout morning_consumer = (LinearLayout) findViewById(R.id.morning_consumer_layout);
        final LinearLayout family_consumes = (LinearLayout) findViewById(R.id.family_consumes_layout);
        final LinearLayout started_how = (LinearLayout) findViewById(R.id.started_how_layout);
        final LinearLayout other_habit = (LinearLayout) findViewById(R.id.other_habit_layout);
        final LinearLayout aware_harms = (LinearLayout) findViewById(R.id.aware_harms_layout);
        final LinearLayout disease_aware = (LinearLayout) findViewById(R.id.disease_aware_layout);
        final LinearLayout quit = (LinearLayout) findViewById(R.id.quit_layout);
        final LinearLayout reasonforquitting = (LinearLayout) findViewById(R.id.reasonofquitting_layout);
        final LinearLayout tried_quitting = (LinearLayout) findViewById(R.id.tried_quitting_layout);
        final LinearLayout craving = (LinearLayout) findViewById(R.id.craving_layout);


        S.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!isChecked)
                {
                    morning_consumer.setVisibility(GONE);
                    family_consumes.setVisibility(GONE);
                    started_how.setVisibility(GONE);
                    other_habit.setVisibility(GONE);
                    aware_harms.setVisibility(GONE);
                    disease_aware.setVisibility(GONE);
                    quit.setVisibility(GONE);
                    reasonforquitting.setVisibility(GONE);
                    tried_quitting.setVisibility(GONE);
                    craving.setVisibility(GONE);
                }
                else
                {
                    morning_consumer.setVisibility(LinearLayout.VISIBLE);
                    family_consumes.setVisibility(LinearLayout.VISIBLE);
                    started_how.setVisibility(LinearLayout.VISIBLE);
                    other_habit.setVisibility(LinearLayout.VISIBLE);
                    aware_harms.setVisibility(LinearLayout.VISIBLE);

                    disease_aware.setVisibility(LinearLayout.VISIBLE);
                    quit.setVisibility(LinearLayout.VISIBLE);
                    reasonforquitting.setVisibility(LinearLayout.VISIBLE);
                    tried_quitting.setVisibility(LinearLayout.VISIBLE);
                    craving.setVisibility(LinearLayout.VISIBLE);
                }
            }
        });

        //for fill more switch

        //how did start consuming
        final CheckBox res1=(CheckBox) findViewById(R.id.habit_started_with_friends);
        final CheckBox res2=(CheckBox) findViewById(R.id.habit_started_for_hunger);
        final CheckBox res3=(CheckBox) findViewById(R.id.habbit_started_social_issues);
        final CheckBox res4=(CheckBox) findViewById(R.id.habit_started_other);
        //Anyone in Family consume tobacco
        final RadioGroup rg3 = (RadioGroup)findViewById(R.id.family_consumes_RG);
        //Morning consumer
        final RadioGroup rg2 = (RadioGroup)findViewById(R.id.within_30_mins);

        //For other Habbits
        final CheckBox hab1_checkbox=(CheckBox) findViewById(R.id.drugs_habit);
        final CheckBox hab2_checkbox=(CheckBox) findViewById(R.id.alchohol_habit);

        //Aware or not
        final RadioGroup rg4 = (RadioGroup)findViewById(R.id.aware_radio_group);

        final RadioButton yes_aware = (RadioButton) findViewById(R.id.yes_aware);
        //Which diseases
        final CheckBox disease_checkbox1=(CheckBox) findViewById(R.id.disease_aware_1);
        final CheckBox disease_checkbox2=(CheckBox) findViewById(R.id.disease_aware_2);
        final CheckBox disease_checkbox3=(CheckBox) findViewById(R.id.disease_aware_3);
        final CheckBox disease_checkbox4=(CheckBox) findViewById(R.id.disease_aware_4);
        final CheckBox disease_checkbox5=(CheckBox) findViewById(R.id.disease_aware_5);
        final CheckBox disease_checkbox6=(CheckBox) findViewById(R.id.disease_aware_6);
        final CheckBox disease_checkbox7=(CheckBox) findViewById(R.id.disease_aware_7);
        final CheckBox disease_checkbox8=(CheckBox) findViewById(R.id.disease_aware_8);
        final CheckBox disease_checkbox9=(CheckBox) findViewById(R.id.disease_aware_9);
        final CheckBox disease_checkbox10=(CheckBox) findViewById(R.id.disease_aware_10);

        disease_checkbox1.setVisibility(View.GONE);
        disease_checkbox2.setVisibility(View.GONE);
        disease_checkbox3.setVisibility(View.GONE);
        disease_checkbox4.setVisibility(View.GONE);
        disease_checkbox5.setVisibility(View.GONE);
        disease_checkbox6.setVisibility(View.GONE);
        disease_checkbox7.setVisibility(View.GONE);
        disease_checkbox8.setVisibility(View.GONE);
        disease_checkbox9.setVisibility(View.GONE);
        disease_checkbox10.setVisibility(View.GONE);

        yes_aware.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    disease_checkbox1.setVisibility(View.GONE);
                    disease_checkbox2.setVisibility(View.GONE);
                    disease_checkbox3.setVisibility(View.GONE);
                    disease_checkbox4.setVisibility(View.GONE);
                    disease_checkbox5.setVisibility(View.GONE);
                    disease_checkbox6.setVisibility(View.GONE);
                    disease_checkbox7.setVisibility(View.GONE);
                    disease_checkbox8.setVisibility(View.GONE);
                    disease_checkbox9.setVisibility(View.GONE);
                    disease_checkbox10.setVisibility(View.GONE);
                }
                else
                {
                    disease_checkbox1.setVisibility(View.VISIBLE);
                    disease_checkbox2.setVisibility(View.VISIBLE);
                    disease_checkbox3.setVisibility(View.VISIBLE);
                    disease_checkbox4.setVisibility(View.VISIBLE);
                    disease_checkbox5.setVisibility(View.VISIBLE);
                    disease_checkbox6.setVisibility(View.VISIBLE);
                    disease_checkbox7.setVisibility(View.VISIBLE);
                    disease_checkbox8.setVisibility(View.VISIBLE);
                    disease_checkbox9.setVisibility(View.VISIBLE);
                    disease_checkbox10.setVisibility(View.VISIBLE);
                }
            }
        });


        //Want to quit yes or no
        final RadioGroup rg5 = (RadioGroup)findViewById(R.id.quit_radiogroup);

        final RadioButton yes_quitting = (RadioButton) findViewById(R.id.yes_quitting);

        //Why Quit
        final CheckBox reason_quit_checkbox1=(CheckBox) findViewById(R.id.quitting_1);
        final CheckBox reason_quit_checkbox2=(CheckBox) findViewById(R.id.quitting_2);
        final CheckBox reason_quit_checkbox3=(CheckBox) findViewById(R.id.quitting_3);
        final CheckBox reason_quit_checkbox4=(CheckBox) findViewById(R.id.quitting_4);

        reason_quit_checkbox1.setVisibility(View.GONE);
        reason_quit_checkbox2.setVisibility(View.GONE);
        reason_quit_checkbox3.setVisibility(View.GONE);
        reason_quit_checkbox4.setVisibility(View.GONE);

        yes_quitting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    reason_quit_checkbox1.setVisibility(View.GONE);
                    reason_quit_checkbox2.setVisibility(View.GONE);
                    reason_quit_checkbox3.setVisibility(View.GONE);
                    reason_quit_checkbox4.setVisibility(View.GONE);
                }
                else
                {
                    reason_quit_checkbox1.setVisibility(View.VISIBLE);
                    reason_quit_checkbox2.setVisibility(View.VISIBLE);
                    reason_quit_checkbox3.setVisibility(View.VISIBLE);
                    reason_quit_checkbox4.setVisibility(View.VISIBLE);
                }
            }
        });


        //Tried Quiting Before
        final RadioGroup rg6 = (RadioGroup)findViewById(R.id.quit_before_radiogroup);

        //Craving timings
        final CheckBox craving_checkbox1=(CheckBox) findViewById(R.id.craving_1);
        final CheckBox craving_checkbox2=(CheckBox) findViewById(R.id.craving_2);
        final CheckBox craving_checkbox3=(CheckBox) findViewById(R.id.craving_3);
        final CheckBox craving_checkbox4=(CheckBox) findViewById(R.id.craving_4);
        final CheckBox craving_checkbox5=(CheckBox) findViewById(R.id.craving_5);
        final CheckBox craving_checkbox6=(CheckBox) findViewById(R.id.craving_6);
        final CheckBox craving_checkbox7=(CheckBox) findViewById(R.id.craving_7);
        final CheckBox craving_checkbox8=(CheckBox) findViewById(R.id.craving_8);
        final CheckBox craving_checkbox9=(CheckBox) findViewById(R.id.craving_9);

        final ScrollView parent_scroll_view = (ScrollView) findViewById(R.id.parent_scroll);

        mSaveButton=(Button) findViewById(R.id.save);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validations for scrolling view
                if(!validation[0]){
                    parent_scroll_view.smoothScrollTo(0,$name_layout.getTop());
                }
                else if(!validation[1]){
                    parent_scroll_view.smoothScrollTo(0,$age_layout.getTop());
                }
                else if(!validation[3]){
                    parent_scroll_view.smoothScrollTo(0,$phone_layout.getTop());
                }
                else if(!validation[2]){
                    parent_scroll_view.smoothScrollTo(0,$email_layout.getTop());
                }
                else if(!validation[4]){
                    parent_scroll_view.smoothScrollTo(0,$salary_layout.getTop());
                }
                else {
                    EditText nameView = (EditText) findViewById(R.id.name_edit_text);
                    name = nameView.getText().toString();
                    //For Age
                    EditText ageView = (EditText) findViewById(R.id.age_edit_text);
                    age = parseInt(ageView.getText().toString());
                    //For Sex
                    RadioGroup rg = (RadioGroup) findViewById(R.id.sex_group);
                    sex = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                    //For Contact No
                    EditText contactView = (EditText) findViewById(R.id.contact_edit_text);
                    contact = (contactView.getText().toString());
                    //For email
                    email = $email.getText().toString();
                    //For address
                    EditText addressView = (EditText) findViewById(R.id.address_edit_text);
                    address = (addressView.getText().toString());

                    //For Med History
                    med_history = "";
                    CheckBox med1 = (CheckBox) findViewById(R.id.disease_1);
                    CheckBox med2 = (CheckBox) findViewById(R.id.disease_2);
                    CheckBox med3 = (CheckBox) findViewById(R.id.disease_3);
                    CheckBox med4 = (CheckBox) findViewById(R.id.disease_4);
                    CheckBox med5 = (CheckBox) findViewById(R.id.disease_5);
                    if (med1.isChecked()) {
                        med_history += med1.getText().toString() + " ";
                    }
                    if (med2.isChecked()) {
                        med_history += med2.getText().toString() + " ";
                    }
                    if (med3.isChecked()) {
                        med_history += med3.getText().toString() + " ";
                    }
                    if (med4.isChecked()) {
                        med_history += med4.getText().toString() + " ";
                    }
                    if (med5.isChecked()) {
                        med_history += med5.getText().toString() + " ";
                    }


                    if (chewer.isChecked()) {

                        //For chiewer text
                        chewText = chewer.getText().toString();

                        //For chewing history
                        EditText chew_yearView = (EditText) findViewById(R.id.years_chewing_edit_text);
                        int chew_years = Integer.parseInt(chew_yearView.getText().toString());

                        EditText chew_monthView = (EditText) findViewById(R.id.months_chewing_edit_text);
                        int chew_months = Integer.parseInt(chew_monthView.getText().toString());

                        chew_days = (chew_years * 365) + (chew_months * 30);

                        //For chewing frequency(in a day)
                        EditText chew_frequencyView = (EditText) findViewById(R.id.often_chewing_edit_text);
                        chew_freq = Integer.parseInt(chew_frequencyView.getText().toString());

                        //For avg cost of each chewing thing
                        EditText chew_costView = (EditText) findViewById(R.id.cost_chewing_edit_text);
                        chew_cost = Float.parseFloat(chew_costView.getText().toString());
                    }

                    if (smoker.isChecked()) {

                        //For chiewer text
                        smokeText = smoker.getText().toString();

                        //For smoking history
                        EditText smoke_yearView = (EditText) findViewById(R.id.smoking_years_edit_text);
                        int smoke_years = Integer.parseInt(smoke_yearView.getText().toString());

                        EditText smoke_monthView = (EditText) findViewById(R.id.smoking_months_edit_text);
                        int smoke_months = Integer.parseInt(smoke_monthView.getText().toString());

                        smoke_days = (smoke_years * 365) + (smoke_months * 30);

                        //For smoking frequency(in a day)
                        EditText smoke_frequencyView = (EditText) findViewById(R.id.often_smoking_edit_text);
                        smoke_freq = Integer.parseInt(smoke_frequencyView.getText().toString());

                        //For avg cost of each smoking thing
                        EditText smoke_costView = (EditText) findViewById(R.id.cost_smoking_edit_text);
                        smoke_cost = Float.parseFloat(smoke_costView.getText().toString());
                    }


                    //For marital status m=marriage
                    RadioGroup rg1 = (RadioGroup) findViewById(R.id.m_status_group);
                    m_status = ((RadioButton) findViewById(rg1.getCheckedRadioButtonId())).getText().toString();

                    //For Business
                    EditText businessView = (EditText) findViewById(R.id.business_edit_text);
                    business = (businessView.getText().toString());

                    //For Salary
                    EditText salaryView = (EditText) findViewById(R.id.salary_edit_text);
                    salary = Integer.parseInt(salaryView.getText().toString());

                    //For Current time
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
                    //SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    final String formattedtime1 = df.format(c.getTime());

                    //For current date
                    SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
                    final String formattedDate1 = df1.format(c.getTime());

                    //For fill more
                    if (S.isChecked()) {
                        morning_status = ((RadioButton) findViewById(rg2.getCheckedRadioButtonId())).getText().toString();
                        family_status = ((RadioButton) findViewById(rg3.getCheckedRadioButtonId())).getText().toString();
                        if (res1.isChecked()) {
                            habit_reason += res1.getText().toString() + " ";
                        }
                        if (res2.isChecked()) {
                            habit_reason += res2.getText().toString() + " ";
                        }
                        if (res3.isChecked()) {
                            habit_reason += res3.getText().toString() + " ";
                        }
                        if (res4.isChecked()) {
                            habit_reason += res4.getText().toString() + " ";
                        }
                        if (hab1_checkbox.isChecked()) {
                            habbit += hab1_checkbox.getText().toString() + " ";
                        }
                        if (hab2_checkbox.isChecked()) {
                            habbit += hab2_checkbox.getText().toString() + " ";
                        }
                        aware_status = ((RadioButton) findViewById(rg4.getCheckedRadioButtonId())).getText().toString();
                        if (disease_checkbox1.isChecked()) {
                            aware_diseases += disease_checkbox1.getText().toString() + "";
                        }

                        if (disease_checkbox2.isChecked()) {
                            aware_diseases += disease_checkbox2.getText().toString() + "";
                        }
                        if (disease_checkbox3.isChecked()) {
                            aware_diseases += disease_checkbox3.getText().toString() + "";
                        }
                        if (disease_checkbox4.isChecked()) {
                            aware_diseases += disease_checkbox4.getText().toString() + "";
                        }
                        if (disease_checkbox5.isChecked()) {
                            aware_diseases += disease_checkbox5.getText().toString() + "";
                        }
                        if (disease_checkbox6.isChecked()) {
                            aware_diseases += disease_checkbox6.getText().toString() + "";
                        }
                        if (disease_checkbox7.isChecked()) {
                            aware_diseases += disease_checkbox7.getText().toString() + "";
                        }
                        if (disease_checkbox8.isChecked()) {
                            aware_diseases += disease_checkbox8.getText().toString() + "";
                        }
                        if (disease_checkbox9.isChecked()) {
                            aware_diseases += disease_checkbox9.getText().toString() + "";
                        }
                        if (disease_checkbox10.isChecked()) {
                            aware_diseases += disease_checkbox10.getText().toString() + "";
                        }
                        quit_status = ((RadioButton) findViewById(rg5.getCheckedRadioButtonId())).getText().toString();

                        if (reason_quit_checkbox1.isChecked()) {
                            quit_reason += reason_quit_checkbox1.getText().toString() + "";
                        }
                        if (reason_quit_checkbox2.isChecked()) {
                            quit_reason += reason_quit_checkbox2.getText().toString() + "";
                        }
                        if (reason_quit_checkbox2.isChecked()) {
                            quit_reason += reason_quit_checkbox2.getText().toString() + "";
                        }
                        if (reason_quit_checkbox3.isChecked()) {
                            quit_reason += reason_quit_checkbox3.getText().toString() + "";
                        }
                        if (reason_quit_checkbox4.isChecked()) {
                            quit_reason += reason_quit_checkbox4.getText().toString() + "";
                        }
                        quit_before_status = ((RadioButton) findViewById(rg5.getCheckedRadioButtonId())).getText().toString();
                        if (craving_checkbox1.isChecked()) {
                            craving_time += craving_checkbox1.getText().toString() + "";
                        }
                        if (craving_checkbox2.isChecked()) {
                            craving_time += craving_checkbox2.getText().toString() + "";
                        }
                        if (craving_checkbox3.isChecked()) {
                            craving_time += craving_checkbox3.getText().toString() + "";
                        }
                        if (craving_checkbox4.isChecked()) {
                            craving_time += craving_checkbox4.getText().toString() + "";
                        }
                        if (craving_checkbox5.isChecked()) {
                            craving_time += craving_checkbox5.getText().toString() + "";
                        }
                        if (craving_checkbox6.isChecked()) {
                            craving_time += craving_checkbox6.getText().toString() + "";
                        }
                        if (craving_checkbox7.isChecked()) {
                            craving_time += craving_checkbox7.getText().toString() + "";
                        }
                        if (craving_checkbox8.isChecked()) {
                            craving_time += craving_checkbox8.getText().toString() + "";
                        }
                        if (craving_checkbox9.isChecked()) {
                            craving_time += craving_checkbox9.getText().toString() + "";
                        }
                    }
                    final String interest = "";
                    String future = "";

                    message = MessageActivity.getMessage(age, sex, chewer.isChecked(), chew_freq, smoker.isChecked(), smoke_freq, med_history, m_status, habit_reason);
                    mPatientDatabaseReference = FirebaseMethods.getFirebaseReference("patient");

                    final String[] uniqueKey = {""};
                    String key = "";

                    mPatientDatabaseReference
                            .push()
                            .setValue(null, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError,
                                                       DatabaseReference databaseReference) {
                                    uniqueKey[0] = databaseReference.getKey();

                                    DatabaseReference UpdatePatientDatabaseReference = FirebaseDatabase.getInstance().getReference().child("patient").child(uniqueKey[0]);
                                    try {
                                        patient = new Entry(name, age, sex, interest, med_history, contact, email, address, chewText, chew_days, chew_freq, chew_cost, smokeText, smoke_days, smoke_freq, smoke_cost, m_status, business, salary, formattedtime1, formattedDate1, morning_status,
                                                family_status, habit_reason, habbit, aware_status, aware_diseases, quit_status, quit_reason, quit_before_status, craving_time, uniqueKey[0], message);
                                        UpdatePatientDatabaseReference.setValue(patient);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("Updated", "hello" + patient.getId());
                                }
                            });
                    Intent i = new Intent(NewEntryActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });

    }
}
