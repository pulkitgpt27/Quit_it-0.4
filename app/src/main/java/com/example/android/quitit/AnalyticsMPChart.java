package com.example.android.quitit;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by kakrya on 8/23/2017.
 */

public class AnalyticsMPChart extends AppCompatActivity {
    private BarChart smokingBarGraph;
    private BarChart combinedSmokingBarGraph;
    private ArrayList<Entry> patientList;
    private int[] ageArray;
    private int[] menAgeArray;
    private int[] womenAgeArray;
    private ArrayList<BarEntry> barEntries;
    private ArrayList<String> smokingXAxis;
    private ArrayList<BarEntry> womenEntries;
    private ArrayList<BarEntry> menEntries;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytics_layoutmpchart);
        ageArray = new int[70];
        menAgeArray = new int[70];
        womenAgeArray = new int[70];
        barEntries = new ArrayList<BarEntry>();
        womenEntries = new ArrayList<BarEntry>();
        menEntries = new ArrayList<BarEntry>();
        smokingXAxis = new ArrayList<String>();
        patientList = this.getIntent().getExtras().getParcelableArrayList("ARRAYLIST");

        smokingBarGraph = (BarChart) findViewById(R.id.smokingBarGraph);
        combinedSmokingBarGraph = (BarChart) findViewById(R.id.combinedSmokingBarGraph);
        addEntry();
        BarDataSet smokingSet = new BarDataSet(barEntries, "Percent of people started smoking age");
        BarDataSet menSmokingSet = new BarDataSet(menEntries, "Percent of men started smoking age");
        BarDataSet womenSmokingSet = new BarDataSet(womenEntries, "Percent of women started smoking age");
        menSmokingSet.setColor(Color.RED);
        womenSmokingSet.setColor(Color.BLUE);
        BarData smokingData = new BarData(smokingSet);
        BarData combinedSmokingData = new BarData(menSmokingSet,womenSmokingSet);

        XAxis xaxis = smokingBarGraph.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        XAxis xaxis1 = combinedSmokingBarGraph.getXAxis();
        xaxis1.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        //smokingData.setBarWidth(1.0f);
        smokingBarGraph.setData(smokingData);
        combinedSmokingBarGraph.setData(combinedSmokingData);

        smokingBarGraph.setTouchEnabled(true);
        smokingBarGraph.setDragEnabled(true);
        smokingBarGraph.setScaleEnabled(true);

        combinedSmokingBarGraph.setTouchEnabled(true);
        combinedSmokingBarGraph.setDragEnabled(true);
        combinedSmokingBarGraph.setScaleEnabled(true);
    }

    private void addEntry(){
        int years,years1,years2;
        for(int i = 0;i<patientList.size();i++){
            years = (patientList.get(i).getSmokeHistory())/365;
            Log.e("error","" + years);
            ageArray[patientList.get(i).getAge() - years]++;

            //years2 = (patientList.get(i).getChew_history())/365;
            //chewingArray[patientList.get(i).getAge() - years2]++;
            if(patientList.get(i).getSex().equals("Male")){
                years1 = (patientList.get(i).getSmokeHistory())/365;
                menAgeArray[patientList.get(i).getAge() - years1]++;
            }
            else if(patientList.get(i).getSex().equals("Female")){
                years2 = (patientList.get(i).getSmokeHistory())/365;
                womenAgeArray[patientList.get(i).getAge() - years2]++;
            }
        }
        for(int i = 0;i<70;i++){
            Log.e("error",""+(ageArray[i]/patientList.size()) * 100);
            //points[i] = new DataPoint(i,(ageArray[i]/(double)patientList.size()) * 100);
            //points2[i] = new DataPoint(i,(chewingArray[i]/(double)patientList.size()) * 100);
            if(ageArray[i]!=0)
                barEntries.add(new BarEntry(i,(ageArray[i]/(float)patientList.size()) * 100));
            if(menAgeArray[i]!=0 || womenAgeArray[i]!=0) {
                menEntries.add(new BarEntry(i, (menAgeArray[i] / (float) patientList.size()) * 100));
                womenEntries.add(new BarEntry(i, (womenAgeArray[i] / (float) patientList.size()) * 100));
            }
            smokingXAxis.add(String.valueOf(i));
        }
    }
}
