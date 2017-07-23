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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry);
        mUsername=ANONYMOUS;
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mPatientDatabaseReference=mFirebaseDatabase.getReference().child("patient");


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

                //For smoking history
                EditText yearView=(EditText)findViewById(R.id.years_edit_text);
                int years= Integer.parseInt(yearView.getText().toString());

                EditText monthView=(EditText)findViewById(R.id.months_edit_text);
                int months= Integer.parseInt(monthView.getText().toString());

                int days=(years*365)+(months*30);

                //For smoking frequency(in a day)
                EditText frequencyView=(EditText)findViewById(R.id.often_edit_text);
                int freq= Integer.parseInt(frequencyView.getText().toString());

                //For avg cost of each cigarette
                EditText costView=(EditText)findViewById(R.id.cost_edit_text);
                float cost=Float.parseFloat(costView.getText().toString());

                //For Interest
                String interest=" ";
                CheckBox int1=(CheckBox) findViewById(R.id.interest_1);
                CheckBox int2=(CheckBox) findViewById(R.id.interest_2);
                CheckBox int3=(CheckBox) findViewById(R.id.interest_3);
                CheckBox int4=(CheckBox) findViewById(R.id.interest_4);
                CheckBox int5=(CheckBox) findViewById(R.id.interest_5);
                if(int1.isChecked())
                {
                    interest+=int1.getText().toString()+" ";
                }
                if(int2.isChecked())
                {
                    interest+=int2.getText().toString()+" ";
                }
                if(int3.isChecked())
                {
                    interest+=int3.getText().toString()+" ";
                }
                if(int4.isChecked())
                {
                    interest+=int4.getText().toString()+" ";
                }
                if(int5.isChecked())
                {
                    interest+=int5.getText().toString()+" ";
                }


                //For marital status m=marriage
                RadioGroup rg1 = (RadioGroup)findViewById(R.id.m_status_group);
                String m_status = ((RadioButton)findViewById(rg1.getCheckedRadioButtonId())).getText().toString();

                //For Future Plans
                Spinner futurespinner = (Spinner)findViewById(R.id.future_spinner);
                String future = futurespinner.getSelectedItem().toString();

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
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query lastQuery = databaseReference.child("patient").orderByKey().limitToLast(1);
                lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        /*if(dataSnapshot==null)
                        {
                            id=1;
                        }
                        else
                        {
                            id = (dataSnapshot!=null)? (int) dataSnapshot.child("id").getValue() :1;
                            id++;
                        }*/
                    }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Handle possible errors.
                }
            });


                Entry patient=new Entry(name,age,sex,interest,med_history,contact,days,freq,cost,m_status,future,business,salary,formattedtime1,formattedDate1,id);
                mPatientDatabaseReference.push().setValue(patient);
                Intent i=new Intent(NewEntryActivity.this,MainActivity.class);
                startActivity(i);


            }
        });

    }
}
