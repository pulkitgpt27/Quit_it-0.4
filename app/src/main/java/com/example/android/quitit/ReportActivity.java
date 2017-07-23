package com.example.android.quitit;

/**
 * Created by Pulkit on 03-07-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.y;

/**
 * Created by Ayush vaid on 02-07-2017.
 */

public class ReportActivity extends AppCompatActivity {

    Entry ClickedEntry;
    String disp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Bundle B = this.getIntent().getExtras();
        ClickedEntry = B.getParcelable("ClickedEntry");

        //patient name
        TextView Name = (TextView) findViewById(R.id.nameView);
        Name.setText(ClickedEntry.getName());

        //patient gender
        TextView Sex = (TextView) findViewById(R.id.sexView);
        Sex.setText(ClickedEntry.getSex());

        //patient age
        TextView Age = (TextView) findViewById(R.id.ageView);
        Age.setText(Integer.toString(ClickedEntry.getAge()));

        //patient business
        TextView business = (TextView) findViewById(R.id.bussinessView);
        business.setText(ClickedEntry.getBusiness());



        //patient marital status
        TextView marry=(TextView) findViewById(R.id.marrital_statusView);
        marry.setText(ClickedEntry.getMarry_status());

        //how much patient smoke in a day
        TextView cigarettes_day=(TextView) findViewById(R.id.consumptionView);
        cigarettes_day.setText(Integer.toString(ClickedEntry.getSmoke_freq()));

        //for fraction of salary
        float spent=(ClickedEntry.getSmoke_freq())*30*((float)(ClickedEntry.getCost()));
        float earn=(float) (ClickedEntry.getSalary());
        float save=earn-spent;
        float percent=((float)spent/earn)*100;

        disp=Float.toString(percent)+"%";
        TextView fSpenView=(TextView) findViewById(R.id.fractionSpentView);
        fSpenView.setText(disp);
        Button printButton = (Button) findViewById(R.id.print_report_button);
        printButton.setOnClickListener(new Button.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               printDocument(v);
                                           }
                                       }
        );
    }

    public void printDocument(View view)
    {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) +
                " Document";

        printManager.print(jobName, new
                        MyPrintDocumentAdapter(this,ClickedEntry.getName(),ClickedEntry.getSex(),ClickedEntry.getAge(),ClickedEntry.getBusiness(),ClickedEntry.getMarry_status(),ClickedEntry.getSmoke_freq(),disp),
                null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:
                deletepatient();
                Intent i = new Intent(ReportActivity.this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.view_all:
                Intent intent = new Intent(ReportActivity.this,ViewActivity.class);
                //public Entry(String name,int age,String sex,String interest,String med,String contact,int days,int freq,float cost,String marry_status,String future,String business,int salary,String time,String date)
                Entry temp = new Entry(ClickedEntry.getName(),ClickedEntry.getAge(),ClickedEntry.getSex(),ClickedEntry.getInterest(),ClickedEntry.getMed_history(),ClickedEntry.getContact(),ClickedEntry.getSmokeHistory(),ClickedEntry.getSmoke_freq(),ClickedEntry.getCost(),ClickedEntry.getMarry_status(),ClickedEntry.getFuture(),ClickedEntry.getBusiness(),ClickedEntry.getSalary(),ClickedEntry.getTime(),ClickedEntry.getFormattedDate(),ClickedEntry.getId());
                Bundle B = new Bundle();
                B.putParcelable("ClickedEntry", (Parcelable) temp);
                intent.putExtras(B);
                startActivity(intent);
            case R.id.update:
                Intent intent2 = new Intent(ReportActivity.this,NewEntryActivity.class);
                //public Entry(String name,int age,String sex,String interest,String med,String contact,int days,int freq,float cost,String marry_status,String future,String business,int salary,String time,String date)
                Entry temp2 = new Entry(ClickedEntry.getName(),ClickedEntry.getAge(),ClickedEntry.getSex(),ClickedEntry.getInterest(),ClickedEntry.getMed_history(),ClickedEntry.getContact(),ClickedEntry.getSmokeHistory(),ClickedEntry.getSmoke_freq(),ClickedEntry.getCost(),ClickedEntry.getMarry_status(),ClickedEntry.getFuture(),ClickedEntry.getBusiness(),ClickedEntry.getSalary(),ClickedEntry.getTime(),ClickedEntry.getFormattedDate(),ClickedEntry.getId());
                Bundle B1 = new Bundle();
                B1.putParcelable("ClickedEntry", (Parcelable) temp2);
                intent2.putExtras(B1);
                startActivity(intent2);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deletepatient() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query nameQuery = ref.child("patient").orderByChild("name").equalTo(ClickedEntry.getName());

        nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
}
