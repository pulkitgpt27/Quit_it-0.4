package com.example.android.quitit;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by kakrya on 8/11/2017.
 */

public class Analytics extends AppCompatActivity {

    //private LineGraphSeries<DataPoint> series1,series2;
    private BarGraphSeries<DataPoint> series1,series2;
    private ArrayList<Entry> patientList;
    private DataPoint[] points,points2;
    EditText inputText;
    GraphView smokingGraph,chewingGraph;
    private int[] ageArray,chewingArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytics_layout);
        ageArray = new int[70];
        chewingArray = new int[70];

        patientList = this.getIntent().getExtras().getParcelableArrayList("ARRAYLIST");
        //points1 = new DataPoint[patientList.size()];
        //points2 = new DataPoint[patientList.size()];
        points = new DataPoint[70];
        points2 = new DataPoint[70];
        smokingGraph = (GraphView) findViewById(R.id.smokingGraph);
        chewingGraph = (GraphView) findViewById(R.id.chewingGraph);
        addEntry();

        //For smoking Graph
        //series1 = new LineGraphSeries<>(points1);
        series1 = new BarGraphSeries<DataPoint>(points);
        series2 = new BarGraphSeries<DataPoint>(points2);

        /*smokingGraph.getViewport().setMinX(1);
        smokingGraph.getViewport().setMaxX(100);
        smokingGraph.getViewport().setMinY(0);
        smokingGraph.getViewport().setMaxY(50);

        smokingGraph.getViewport().setYAxisBoundsManual(true);
        smokingGraph.getViewport().setXAxisBoundsManual(true);
        smokingGraph.getViewport().setScalable(true);
        smokingGraph.getViewport().setScrollable(true);
        smokingGraph.addSeries(series1);*/

        //For Chewing Graph
        /*series2 = new LineGraphSeries<>(points2);

        chewingGraph.getViewport().setMinX(1);
        chewingGraph.getViewport().setMaxX(100);
        chewingGraph.getViewport().setMinY(0);
        chewingGraph.getViewport().setMaxY(50);

        chewingGraph.getViewport().setYAxisBoundsManual(true);
        chewingGraph.getViewport().setXAxisBoundsManual(true);
        chewingGraph.getViewport().setScalable(true);
        chewingGraph.getViewport().setScrollable(true);
        chewingGraph.addSeries(series2);*/
        series1.setSpacing(50);
        series1.setDrawValuesOnTop(true);
        series1.setValuesOnTopColor(Color.RED);

        series2.setSpacing(50);
        series2.setDrawValuesOnTop(true);
        series2.setValuesOnTopColor(Color.RED);

        smokingGraph.getViewport().setScalable(true);
        //smokingGraph.getViewport().setScrollable(true);
        smokingGraph.getViewport().setScalableY(true);
        //smokingGraph.getViewport().setScrollableY(true);*/
        smokingGraph.getViewport().setXAxisBoundsManual(true);
        smokingGraph.getViewport().setMinX(5);
        smokingGraph.getViewport().setMaxX(70);
        smokingGraph.getViewport().setYAxisBoundsManual(true);
        smokingGraph.getViewport().setMinY(0);
        smokingGraph.getViewport().setMaxY(100);
        //smokingGraph.getViewport().setScrollable(true);
        //smokingGraph.getViewport().setScrollableY(true);

        chewingGraph.getViewport().setScalable(true);
        //smokingGraph.getViewport().setScrollable(true);
        chewingGraph.getViewport().setScalableY(true);
        //smokingGraph.getViewport().setScrollableY(true);*/
        chewingGraph.getViewport().setXAxisBoundsManual(true);
        chewingGraph.getViewport().setMinX(10);
        chewingGraph.getViewport().setMaxX(70);
        chewingGraph.getViewport().setYAxisBoundsManual(true);
        chewingGraph.getViewport().setMinY(0.5);
        chewingGraph.getViewport().setMaxY(100);

        smokingGraph.addSeries(series1);
        chewingGraph.addSeries(series2);
    }
    private void addEntry(){
        /*Collections.sort(patientList, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return (new Integer(o1.getAge()).compareTo(new Integer(o2.getAge())));
            }
        });

        double percentSmoke = 0,totalSmoke = 0,totalChew = 0,percentChew = 0;
        for(int i = 0;i<patientList.size();i++){

            //Log.e("error", String.valueOf(patientList.get(i).getAge()   ));
            //points[i] = new DataPoint(patientList.get(i).getAge(),patientList.get(i).getChew_freq());
            if(patientList.get(i).getAge() > input ){
                if(patientList.get(i).getSmoke_freq() > 0){
                    totalSmoke = totalSmoke + 1;
                }
                if(patientList.get(i).getChew_freq() > 0){
                    totalChew = totalChew + 1;
                }
            }
        }
        percentSmoke = (totalSmoke/patientList.size()) * 100;
        percentChew = (totalChew/patientList.size()) * 100;
        Log.e("error",percentSmoke + " " + percentChew);
        for(int i = 0;i<patientList.size();i++){
            points1[i] = new DataPoint(i,percentSmoke);
            points2[i] = new DataPoint(i,percentChew);
        }*/
        int years,years2;
        for(int i = 0;i<patientList.size();i++){
            years = (patientList.get(i).getSmokeHistory())/365;
            ageArray[patientList.get(i).getAge() - years]++;

            years2 = (patientList.get(i).getChew_history())/365;
            chewingArray[patientList.get(i).getAge() - years2]++;

        }
        for(int i = 0;i<70;i++){
            Log.e("error",""+(ageArray[i]/patientList.size()) * 100);
            points[i] = new DataPoint(i,(ageArray[i]/(double)patientList.size()) * 100);
            points2[i] = new DataPoint(i,(chewingArray[i]/(double)patientList.size()) * 100);
        }
    }
}
