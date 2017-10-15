package com.example.android.quitit;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

public class AnalyticsMPChartChewing extends AppCompatActivity {

    private BarChart chewingBarGraph;
    private ArrayList<Entry> patientList;
    private int[] ageArray;
    private int[] menAgeArray;
    private int[] womenAgeArray;
    private ArrayList<BarEntry> barEntries;
    private ArrayList<String> smokingXAxis;
    private ArrayList<BarEntry> womenEntries;
    private ArrayList<BarEntry> menEntries;
    private PieChart combinedMenWomenPieChart,pieChart;
    private ArrayList<PieEntry> MenWomenPieChartEnteries;
    private SwitchCompat genderSwitch;
    private BarData chewingData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytics_layout_mpchart_smoking);
        ageArray = new int[70];
        menAgeArray = new int[70];
        womenAgeArray = new int[70];
        barEntries = new ArrayList<BarEntry>();
        womenEntries = new ArrayList<BarEntry>();
        menEntries = new ArrayList<BarEntry>();
        smokingXAxis = new ArrayList<String>();
        MenWomenPieChartEnteries = new ArrayList<PieEntry>();
        patientList = this.getIntent().getExtras().getParcelableArrayList("ARRAYLIST");

        genderSwitch = (SwitchCompat) findViewById(R.id.genderSwitch);

        combinedMenWomenPieChart = (PieChart) findViewById(R.id.pieChartMenWomen);
        combinedMenWomenPieChart.setUsePercentValues(true);
        combinedMenWomenPieChart.setDrawHoleEnabled(true);
        combinedMenWomenPieChart.setHoleColor(Color.TRANSPARENT);
        combinedMenWomenPieChart.setHoleRadius(7);
        combinedMenWomenPieChart.setTransparentCircleRadius(10);

        combinedMenWomenPieChart.setRotationAngle(0);
        combinedMenWomenPieChart.setRotationEnabled(true);


        chewingBarGraph = (BarChart) findViewById(R.id.smokingBarGraph);
        addEntry();
        final BarDataSet smokingSet = new BarDataSet(barEntries, "Percent people");
        final BarDataSet menSmokingSet = new BarDataSet(menEntries, "Percent of men");
        final BarDataSet womenSmokingSet = new BarDataSet(womenEntries, "Percent of women");
        menSmokingSet.setColor(Color.parseColor("#03A9F4"));
        womenSmokingSet.setColor(Color.parseColor("#0D47A1"));
        chewingBarGraph.getDescription().setEnabled(false);
        chewingData = new BarData(smokingSet);
        final BarData combinedSmokingData = new BarData(menSmokingSet,womenSmokingSet);

        ArrayList<Integer> colorList = new ArrayList<Integer>();
        colorList.add(Color.DKGRAY);
        colorList.add(Color.LTGRAY);
        ArrayList<LegendEntry> pieChartLegend = new ArrayList<LegendEntry>();
        LegendEntry entry1 = new LegendEntry();
        LegendEntry entry2 = new LegendEntry();
        entry1.formColor = colorList.get(0);
        entry2.formColor = colorList.get(1);
        entry1.label = "Men";
        entry2.label = "Women";
        pieChartLegend.add(entry1);
        pieChartLegend.add(entry2);


        PieDataSet pieDataSet = new PieDataSet(MenWomenPieChartEnteries,"Men");
        pieDataSet.setColors(colorList);

        Legend i = combinedMenWomenPieChart.getLegend();
        i.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        i.setXEntrySpace(7);
        i.setYEntrySpace(5);
        i.setCustom(pieChartLegend);

        PieData data = new PieData(pieDataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);
        combinedMenWomenPieChart.setData(data);
        combinedMenWomenPieChart.highlightValues(null);
        combinedMenWomenPieChart.invalidate();
        combinedMenWomenPieChart.getDescription().setEnabled(false);

        XAxis xaxis = chewingBarGraph.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        //chewingData.setBarWidth(1.0f);
        chewingBarGraph.setData(chewingData);
        //chewingBarGraph.setData();

        chewingBarGraph.setTouchEnabled(true);
        chewingBarGraph.setDragEnabled(true);
        chewingBarGraph.setScaleEnabled(true);


        genderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if(isChecked){
                    chewingData = new BarData(smokingSet,menSmokingSet,womenSmokingSet);
                    chewingBarGraph.setData(chewingData);
                }
                else{
                    chewingData = new BarData(smokingSet);
                    chewingBarGraph.setData(chewingData);
                }*/
                chewingBarGraph.setVisibility(View.GONE);
                if(isChecked){
                    chewingData = new BarData(smokingSet,menSmokingSet,womenSmokingSet);
                    chewingBarGraph.setData(chewingData);
                    chewingBarGraph.setVisibility(View.VISIBLE);
                }
                else{
                    chewingData = new BarData(smokingSet);
                    chewingBarGraph.setData(chewingData);
                    chewingBarGraph.setVisibility(View.VISIBLE);
                }
            }

        });
    }

    private void addEntry(){
        int years,years1,years2,menCount = 0,womenCount = 0,count = 0;
        float menPercent,womenPercent;
        for(int i = 0;i<patientList.size();i++){
            if(patientList.get(i).getChew_freq()!=0) {
                count++;
                years = (patientList.get(i).getChew_history()) / 365;
                //Log.e("error","" + years);
                ageArray[patientList.get(i).getAge() - years]++;
            }

            //years2 = (patientList.get(i).getChew_history())/365;
            //chewingArray[patientList.get(i).getAge() - years2]++;
            if(patientList.get(i).getSex().equals("Male") && patientList.get(i).getChew_freq() != 0){
                years1 = (patientList.get(i).getChew_history())/365;
                menAgeArray[patientList.get(i).getAge() - years1]++;
                menCount++;
            }
            else if(patientList.get(i).getSex().equals("Female") && patientList.get(i).getChew_freq() != 0){
                years2 = (patientList.get(i).getChew_history())/365;
                womenAgeArray[patientList.get(i).getAge() - years2]++;
                womenCount++;
            }
        }
        menPercent = (menCount/(float)patientList.size())*100;
        womenPercent = (womenCount/(float)patientList.size())*100;
        MenWomenPieChartEnteries.add(new PieEntry(menPercent,0));
        MenWomenPieChartEnteries.add(new PieEntry(womenPercent,1));
        for(int i = 0;i<70;i++){
            //Log.e("error",""+(ageArray[i]/patientList.size()) * 100);
            //points[i] = new DataPoint(i,(ageArray[i]/(double)patientList.size()) * 100);
            //points2[i] = new DataPoint(i,(chewingArray[i]/(double)patientList.size()) * 100);
            if(ageArray[i]!=0)
                barEntries.add(new BarEntry(i,(ageArray[i]/(float)count) * 100));
            if(menAgeArray[i]!=0 || womenAgeArray[i]!=0) {
                menEntries.add(new BarEntry(i, (menAgeArray[i] / (float) count) * 100));
                womenEntries.add(new BarEntry(i, (womenAgeArray[i] / (float) count) * 100));
            }
            smokingXAxis.add(String.valueOf(i));
        }
    }
}
