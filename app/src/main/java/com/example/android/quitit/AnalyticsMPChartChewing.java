package com.example.android.quitit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
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
    private PieChart combinedMenWomenPieChart,diseaseChart;
    private ArrayList<PieEntry> MenWomenPieChartEnteries;
    private BarData chewingData;
    private BarChart smokingBarMaleFemaleGraph;
    private BarData smokingMaleFemaleData;
    private ArrayList<PieEntry> diseaseEntries;
    private float barWidth = 0.3f;
    private float groupSpace = 0.4f;
    private float barSpace = 0.00f; // x4 DataSet
    private ScrollView chartLayout;
    private LinearLayout mEmptyPatientLayout;
    private ImageView mEmptyPatientImage;
    private TextView mEmptyPatientTextView1;
    private TextView mEmptyPatientTextView2;
    private boolean isDataPresent;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_mpchart_chewing);
        isDataPresent = false;
        ageArray = new int[70];
        menAgeArray = new int[70];
        womenAgeArray = new int[70];
        barEntries = new ArrayList<BarEntry>();
        womenEntries = new ArrayList<BarEntry>();
        menEntries = new ArrayList<BarEntry>();
        smokingXAxis = new ArrayList<String>();
        MenWomenPieChartEnteries = new ArrayList<PieEntry>();
        diseaseEntries = new ArrayList<PieEntry>();
        patientList = this.getIntent().getExtras().getParcelableArrayList("ARRAYLIST");

        //genderSwitch = (SwitchCompat) findViewById(R.id.genderSwitch);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        chartLayout = (ScrollView) findViewById(R.id.chartLayout);
        mEmptyPatientLayout = (LinearLayout) findViewById(R.id.empty_layout);
        mEmptyPatientImage = (ImageView) findViewById(R.id.empty_image_view);
        mEmptyPatientTextView1 = (TextView) findViewById(R.id.empty_textView_1);
        mEmptyPatientTextView2= (TextView) findViewById(R.id.empty_textView_2);

        combinedMenWomenPieChart = (PieChart) findViewById(R.id.pieChartMenWomen);
        combinedMenWomenPieChart.setUsePercentValues(true);
        combinedMenWomenPieChart.setDrawHoleEnabled(true);
        combinedMenWomenPieChart.setHoleColor(Color.TRANSPARENT);
        combinedMenWomenPieChart.setHoleRadius(7);
        combinedMenWomenPieChart.setTransparentCircleRadius(10);

        combinedMenWomenPieChart.setRotationAngle(0);
        combinedMenWomenPieChart.setRotationEnabled(true);


        chewingBarGraph = (BarChart) findViewById(R.id.smokingBarGraph);
        smokingBarMaleFemaleGraph = (BarChart) findViewById(R.id.smokingBarMaleFemaleGraph);

        for(int i = 0;i<patientList.size();i++){
            if(patientList.get(i).getChew_freq() != 0){
                isDataPresent = true;
                break;
            }
        }
        if(isDataPresent) {
            chartLayout.setVisibility(View.VISIBLE);
            mEmptyPatientTextView1.setVisibility(View.GONE);
            mEmptyPatientTextView2.setVisibility(View.GONE);
            mEmptyPatientImage.setVisibility(View.GONE);
            mEmptyPatientLayout.setVisibility(View.GONE);
            addEntry();
            final BarDataSet smokingSet = new BarDataSet(barEntries, "Percent people");
            final BarDataSet menSmokingSet = new BarDataSet(menEntries, "Percent of men");
            final BarDataSet womenSmokingSet = new BarDataSet(womenEntries, "Percent of women");
            menSmokingSet.setColor(Color.parseColor("#03A9F4"));
            womenSmokingSet.setColor(Color.parseColor("#0D47A1"));
            chewingBarGraph.getDescription().setEnabled(false);

            chewingData = new BarData(smokingSet);
            smokingMaleFemaleData = new BarData(menSmokingSet,womenSmokingSet);
            final BarData combinedSmokingData = new BarData(menSmokingSet,womenSmokingSet);

            ArrayList<Integer> colorList = new ArrayList<Integer>();
            colorList.add(Color.DKGRAY);
            colorList.add(Color.LTGRAY);
            colorList.add(Color.GRAY);

            ArrayList<LegendEntry> pieChartLegend = new ArrayList<LegendEntry>();
            LegendEntry entry1 = new LegendEntry();
            LegendEntry entry2 = new LegendEntry();
            entry1.formColor = colorList.get(0);
            entry2.formColor = colorList.get(1);
            entry1.label = "Men";
            entry2.label = "Women";
            pieChartLegend.add(entry1);
            pieChartLegend.add(entry2);

            combinedMenWomenPieChart = (PieChart) findViewById(R.id.pieChartMenWomen);
            combinedMenWomenPieChart.setUsePercentValues(true);
            combinedMenWomenPieChart.setDrawHoleEnabled(true);
            combinedMenWomenPieChart.setHoleColor(Color.TRANSPARENT);
            combinedMenWomenPieChart.setHoleRadius(7);
            combinedMenWomenPieChart.setTransparentCircleRadius(10);
            combinedMenWomenPieChart.setRotationAngle(0);
            combinedMenWomenPieChart.setRotationEnabled(true);

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
            data.setValueTextColor(Color.BLACK);
            combinedMenWomenPieChart.setData(data);
            combinedMenWomenPieChart.highlightValues(null);
            combinedMenWomenPieChart.invalidate();
            combinedMenWomenPieChart.getDescription().setEnabled(false);

            ArrayList<LegendEntry> diseaseChartLegend = new ArrayList<LegendEntry>();
            LegendEntry diseaseentry1 = new LegendEntry();
            LegendEntry diseaseentry2 = new LegendEntry();
            LegendEntry diseaseentry3 = new LegendEntry();
            diseaseentry1.formColor = colorList.get(0);
            diseaseentry2.formColor = colorList.get(1);
            diseaseentry3.formColor = colorList.get(2);
            diseaseentry1.label = "Diabetes";
            diseaseentry2.label = "High Blood Pressure";
            diseaseentry3.label = "Heart Problem";
            diseaseChartLegend.add(diseaseentry1);
            diseaseChartLegend.add(diseaseentry2);
            diseaseChartLegend.add(diseaseentry3);

            diseaseChart = (PieChart) findViewById(R.id.diseaseChart);
            diseaseChart.setUsePercentValues(true);
            diseaseChart.setDrawHoleEnabled(true);
            diseaseChart.setHoleColor(Color.TRANSPARENT);
            diseaseChart.setHoleRadius(7);
            diseaseChart.setTransparentCircleRadius(10);
            diseaseChart.setRotationAngle(0);
            diseaseChart.setRotationEnabled(true);

            Legend leg = diseaseChart.getLegend();
            leg.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
            leg.setXEntrySpace(7);
            leg.setYEntrySpace(5);
            leg.setCustom(diseaseChartLegend);

            PieDataSet diseaseDataSet = new PieDataSet(diseaseEntries,"Men");
            diseaseDataSet.setColors(colorList);

            PieData diseaseData = new PieData(diseaseDataSet);
            
          diseaseData.setValueFormatter(new PercentFormatter());
            diseaseData.setValueTextSize(11f);
            diseaseData.setValueTextColor(Color.BLACK);
            diseaseChart.setData(diseaseData);
            diseaseChart.highlightValues(null);
            diseaseChart.invalidate();
            diseaseChart.getDescription().setEnabled(false);

            XAxis xaxis = chewingBarGraph.getXAxis();
            xaxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

            Legend legend = chewingBarGraph.getLegend();
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

            chewingBarGraph.getAxisLeft().setStartAtZero(true);
            chewingBarGraph.getAxisRight().setStartAtZero(true);
            chewingBarGraph.getXAxis().setAxisMinimum(0);
            chewingBarGraph.setFitBars(true);
            //chewingData.setBarWidth(1.0f);
            chewingBarGraph.setData(chewingData);
            //chewingBarGraph.setData();

            chewingBarGraph.setTouchEnabled(true);
            chewingBarGraph.setDragEnabled(true);
            chewingBarGraph.setScaleEnabled(true);

            smokingMaleFemaleSet();
        }
        else{
            chartLayout.setVisibility(View.GONE);
            mEmptyPatientTextView1.setVisibility(View.VISIBLE);
            mEmptyPatientTextView2.setVisibility(View.VISIBLE);
            mEmptyPatientImage.setVisibility(View.VISIBLE);
            mEmptyPatientLayout.setVisibility(View.VISIBLE);
        }
    }

    private void smokingMaleFemaleSet() {
        smokingBarMaleFemaleGraph.setDescription(null);
        smokingBarMaleFemaleGraph.setPinchZoom(false);
        smokingBarMaleFemaleGraph.setScaleEnabled(false);
        smokingBarMaleFemaleGraph.setDrawBarShadow(false);
        smokingBarMaleFemaleGraph.setDrawGridBackground(false);
        smokingBarMaleFemaleGraph.setData(smokingMaleFemaleData);
        smokingBarMaleFemaleGraph.getBarData().setBarWidth(barWidth);
        smokingBarMaleFemaleGraph.getXAxis().setAxisMinimum(0);
        smokingBarMaleFemaleGraph.getXAxis().setAxisMaximum(0 + smokingBarMaleFemaleGraph.getBarData().getGroupWidth(groupSpace, barSpace) * smokingXAxis.size());
        smokingBarMaleFemaleGraph.groupBars(0, groupSpace, barSpace);
        smokingBarMaleFemaleGraph.getData().setHighlightEnabled(false);
        smokingBarMaleFemaleGraph.setNoDataText("No Chart Data Available");
        smokingBarMaleFemaleGraph.invalidate();

        Legend l = smokingBarMaleFemaleGraph.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        //X-axis
        XAxis xAxis = smokingBarMaleFemaleGraph.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(smokingXAxis.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(smokingXAxis));
        //Y-axis
        smokingBarMaleFemaleGraph.getAxisRight().setEnabled(false);
        YAxis leftAxis = smokingBarMaleFemaleGraph.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = smokingBarMaleFemaleGraph.getAxisRight();
        rightAxis.setEnabled(true);
        rightAxis.setValueFormatter(new LargeValueFormatter());
        rightAxis.setDrawGridLines(true);
        rightAxis.setSpaceTop(35f);
        rightAxis.setAxisMinimum(0f);
    }

    private void addEntry(){
        int years,years1,years2,menCount = 0,womenCount = 0,count = 0,diabetesCount = 0,bpCount = 0,heartProblemCount = 0;
        float menPercent,womenPercent,diabetesPercent,bpPercent,heartProblemPercent;
        for(int i = 0;i<patientList.size();i++){
            if(patientList.get(i).getChew_freq()!=0) {
                count++;
                years = (patientList.get(i).getChew_history()) / 365;
                //Log.e("error","" + years);
                ageArray[patientList.get(i).getAge() - years]++;
                String[] diseaseList = patientList.get(i).getMed_history().split(",");
                for(int j = 0;j<diseaseList.length;j++){
                    if(diseaseList[j].equalsIgnoreCase("Diabetes"))
                        diabetesCount++;
                    else if(diseaseList[j].equalsIgnoreCase("Heart Problem")){
                        heartProblemCount++;
                    }
                    else if(diseaseList[j].equalsIgnoreCase("High BloodPressure"))
                        bpCount++;
                }
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
        menPercent = (menCount/(float)(menCount + womenCount))*100;
        womenPercent = (womenCount/(float)(menCount + womenCount))*100;

        diabetesPercent = ((diabetesCount)/(float)(menCount + womenCount))*100;
        bpPercent = ((bpCount)/(float)(menCount + womenCount))*100;
        heartProblemPercent = ((heartProblemCount)/(float)(menCount + womenCount))*100;

        MenWomenPieChartEnteries.add(new PieEntry(menPercent,0));
        MenWomenPieChartEnteries.add(new PieEntry(womenPercent,1));

        diseaseEntries.add(new PieEntry(diabetesPercent,0));
        diseaseEntries.add(new PieEntry(bpPercent,1));
        diseaseEntries.add(new PieEntry(heartProblemPercent,2));

        for(int i = 0;i<70;i++){
            //Log.e("error",""+(ageArray[i]/patientList.size()) * 100);
            //points[i] = new DataPoint(i,(ageArray[i]/(double)patientList.size()) * 100);
            //points2[i] = new DataPoint(i,(chewingArray[i]/(double)patientList.size()) * 100);
            if(ageArray[i]!=0)
                barEntries.add(new BarEntry(i,(ageArray[i]/(float)count) * 100));
            if(menAgeArray[i]!=0 || womenAgeArray[i]!=0) {
                float menDivision = (menAgeArray[i] / (float) menCount) * 100;

//                if(menDivision == womenDivision){
//                    menDivision = menDivision * 2;
//                }
                smokingXAxis.add(String.valueOf(i));
                //Log.e("Men","" + i);
                menEntries.add(new BarEntry(i, menDivision));
            }
            if(womenAgeArray[i] != 0){
                float womenDivision = (womenAgeArray[i] / (float) womenCount) * 100;
                smokingXAxis.add(String.valueOf(i));
                womenEntries.add(new BarEntry(i, womenDivision));
            }
        }
    }
}
