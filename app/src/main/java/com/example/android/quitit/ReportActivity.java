package com.example.android.quitit;

/**
 * Created by Pulkit on 03-07-2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
}
