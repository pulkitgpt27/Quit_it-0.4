
package com.example.android.quitit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ClientCertRequest;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

/**
 * Created by Pulkit on 23-07-2017.
 */

public class ViewActivity extends AppCompatActivity {
    public static final String ANONYMOUS = "anonymous";

    private String mUsername;
    private int id;

    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPatientDatabaseReference;
    //private ListView mPatientListView;
    //private EntriesListAdapter mPatientAdapter;
    /*private Button mSaveButton;
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
    */

    Entry ClickedEntry;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Bundle B = this.getIntent().getExtras();
        ClickedEntry = B.getParcelable("ClickedEntry");

        mUsername=ANONYMOUS;
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mPatientDatabaseReference=mFirebaseDatabase.getReference().child("patient");


        final TextView name_text_view = (TextView) findViewById(R.id.name_text_view);
        name_text_view.setText(ClickedEntry.getName());

        final TextView age_text_view = (TextView) findViewById(R.id.age_text_view);
      
        age_text_view.setText(String.valueOf(ClickedEntry.getAge()));
        
        final TextView sex_text_view = (TextView) findViewById(R.id.sex_text_view);
        
        sex_text_view.setText(ClickedEntry.getSex());

        final TextView contact_text_view = (TextView) findViewById(R.id.contact_text_view);
        contact_text_view.setText(ClickedEntry.getContact());

        final TextView email_text_view = (TextView) findViewById(R.id.email_text_view);
        email_text_view.setText(ClickedEntry.getEmail());

        final TextView address_text_view = (TextView) findViewById(R.id.address_text_view);
        address_text_view.setText(ClickedEntry.getAddress());

        final TextView medical_history_text_view = (TextView) findViewById(R.id.medicalHistory_text_view);
        medical_history_text_view.setText(ClickedEntry.getMed_history());

        String consume = null;
        if(ClickedEntry.getSmokeHistory() != 0 && ClickedEntry.getChew_history()!=0){
            consume = "Ciggerates/Bidi/Hukkah " + "\nTambakoo/Gutka/Pan";
        }
        else if(ClickedEntry.getSmokeHistory() == 0){
            consume = "Tambakoo/Gutka/Pan";
        }
        else if(ClickedEntry.getChew_history() == 0){
            consume = "Ciggerates/Bidi/Hukkah";
        }
        final TextView consume_text_view = (TextView) findViewById(R.id.consume_text_view);
        consume_text_view.setText(consume);

        int chewYears = ClickedEntry.getChew_history()/365;
        int chewMonths = (ClickedEntry.getChew_history()%365)/30;

        final TextView years_chewing_input = (TextView) findViewById(R.id.years_chewing_text_view);
        years_chewing_input.setText(String.valueOf(chewYears));

        final TextView months_chewing_text_view = (TextView) findViewById(R.id.months_chewing_text_view);
        months_chewing_text_view.setText(String.valueOf(chewMonths));

        final TextView often_chewing_text_view = (TextView) findViewById(R.id.often_chewing_text_view_value);
        often_chewing_text_view.setText(String.valueOf(ClickedEntry.getChew_freq()));

        final TextView cost_packet_text_view = (TextView) findViewById(R.id.cost_chewing_text_view);
        cost_packet_text_view.setText(String.valueOf(ClickedEntry.getChew_cost()));

        final TextView marrital_status_text_view = (TextView) findViewById(R.id.marrital_status_text_view);
        marrital_status_text_view.setText(ClickedEntry.getMarry_status());

        final TextView business_text_view = (TextView) findViewById(R.id.business_text_view);
        business_text_view.setText(ClickedEntry.getBusiness());

        final TextView salary_text_view = (TextView) findViewById(R.id.salary_text_view);
        salary_text_view.setText(String.valueOf(ClickedEntry.getSalary()));

        final TextView morning_consumer_text_view = (TextView) findViewById(R.id.morning_consumer_text_view);
        morning_consumer_text_view.setText(ClickedEntry.getMorning_status());

        final TextView family_consumes_text_view = (TextView) findViewById(R.id.family_consumes_text_view);
        family_consumes_text_view.setText(ClickedEntry.getFamily_status());

        final TextView start_consuming_text_view = (TextView) findViewById(R.id.start_consuming_text_view);
        start_consuming_text_view.setText(ClickedEntry.getHabit_reason());

        final TextView habit_text_view = (TextView) findViewById(R.id.habit_text_view);
        habit_text_view.setText(ClickedEntry.getHabit());

        final TextView aware_harms_text_view = (TextView) findViewById(R.id.aware_harms_text_view);
        aware_harms_text_view.setText(ClickedEntry.getAware_status());

        final TextView disease_text_view = (TextView) findViewById(R.id.diseases_text_view);
        disease_text_view.setText(ClickedEntry.getAware_disease());

        final TextView aware_quitting_text_view = (TextView) findViewById(R.id.aware_quitting_text_view);
        aware_quitting_text_view.setText(ClickedEntry.getQuit_status());

        final TextView reason_text_view = (TextView) findViewById(R.id.reason_text_view);
        reason_text_view.setText(ClickedEntry.getQuit_reason());

        final TextView tried_quitting_text_view = (TextView) findViewById(R.id.tried_quitting_text_view);
        tried_quitting_text_view.setText(ClickedEntry.getQuit_before_status());

        final TextView craving_text_view = (TextView) findViewById(R.id.craving_text_view);
        craving_text_view.setText(ClickedEntry.getCraving_time());

        Log.e("error",""+ClickedEntry.getSmokeHistory());
        int smokeYears = ClickedEntry.getSmokeHistory()/365;
        int smokeMonths = (ClickedEntry.getSmokeHistory()%365)/30;

        final TextView years_smoking_text_view = (TextView) findViewById(R.id.smoking_years_text_view);
        years_smoking_text_view.setText(String.valueOf(smokeYears));

        final TextView months_smoking_text_view = (TextView) findViewById(R.id.smoking_months_text_view);
        months_smoking_text_view.setText(String.valueOf(smokeMonths));

        final TextView often_smoking_text_view = (TextView) findViewById(R.id.often_text_view);
        often_smoking_text_view.setText(String.valueOf(ClickedEntry.getSmoke_freq()));

        final TextView cost_ciggartte_text_view = (TextView) findViewById(R.id.cost_smoking_text_view_value);
        cost_ciggartte_text_view.setText(String.valueOf(String.valueOf(ClickedEntry.getSmoke_cost())));

    }
}
