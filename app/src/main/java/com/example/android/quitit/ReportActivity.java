package com.example.android.quitit;



import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.print.PrintManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        //Receiving bundle
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
        float spent=(ClickedEntry.getSmoke_freq())*30*((float)(ClickedEntry.getSmoke_cost()));
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
                new AlertDialog.Builder(this)
                        .setTitle("Delete")
                        .setMessage("Do you really want to delete")
                       // .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                deletepatient();
                                Intent i = new Intent(ReportActivity.this, MainActivity.class);
                                startActivity(i);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            case R.id.view_all:
                Intent intent = new Intent(ReportActivity.this,ViewActivity.class);
              /*  Entry(String name,int age,String sex,String interest,String med,String contact,int chew_days,int chew_freq,float chew_cost,int smoke_days,int smoke_freq,float smoke_cost,String marry_status,
               String future,String business,int salary,String time,String date,
                    String morning_status,String family_status,String habit_reason,String habit,String aware_status,String aware_diseases,String quit_status,String quit_reason,String quit_before_status,String craving_time,int id){
                */

                Entry temp = ClickedEntry;
                Bundle B = new Bundle();

                //passing bundle
                B.putParcelable("ClickedEntry", (Parcelable) temp);
                intent.putExtras(B);
                startActivity(intent);
                return true;


            case R.id.update:
                Intent intent2 = new Intent(ReportActivity.this,UpdateActivity.class);
                Entry temp2 = ClickedEntry;
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
