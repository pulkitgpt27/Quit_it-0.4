package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static com.example.android.quitit.R.id.age;
import static com.example.android.quitit.R.id.interest;
import static java.lang.Integer.parseInt;


/**
 * Created by Ayush vaid on 12-06-2017.
 */
public class NewEntryActivity  extends AppCompatActivity {

    /*this is where the code for adding the data to the DATABASE will go */
    public static final String ANONYMOUS = "anonymous";

    private String mUsername;
    private int id;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry);

        mUsername=ANONYMOUS;
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mPatientDatabaseReference=mFirebaseDatabase.getReference().child("patient");


        final CheckBox chewer = (CheckBox) findViewById(R.id.chewer);
        final CheckBox smoker = (CheckBox) findViewById(R.id.smoker);


        final TextView chewing_heading = (TextView) findViewById(R.id.chewing_history_heading);
        final TextView time_chewing = (TextView) findViewById(R.id.chewing_time_text_view);
        final EditText years_chewing_input = (EditText) findViewById(R.id.years_chewing_edit_text);
        final EditText months_chewing_input = (EditText) findViewById(R.id.months_chewing_edit_text);
        final TextView often_chewing = (TextView) findViewById(R.id.often_chewing_text_view);
        final EditText often_chewing_input = (EditText) findViewById(R.id.often_chewing_edit_text);
        final TextView cost_packet = (TextView) findViewById(R.id.cost_of_one_packet); //textView
        final EditText cost_packet_input = (EditText) findViewById(R.id.cost_chewing_edit_text);


        final TextView smoking_heading = (TextView) findViewById(R.id.smoking_history_heading);
        final TextView time_smoking = (TextView) findViewById(R.id.smoking_time_text_view);
        final EditText years_smoking_input = (EditText) findViewById(R.id.smoking_years_edit_text);
        final EditText months_smoking_input = (EditText) findViewById(R.id.smoking_months_edit_text);
        final TextView often_smoking = (TextView) findViewById(R.id.often_smoking_text_view);
        final EditText often_smoking_input = (EditText) findViewById(R.id.often_edit_text);
        final TextView cost_ciggartte = (TextView) findViewById(R.id.cost_smoking_text_view); //textView
        final EditText cost_ciggartte_input = (EditText) findViewById(R.id.cost_smoking_edit_text);


        chewer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(!isChecked){
                    chewing_heading.setVisibility(View.GONE);
                    time_chewing.setVisibility(View.GONE);
                    years_chewing_input.setVisibility(View.GONE);
                    months_chewing_input.setVisibility(View.GONE);
                    often_chewing.setVisibility(View.GONE);
                    often_chewing_input.setVisibility(View.GONE);
                    cost_packet.setVisibility(View.GONE);
                    cost_packet_input.setVisibility(View.GONE);
                }
                else
                {
                    chewing_heading.setVisibility(View.VISIBLE);
                    time_chewing.setVisibility(View.VISIBLE);
                    years_chewing_input.setVisibility(View.VISIBLE);
                    months_chewing_input.setVisibility(View.VISIBLE);
                    often_chewing.setVisibility(View.VISIBLE);
                    often_chewing_input.setVisibility(View.VISIBLE);
                    cost_packet.setVisibility(View.VISIBLE);
                    cost_packet_input.setVisibility(View.VISIBLE);
                }
            }
        });

        smoker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(!isChecked){
                    smoking_heading.setVisibility(View.GONE);
                    time_smoking.setVisibility(View.GONE);
                    years_smoking_input.setVisibility(View.GONE);
                    months_smoking_input.setVisibility(View.GONE);
                    often_smoking.setVisibility(View.GONE);
                    often_smoking_input.setVisibility(View.GONE);
                    cost_ciggartte.setVisibility(View.GONE);
                    cost_ciggartte_input.setVisibility(View.GONE);
                }
                else {
                    smoking_heading.setVisibility(View.VISIBLE);
                    time_smoking.setVisibility(View.VISIBLE);
                    years_smoking_input.setVisibility(View.VISIBLE);
                    months_smoking_input.setVisibility(View.VISIBLE);
                    often_smoking.setVisibility(View.VISIBLE);
                    often_smoking_input.setVisibility(View.VISIBLE);
                    cost_ciggartte.setVisibility(View.VISIBLE);
                    cost_ciggartte_input.setVisibility(View.VISIBLE);
                }
            }
        });

        mSaveButton=(Button) findViewById(R.id.save);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For Name
                EditText nameView=(EditText)findViewById(R.id.name_edit_text);
                String name=nameView.getText().toString();

                //For Age
                EditText ageView=(EditText)findViewById(R.id.age_edit_text);
                int age= parseInt(ageView.getText().toString());

                //For Med History
                String med_history="";
                CheckBox med1=(CheckBox) findViewById(R.id.disease_1);
                CheckBox med2=(CheckBox) findViewById(R.id.disease_2);
                CheckBox med3=(CheckBox) findViewById(R.id.disease_3);
                CheckBox med4=(CheckBox) findViewById(R.id.disease_4);
                CheckBox med5=(CheckBox) findViewById(R.id.disease_5);
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


                //For Sex
                RadioGroup rg = (RadioGroup)findViewById(R.id.sex_group);
                String sex = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();

                //For Contact No
                EditText contactView=(EditText)findViewById(R.id.contact_edit_text);
                String contact= (contactView.getText().toString());

                if(chewer.isChecked()) {
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

                if(smoker.isChecked()){
                    //For chewing history
                    EditText smoke_yearView = (EditText) findViewById(R.id.smoking_years_edit_text);
                    int smoke_years = Integer.parseInt(smoke_yearView.getText().toString());

                    EditText smoke_monthView = (EditText) findViewById(R.id.smoking_months_edit_text);
                    int smoke_months = Integer.parseInt(smoke_monthView.getText().toString());

                    smoke_days = (smoke_years * 365) + (smoke_months * 30);

                    //For chewing frequency(in a day)
                    EditText smoke_frequencyView = (EditText) findViewById(R.id.often_edit_text);
                    chew_freq = Integer.parseInt(smoke_frequencyView.getText().toString());

                    //For avg cost of each chewing thing
                    EditText smoke_costView = (EditText) findViewById(R.id.cost_smoking_edit_text);
                    smoke_cost = Float.parseFloat(smoke_costView.getText().toString());
                }


                //For marital status m=marriage
                RadioGroup rg1 = (RadioGroup)findViewById(R.id.m_status_group);
                String m_status = ((RadioButton)findViewById(rg1.getCheckedRadioButtonId())).getText().toString();

                //For Future Plans
                //Spinner futurespinner = (Spinner)findViewById(R.id.future_spinner);
                //String future = futurespinner.getSelectedItem().toString();

                //For Business
                EditText businessView=(EditText)findViewById(R.id.business_edit_text);
                String business= (businessView.getText().toString());

                //For Salary
                EditText salaryView=(EditText)findViewById(R.id.salary_edit_text);
                int salary= Integer.parseInt(salaryView.getText().toString());

                //For Current time
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
                //SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                String formattedtime1 = df.format(c.getTime());

                //For current date
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate1 = df1.format(c.getTime());

                //For id
               // DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                //Query lastQuery = databaseReference.child("patient").orderByKey().limitToLast(1);
                //lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                  /*  @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        if(dataSnapshot==null)
                        {
                            id=1;
                        }
                        else
                        {
                            id = (dataSnapshot!=null)? (int) dataSnapshot.child("id").getValue() :1;
                            id++;
                        }
                    }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Handle possible errors.
                }
            });*/

            //For fill more
                Switch S = (Switch) findViewById(R.id.mySwitch);
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

                            morning_consumer.setVisibility(LinearLayout.GONE);
                            family_consumes.setVisibility(LinearLayout.GONE);
                            started_how.setVisibility(LinearLayout.GONE);
                            other_habit.setVisibility(LinearLayout.GONE);
                            aware_harms.setVisibility(LinearLayout.GONE);
                            disease_aware.setVisibility(LinearLayout.GONE);
                            quit.setVisibility(LinearLayout.GONE);
                            reasonforquitting.setVisibility(LinearLayout.GONE);
                            tried_quitting.setVisibility(LinearLayout.GONE);
                            craving.setVisibility(LinearLayout.GONE);
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
                            //Morning consumer
                            RadioGroup rg1 = (RadioGroup)findViewById(R.id.within_30_mins);
                            morning_status = ((RadioButton)findViewById(rg1.getCheckedRadioButtonId())).getText().toString();

                            //Anyone in Family consume tobacco
                            RadioGroup rg2 = (RadioGroup)findViewById(R.id.family_consumes_RG);
                            family_status = ((RadioButton)findViewById(rg1.getCheckedRadioButtonId())).getText().toString();

                            //how did start consuming

                            CheckBox res1=(CheckBox) findViewById(R.id.habit_started_with_friends);
                            CheckBox res2=(CheckBox) findViewById(R.id.habit_started_for_hunger);
                            CheckBox res3=(CheckBox) findViewById(R.id.habbit_started_social_issues);
                            CheckBox res4=(CheckBox) findViewById(R.id.habit_started_other);

                            if(res1.isChecked())
                            {
                                habit_reason+=res1.getText().toString()+" ";
                            }
                            if(res2.isChecked())
                            {
                                habit_reason+=res2.getText().toString()+" ";
                            }
                            if(res3.isChecked())
                            {
                                habit_reason+=res3.getText().toString()+" ";
                            }
                            if(res4.isChecked())
                            {
                                habit_reason+=res4.getText().toString()+" ";
                            }
                            
                        }
                    }
                });


                String interest = "";
                String future = "";
                Entry patient=new Entry(name,age,sex,interest,med_history,contact,chew_days,chew_freq,chew_cost,m_status,future,business,salary,formattedtime1,formattedDate1,id);
                mPatientDatabaseReference.push().setValue(patient);
                Intent i=new Intent(NewEntryActivity.this,MainActivity.class);
                startActivity(i);


            }
        });

    }
}
