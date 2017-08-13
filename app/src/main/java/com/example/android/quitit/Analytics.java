package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kakrya on 8/11/2017.
 */

public class Analytics extends AppCompatActivity {

    private LineGraphSeries<DataPoint> series;
    private ArrayList<Entry> patientList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytics_layout);
        patientList = this.getIntent().getExtras().getParcelableArrayList("ARRAYLIST");
        GraphView smokingGraph = (GraphView) findViewById(R.id.smokingGraph);
        series = new LineGraphSeries<DataPoint>();
        addEntry();

    }
    private void addEntry(){
        Collections.sort(patientList, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return (new Integer(o1.getAge()).compareTo(new Integer(o2.getAge())));
            }
        });
        for(int i = 0;i<patientList.size();i++){
            series.appendData(new DataPoint(patientList.get(i).getAge(),patientList.get(i).getChew_freq()),true,100);
        }
    }
}
