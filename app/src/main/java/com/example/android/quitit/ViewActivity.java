
package com.example.android.quitit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

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

    Entry ClickedEntry;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Bundle B = this.getIntent().getExtras();
        ClickedEntry = B.getParcelable("ClickedEntry");

        mUsername=ANONYMOUS;
        final TextView name_text_view = (TextView) findViewById(R.id.name_text_view);
        name_text_view.setText(ClickedEntry.getName());
        final TextView age_text_view = (TextView) findViewById(R.id.age_text_view);
        name_text_view.setText(ClickedEntry.getAge());
        final TextView sex_text_view = (TextView) findViewById(R.id.sex_text_view);
        name_text_view.setText(ClickedEntry.getSex());
        final TextView contact_text_view = (TextView) findViewById(R.id.contact_text_view);
        name_text_view.setText(ClickedEntry.getContact());
        final TextView email_text_view = (TextView) findViewById(R.id.email_text_view);
        /*name_text_view.setText(ClickedEntry.getEmail());
        final TextView address_text_view = (TextView) findViewById(R.id.address_text_view);
        name_text_view.setText(ClickedEntry.getAddress());
        final TextView medical_history_text_view = (TextView) findViewById(R.id.medicalHistory_text_view);
        name_text_view.setText(ClickedEntry.getMed_history());
        final TextView consume_text_view = (TextView) findViewById(R.id.consume_text_view);
        name_text_view.setText(ClickedEntry.get);
        final TextView years_chewing_input = (TextView) findViewById(R.id.years_chewing_text_view);
        years_chewing_input.setText(ClickedEntry.);
        final EditText months_chewing_input = (EditText) findViewById(R.id.months_chewing_edit_text);
        final EditText often_chewing_input = (EditText) findViewById(R.id.often_chewing_edit_text);
        final EditText cost_packet_input = (EditText) findViewById(R.id.cost_chewing_edit_text);

        final EditText years_smoking_input = (EditText) findViewById(R.id.smoking_years_edit_text);
        final EditText months_smoking_input = (EditText) findViewById(R.id.smoking_months_edit_text);
        final EditText often_smoking_input = (EditText) findViewById(R.id.often_edit_text);
        final EditText cost_ciggartte_input = (EditText) findViewById(R.id.cost_smoking_edit_text);*/

    }
}
