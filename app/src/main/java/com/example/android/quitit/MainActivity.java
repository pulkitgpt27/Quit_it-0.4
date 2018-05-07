package com.example.android.quitit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import static com.example.android.quitit.FirebaseMethods.getUserId;
import static com.example.android.quitit.Utility.isNetworkAvailable;
import static com.example.android.quitit.Utility.sortByValues;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener{

    private ValueEventListener mValueEventListener;
    private DatabaseReference mDoctorsDatabaseReference;
    private DatabaseReference mPatientDatabaseRefernce;
    private ListView mPatientListView;
    private LinearLayout mEmptyPatientLayout;
    private ImageView mEmptyPatientImage;
    private TextView mEmptyPatientTextView1;
    private TextView emptyTextView;
    private TextView mEmptyPatientTextView2;
    private EntriesListAdapter mPatientAdapter;
    private ArrayList<Entry> patientList;
    private ArrayList<Entry> allPatients;
    private ProgressBar spinner;
    private Boolean empty;
    SearchManager searchManager;
    SearchView searchView;
    private TextView usernameTxt,emailTxt;
    private ImageView userImageView;
    private static final int PATIENT_LOADER_ID = 1;
    private Patient patient;
    private FirebaseAuth mAuth;
    private View list_of_all_Enteries;
    private View patient_home;
    private MenuItem doctorAnalysis;
    private MenuItem patientViewDetails;
    private MenuItem patientReport;
    private FloatingActionButton newEntryFab;
    private NavigationView navigationView;
    private View headerLayout;
    private Menu menu;
    private MenuItem search_icon;
    private MenuInflater inflater;
    private DrawerLayout drawer;
    private  TextView smoke_tv;
    private  TextView chew_tv;
    public static String currentdoctorKey;
    private GoogleApiClient mGoogleApiClient;
    private String USER;
    private LineChart lifeExpectancyChart,lifeExpectancyChart2;
    private String lifeExpectancyChartXAxis[],lifeExpectancyChart2XAxis[];
    private ArrayList<com.github.mikephil.charting.data.Entry> lifeExpectancyChartYAxis,lifeExpectancyChart2YAxis;
    private TextView money_tv;
    private TextView life_tv;
    private TextView sal_tv;
    private TextView data_unavailable_tv;
    private TextView data_unavailable_tv2;


    private ImageView previous_month_iv;
    private ImageView next_month_iv;
    private ImageView data_unavailable_iv;

    private Calendar cal;
    private SimpleDateFormat month_date;
    private String month_name;
    private TextView cig_tv;
    MultiSwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.AppThemeNoBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main); //changed due to navbar;
        //code changes for refresh
         swipeRefreshLayout =(MultiSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
         swipeRefreshLayout.setSwipeableChildren(R.id.listView,R.id.empty_list);
         swipeRefreshLayout.setColorSchemeResources(R.color.magnitude1,R.color.magnitude2,R.color.magnitude7);
         swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 swipeRefreshLayout.setRefreshing(true);
                 (new Handler()).postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         swipeRefreshLayout.setRefreshing(false);
                         finish();
                         startActivity(getIntent());
                     }
                 },3000);
             }
         });
        //end
        list_of_all_Enteries = (View) findViewById(R.id.include_list_of_all_Entries);
        patient_home = (View) findViewById(R.id.include_patient_home);
        spinner=(ProgressBar) findViewById(R.id.spinner);

        lifeExpectancyChart = (LineChart) findViewById(R.id.LifeExpectancyChart);
        lifeExpectancyChart2 = (LineChart) findViewById(R.id.LifeExpectancyChart2);
        lifeExpectancyChartYAxis = new ArrayList<com.github.mikephil.charting.data.Entry>();
        lifeExpectancyChart2YAxis = new ArrayList<com.github.mikephil.charting.data.Entry>();

        data_unavailable_tv = (TextView) findViewById(R.id.data_unavailable_textView_1);
        data_unavailable_tv2 = (TextView) findViewById(R.id.data_unavailable_textview_2);
        data_unavailable_iv = (ImageView) findViewById(R.id.data_unavailable_image_view);

        empty = true;

        cal=Calendar.getInstance();
        month_date = new SimpleDateFormat("MMMM");
        cal.add(Calendar.MONTH,-1);
        month_name = month_date.format(cal.getTime());

        //setting list view
        mPatientListView = (ListView) findViewById(R.id.listView);
        mEmptyPatientLayout = (LinearLayout) findViewById(R.id.empty_layout);
        mEmptyPatientImage = (ImageView) findViewById(R.id.empty_image_view);
        mEmptyPatientTextView1 = (TextView) findViewById(R.id.empty_textView_1);
        mEmptyPatientTextView2 = (TextView) findViewById(R.id.empty_textView_2);
        /// /fetching data from firebase

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        patientList=new ArrayList<Entry>();
        allPatients=new ArrayList<Entry>();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
        doctorAnalysis = menu.findItem(R.id.doctorAnalysis);
        patientViewDetails = menu.findItem(R.id.patientViewDetails);
        patientReport = menu.findItem(R.id.patientReport);

        previous_month_iv = (ImageView) findViewById(R.id.previous_month);
        next_month_iv = (ImageView) findViewById(R.id.next_month);

        usernameTxt = (TextView) headerLayout.findViewById(R.id.usernameTxt);
        emailTxt = (TextView) headerLayout.findViewById(R.id.emailTxt);
        userImageView = (ImageView) headerLayout.findViewById(R.id.imageView);

        if(getIntent().getStringExtra("displayImage")!=null) {
            Picasso.with(this).load(Uri.parse(getIntent().getStringExtra("displayImage"))).into(userImageView);
        }

        usernameTxt.setText(getIntent().getStringExtra("displayName"));
        emailTxt.setText(getIntent().getStringExtra("displayEmail"));
        emptyTextView = (TextView) findViewById(R.id.empty_view);


        if(isNetworkAvailable(getBaseContext())){
            spinner.setVisibility(View.VISIBLE);
        }
        else {
            spinner.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet_connection);
        }

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
            return;

        FirebaseDatabase.getInstance().getReference().child("usertype").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserType userType = dataSnapshot.getValue(UserType.class);

                if (userType!=null && userType.getType().equals("Patient")) {
                    mPatientDatabaseRefernce = FirebaseDatabase.getInstance().getReference().child("patients").child(mAuth.getCurrentUser().getUid());
                    initiatePatientHome(mAuth.getCurrentUser().getUid());
                } else {
                    mDoctorsDatabaseReference = FirebaseMethods.getFirebaseReference("doctors");
                    initiateDoctorHome();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    protected  void initiatePatientHome(String uid){
        //Alarm for notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar= Calendar.getInstance();
        //calendar.set(Calendar.HOUR_OF_DAY,22);//set time here
        calendar.set(Calendar.MINUTE,02);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,broadcast);
        //***************

        final TextView cur_month_tv=(TextView) findViewById(R.id.month_name_tv);
        //setting month
        cal=Calendar.getInstance();
        month_date = new SimpleDateFormat("MMMM");
        month_name = month_date.format(cal.getTime());
        cur_month_tv.setText(month_name);
        //end
        cig_tv = (TextView) findViewById(R.id.cigarette_txt);
        smoke_tv = (TextView) findViewById(R.id.smoke_avg_tv);
        chew_tv = (TextView) findViewById(R.id.chew_avg_tv);
        money_tv =(TextView) findViewById(R.id.money_txt);
        life_tv = (TextView) findViewById(R.id.life_txt);
        sal_tv = (TextView) findViewById(R.id.sal_txt);

        cig_tv.setVisibility(View.GONE);
        sal_tv.setVisibility(View.GONE);
        life_tv.setVisibility(View.GONE);
        smoke_tv.setVisibility(View.GONE);
        chew_tv.setVisibility(View.GONE);
        money_tv.setVisibility(View.GONE);

        firebasePatientDataFetch();

        list_of_all_Enteries.setVisibility(View.GONE);
        patient_home.setVisibility(View.VISIBLE);
        doctorAnalysis.setVisible(false);
        patientViewDetails.setVisible(true);
        patientReport.setVisible(true);

        if(search_icon!=null) {
            search_icon.setVisible(false);
        }

        previous_month_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.add(Calendar.MONTH, -1);
                month_name = month_date.format(cal.getTime());

                Calendar currMonthCal = Calendar.getInstance();
                String curr_month = month_date.format(currMonthCal.getTime());

                if (!curr_month.equals(month_name)) {
                    final boolean[] smoker_data_load = {false};
                    final boolean[] chewer_data_load = {false};
                    FirebaseDatabase.getInstance().getReference().child("doctors").child(patient.getDoctor_key()).child("patients").child(patient.getEntry_key()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Entry ObjEntry = dataSnapshot.getValue(Entry.class);
                            if (!ObjEntry.getSmokeText().isEmpty()) {
                                FirebaseDatabase.getInstance().getReference().child("patients").child(mAuth.getCurrentUser().getUid()).
                                        child("monthlydata").child(month_name).child("day_map_smoke").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //dataSnapshot.getChildren();
                                        HashMap<String, Long> smoking_days_value = (HashMap<String, Long>) dataSnapshot.getValue();
                                        if(smoking_days_value != null) {
                                            smoke_tv.setVisibility(View.VISIBLE);
                                            chew_tv.setVisibility(View.VISIBLE);
                                            smoker_data_load[0] = true;
                                            data_unavailable_iv.setVisibility(View.GONE);
                                            data_unavailable_tv.setVisibility(View.GONE);
                                            data_unavailable_tv2.setVisibility(View.GONE);
                                            lifeExpectancyChart.setVisibility(View.VISIBLE);

                                            cur_month_tv.setText(month_name);
                                            drawSmokeGraph(smoking_days_value);
                                        }
                                        else {
                                            smoker_data_load[0] = false;
                                            //lifeExpectancyChart.setVisibility(View.GONE);
                                            showToast();
                                            cur_month_tv.setText(month_name);
                                            dataUnavailableVisible(smoker_data_load[0],chewer_data_load[0]);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                            if (!ObjEntry.getChewText().isEmpty()) {
                                FirebaseDatabase.getInstance().getReference().child("patients").child(mAuth.getCurrentUser().getUid()).
                                        child("monthlydata").child(month_name).child("day_map_chew").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //dataSnapshot.getChildren();
                                        HashMap<String, Long> chewing_days_value = (HashMap<String, Long>) dataSnapshot.getValue();
                                        if(chewing_days_value != null) {
                                            chewer_data_load[0] = true;
                                            smoke_tv.setVisibility(View.VISIBLE);
                                            chew_tv.setVisibility(View.VISIBLE);
                                            cur_month_tv.setText(month_name);
                                            lifeExpectancyChart2.setVisibility(View.VISIBLE);
                                            //create graph here using ObjectEntry and smoking_days_value
                                            data_unavailable_iv.setVisibility(View.GONE);
                                            data_unavailable_tv.setVisibility(View.GONE);
                                            data_unavailable_tv2.setVisibility(View.GONE);
                                            drawChewGraph(chewing_days_value);
                                        }
                                        else {
                                            chewer_data_load[0] = false;
                                            //lifeExpectancyChart2.setVisibility(View.GONE);
                                            cur_month_tv.setText(month_name);
                                            dataUnavailableVisible(smoker_data_load[0],chewer_data_load[0]);
                                            showToast();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else{
                    cal.add(Calendar.MONTH, 1);
                }
            }
        });

        next_month_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal=Calendar.getInstance();
                //cal.add(Calendar.MONTH, -1);
                month_date = new SimpleDateFormat("MMMM");
                month_name = month_date.format(cal.getTime());

                if(cur_month_tv.getText().toString().equals(month_name)){
                    //current month
                    month_name = month_date.format(cal.getTime());
                    FirebaseDatabase.getInstance().getReference().child("doctors").child(patient.getDoctor_key()).child("patients").child(patient.getEntry_key()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //draw graph using patient object
                            if(patient != null){
                                if(patient.getDay_map_smoke()!=null)
                                    drawSmokeGraphWithInt(patient.getDay_map_smoke());
                                if(patient.getDay_map_chew()!=null)
                                    drawChewGraphWithInt(patient.getDay_map_chew());
                                Toast.makeText(getBaseContext(),"Cant go into the future, \n You may have quit by then. ;) ",Toast.LENGTH_LONG).show();
                                
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else{
                    month_name = cur_month_tv.getText().toString();
                    cal=Calendar.getInstance();
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("MMMM",Locale.ENGLISH).parse(month_name);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    cal.setTime(date);
                    cal.add(Calendar.MONTH,1);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM"); // 01-12
                    cur_month_tv.setText(outputFormat.format(cal.getTime()));
                    month_name = outputFormat.format(cal.getTime());

                    Calendar cal_temp = Calendar.getInstance();
                    String live_month = outputFormat.format(cal_temp.getTime());

                    if(live_month.equals(month_name)){
                        FirebaseDatabase.getInstance().getReference().child("doctors").child(patient.getDoctor_key()).child("patients").child(patient.getEntry_key()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //draw graph using patient object
                                if(patient != null){
                                    if(patient.getDay_map_smoke()!=null) {
                                        drawSmokeGraphWithInt(patient.getDay_map_smoke());
                                        data_unavailable_iv.setVisibility(View.GONE);
                                        data_unavailable_tv.setVisibility(View.GONE);
                                        data_unavailable_tv2.setVisibility(View.GONE);
                                        smoke_tv.setVisibility(View.VISIBLE);
                                        chew_tv.setVisibility(View.VISIBLE);
                                        lifeExpectancyChart.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        data_unavailable_iv.setVisibility(View.VISIBLE);
                                        data_unavailable_tv.setVisibility(View.VISIBLE);
                                        data_unavailable_tv2.setVisibility(View.VISIBLE);
                                        smoke_tv.setVisibility(View.GONE);
                                        chew_tv.setVisibility(View.GONE);
                                        lifeExpectancyChart.setVisibility(View.GONE);
                                    }
                                    if(patient.getDay_map_chew()!=null) {
                                        drawChewGraphWithInt(patient.getDay_map_chew());
                                        data_unavailable_iv.setVisibility(View.GONE);
                                        data_unavailable_tv.setVisibility(View.GONE);
                                        data_unavailable_tv2.setVisibility(View.GONE);
                                        smoke_tv.setVisibility(View.VISIBLE);
                                        chew_tv.setVisibility(View.VISIBLE);
                                        lifeExpectancyChart2.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        data_unavailable_iv.setVisibility(View.VISIBLE);
                                        data_unavailable_tv.setVisibility(View.VISIBLE);
                                        data_unavailable_tv2.setVisibility(View.VISIBLE);
                                        smoke_tv.setVisibility(View.GONE);
                                        chew_tv.setVisibility(View.GONE);
                                        lifeExpectancyChart2.setVisibility(View.GONE);
                                    }
                                    //showToast();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                    else{
                        final boolean[] smoker_data_load = {false};
                        final boolean[] chewer_data_load = {false};
                        FirebaseDatabase.getInstance().getReference().child("doctors").child(patient.getDoctor_key()).child("patients").child(patient.getEntry_key()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Entry ObjEntry = dataSnapshot.getValue(Entry.class);
                                if(!ObjEntry.getSmokeText().isEmpty())
                                    FirebaseDatabase.getInstance().getReference().child("patients").child(mAuth.getCurrentUser().getUid()).
                                            child("monthlydata").child(month_name).child("day_map_smoke").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //dataSnapshot.getChildren();
                                            HashMap<String, Long> smoking_days_value = (HashMap<String, Long>) dataSnapshot.getValue();
                                            if (smoking_days_value != null) {
                                                smoker_data_load[0] = true;
                                                drawSmokeGraph(smoking_days_value);
                                                smoke_tv.setVisibility(View.VISIBLE);
                                                chew_tv.setVisibility(View.VISIBLE);
                                                data_unavailable_iv.setVisibility(View.GONE);
                                                data_unavailable_tv.setVisibility(View.GONE);
                                                data_unavailable_tv2.setVisibility(View.GONE);
                                                lifeExpectancyChart.setVisibility(View.VISIBLE);
                                            } else {
                                                smoker_data_load[0] = false;
                                                lifeExpectancyChart.setVisibility(View.GONE);
                                                showToast();
                                                dataUnavailableVisible(smoker_data_load[0],chewer_data_load[0]);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                if(!ObjEntry.getChewText().isEmpty()) {
                                    FirebaseDatabase.getInstance().getReference().child("patients").child(mAuth.getCurrentUser().getUid()).
                                            child("monthlydata").child(month_name).child("day_map_chew").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //dataSnapshot.getChildren();
                                            HashMap<String, Long> chewing_days_value = (HashMap<String, Long>) dataSnapshot.getValue();
                                            if (chewing_days_value != null) {
                                                //create graph here using ObjectEntry and smoking_days_value
                                                chewer_data_load[0] = true;
                                                smoke_tv.setVisibility(View.VISIBLE);
                                                chew_tv.setVisibility(View.VISIBLE);
                                                drawChewGraph(chewing_days_value);
                                                data_unavailable_iv.setVisibility(View.GONE);
                                                data_unavailable_tv.setVisibility(View.GONE);
                                                data_unavailable_tv2.setVisibility(View.GONE);
                                                lifeExpectancyChart2.setVisibility(View.VISIBLE);
                                            } else {
                                                chewer_data_load[0] = false;
                                                lifeExpectancyChart2.setVisibility(View.GONE);
                                                showToast();
                                                dataUnavailableVisible(smoker_data_load[0],chewer_data_load[0]);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }
        });
    }

    private void drawSmokeGraph(HashMap<String,Long> days)
    {
        /*if (days != null) {
            HashMap<String, Integer> smoking_int_map = new HashMap<String, Integer>();
            Set<String> ks = days.keySet();
            for (String key : ks) {
                smoking_int_map.put(key, Integer.parseInt(String.valueOf(days.get(key))));
            }
        }*/
        data_unavailable_iv.setVisibility(View.GONE);
        data_unavailable_tv.setVisibility(View.GONE);
        data_unavailable_tv2.setVisibility(View.GONE);
        lifeExpectancyChart.setVisibility(View.VISIBLE);
        //create graph here using ObjectEntry and smoking_days_value

            Set<String> temp = days.keySet();
            lifeExpectancyChartXAxis = new String[temp.size()];
            int j = 0;
            for (String s : temp) {
                lifeExpectancyChartXAxis[j] = s;
                j++;
            }
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date[] arrayOfDates = new Date[lifeExpectancyChartXAxis.length];
            for (int index = 0; index < lifeExpectancyChartXAxis.length; index++) {
                try {
                    arrayOfDates[index] = format.parse(lifeExpectancyChartXAxis[index]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Arrays.sort(arrayOfDates);
            for (int index = 0; index < lifeExpectancyChartXAxis.length; index++) {
                lifeExpectancyChartXAxis[index] = format.format(arrayOfDates[index]);
            }
            int i = 0;
            lifeExpectancyChartYAxis.clear();
            for (String s : lifeExpectancyChartXAxis) {
                lifeExpectancyChartYAxis.add(new com.github.mikephil.charting.data.Entry(i, days.get(s)));
                i++;
            }
            PopulateChart();
    }

    private void drawSmokeGraphWithInt(HashMap<String,Integer> days)
    {
        data_unavailable_iv.setVisibility(View.GONE);
        data_unavailable_tv.setVisibility(View.GONE);
        data_unavailable_tv2.setVisibility(View.GONE);
        lifeExpectancyChart.setVisibility(View.VISIBLE);
            Set<String> temp = days.keySet();
            lifeExpectancyChartXAxis = new String[temp.size()];
            int j = 0;
            for (String s : temp) {
                lifeExpectancyChartXAxis[j] = s;
                j++;
            }
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date[] arrayOfDates = new Date[lifeExpectancyChartXAxis.length];
            for (int index = 0; index < lifeExpectancyChartXAxis.length; index++) {
                try {
                    arrayOfDates[index] = format.parse(lifeExpectancyChartXAxis[index]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Arrays.sort(arrayOfDates);
            for (int index = 0; index < lifeExpectancyChartXAxis.length; index++) {
                lifeExpectancyChartXAxis[index] = format.format(arrayOfDates[index]);
            }
            int i = 0;
            lifeExpectancyChartYAxis.clear();
            for (String s : lifeExpectancyChartXAxis) {
                lifeExpectancyChartYAxis.add(new com.github.mikephil.charting.data.Entry(i, days.get(s)));
                i++;
            }
            PopulateChart();
    }

    private void drawChewGraph(HashMap<String,Long> days)
    {
        /*if (days != null) {
            HashMap<String, Integer> smoking_int_map = new HashMap<String, Integer>();
            Set<String> ks = days.keySet();
            for (String key : ks) {
                smoking_int_map.put(key, Integer.parseInt(String.valueOf(days.get(key))));
            }
        }*/
        //cur_month_tv.setText(month_name);

        data_unavailable_iv.setVisibility(View.GONE);
        data_unavailable_tv.setVisibility(View.GONE);
        data_unavailable_tv2.setVisibility(View.GONE);
        lifeExpectancyChart2.setVisibility(View.VISIBLE);
        //create graph here using ObjectEntry and smoking_days_value
            Set<String> temp = days.keySet();
            lifeExpectancyChart2XAxis = new String[temp.size()];
            int j = 0;
            for (String s : temp) {
                lifeExpectancyChart2XAxis[j] = s;
                j++;
            }
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date[] arrayOfDates = new Date[lifeExpectancyChart2XAxis.length];
            for (int index = 0; index < lifeExpectancyChart2XAxis.length; index++) {
                try {
                    arrayOfDates[index] = format.parse(lifeExpectancyChart2XAxis[index]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Arrays.sort(arrayOfDates);
            for (int index = 0; index < lifeExpectancyChart2XAxis.length; index++) {
                lifeExpectancyChart2XAxis[index] = format.format(arrayOfDates[index]);
            }
            int i = 0;
            lifeExpectancyChart2YAxis.clear();
            for (String s : lifeExpectancyChart2XAxis) {
                lifeExpectancyChart2YAxis.add(new com.github.mikephil.charting.data.Entry(i, days.get(s)));
                i++;
            }
            PopulateChewChart();
    }

    private void drawChewGraphWithInt(HashMap<String,Integer> days)
    {
        /*if (days != null) {
            HashMap<String, Integer> smoking_int_map = new HashMap<String, Integer>();
            Set<String> ks = days.keySet();
            for (String key : ks) {
                smoking_int_map.put(key, Integer.parseInt(String.valueOf(days.get(key))));
            }
        }*/
        //cur_month_tv.setText(month_name);
        data_unavailable_iv.setVisibility(View.GONE);
        data_unavailable_tv.setVisibility(View.GONE);
        data_unavailable_tv2.setVisibility(View.GONE);
        lifeExpectancyChart2.setVisibility(View.VISIBLE);
        //create graph here using ObjectEntry and smoking_days_value

            Set<String> temp = days.keySet();
            lifeExpectancyChart2XAxis = new String[temp.size()];
            int j = 0;
            for (String s : temp) {
                lifeExpectancyChart2XAxis[j] = s;
                j++;
            }
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date[] arrayOfDates = new Date[lifeExpectancyChart2XAxis.length];
            for (int index = 0; index < lifeExpectancyChart2XAxis.length; index++) {
                try {
                    arrayOfDates[index] = format.parse(lifeExpectancyChart2XAxis[index]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Arrays.sort(arrayOfDates);
            for (int index = 0; index < lifeExpectancyChart2XAxis.length; index++) {
                lifeExpectancyChart2XAxis[index] = format.format(arrayOfDates[index]);
            }
            int i = 0;
            lifeExpectancyChart2YAxis.clear();
            for (String s : lifeExpectancyChart2XAxis) {
                lifeExpectancyChart2YAxis.add(new com.github.mikephil.charting.data.Entry(i, days.get(s)));
                i++;
            }
            PopulateChewChart();
    }

    private void showToast() {
        Toast.makeText(this,"No Data Available!",Toast.LENGTH_SHORT).show();
    }

    protected void initiateDoctorHome(){
        patient_home.setVisibility(View.GONE);
        list_of_all_Enteries.setVisibility(View.VISIBLE);

        doctorAnalysis.setVisible(true);
        patientViewDetails.setVisible(false);
        patientReport.setVisible(false);

        firebaseDataFetch();
        mPatientListView.setEmptyView(emptyTextView);
        mPatientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                Entry temp = patientList.get(i);
                Bundle B = new Bundle();
                B.putParcelable("ClickedEntry", (Parcelable) temp);
                intent.putExtras(B);
                startActivity(intent);
                finish();
            }
        });

        newEntryFab = (FloatingActionButton) findViewById(R.id.fab);
        newEntryFab.setVisibility(View.VISIBLE);
        newEntryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewEntryActivity.class);
                startActivity(i);
                finish();
            }
        });

        if(search_icon!=null)
            search_icon.setVisible(true);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search_icon = menu.findItem(R.id.search_icon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_icon:
                searchView = (SearchView)MenuItemCompat.getActionView(item);
                searchView.setSubmitButtonEnabled(true);
                searchView.setOnQueryTextListener(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.basisOfSmoking:
                Intent intent = new Intent(MainActivity.this, AnalyticsMPChartSmoking.class);
                Bundle args = new Bundle();
                args.putParcelableArrayList("ARRAYLIST", allPatients);
                args.putString("ChartType","Smoking");
                intent.putExtras(args);
                startActivity(intent);
                finish();
                return true;
            case R.id.basisOfChewing:
                intent = new Intent(MainActivity.this, AnalyticsMPChartChewing.class);
                args = new Bundle();
                args.putParcelableArrayList("ARRAYLIST", allPatients);
                args.putString("ChartType","Chewing");
                intent.putExtras(args);
                startActivity(intent);
                finish();
                return true;
            case R.id.logout:
                currentdoctorKey=null;
                if(LoginActivity.isGmailSigned == false) {
                    AuthUI.getInstance().signOut(this);
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    if(mGoogleApiClient == null){
                        mGoogleApiClient.connect();
                    }
                    if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                        Log.e("Gmail",mGoogleApiClient.toString());
                        AuthUI.getInstance().signOut(this);
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                LoginActivity.isGmailSigned = false;
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }
                }
                return true;
            case R.id.patientViewDetails:
                if(patient != null)
                    FirebaseDatabase.getInstance().getReference().child("doctors").child(patient.getDoctor_key()).child("patients").child(patient.getEntry_key()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Entry temp = dataSnapshot.getValue(Entry.class);
                            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                            Bundle B = new Bundle();
                            B.putParcelable("ClickedEntry", (Parcelable) temp);
                            intent.putExtras(B);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                return true;
            case R.id.patientReport:
                if(patient != null)
                FirebaseDatabase.getInstance().getReference().child("doctors").child(patient.getDoctor_key()).child("patients").child(patient.getEntry_key()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Entry temp = dataSnapshot.getValue(Entry.class);
                        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                        Bundle B = new Bundle();
                        B.putParcelable("ClickedEntry", (Parcelable) temp);
                        intent.putExtras(B);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //***************LOGOUT SOLUTION************
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }
    //******************************************

    //**************UTILITY METHODS***************
    public void firebaseDataFetch()
    {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Doctor currentDoctor = child.getValue(Doctor.class);
                    if (currentDoctor.getMail_id() != null) {
                        HashMap<String, Entry> patients = currentDoctor.getPatients();
                        if(currentDoctor.getMail_id().equals(getUserId())) {
                            currentdoctorKey = child.getKey();
                            found = true;
                            if (patients != null) {
                                patients=sortByValues(patients);
                                Set<String> ks = patients.keySet();
                                for (String key : ks) {
                                    patientList.add(patients.get(key));
                                    allPatients.add(patients.get(key));
                                }
                                mPatientAdapter = new EntriesListAdapter(MainActivity.this, R.layout.list_item, patientList);
                                mPatientListView.setAdapter(mPatientAdapter);
                                registerForContextMenu(mPatientListView);
                                Toast.makeText(getBaseContext(), "Patients loaded.", Toast.LENGTH_SHORT).show();
                            } else {
                                mPatientListView.setVisibility(View.GONE);
                                mEmptyPatientTextView1.setVisibility(View.VISIBLE);
                                mEmptyPatientTextView2.setVisibility(View.VISIBLE);
                                mEmptyPatientImage.setVisibility(View.VISIBLE);
                                mEmptyPatientLayout.setVisibility(View.VISIBLE);
                                Toast.makeText(getBaseContext(), "No Patients. Start by Adding some.", Toast.LENGTH_LONG).show();
                            }
                            spinner.setVisibility(View.GONE);
                        }
                        else{
                            if(patients!=null){
                                Set<String> ks = patients.keySet();
                                for (String key : ks) {
                                    allPatients.add(patients.get(key));
                                }
                            }
                        }
                    }
                }
                if(found == false){
                    for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                        if (user.getProviderId().equals("google.com")) {
                            //create new Doctor
                            mDoctorsDatabaseReference
                                    .push()
                                    .setValue(null, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError,
                                                               DatabaseReference databaseReference) {
                                            String uniqueKey = databaseReference.getKey();
                                            DatabaseReference UpdatePatientDatabaseReference = FirebaseDatabase.getInstance().getReference().child("doctors").child(uniqueKey);
                                            Doctor newDoctor = new Doctor(FirebaseMethods.getUserId());
                                            currentdoctorKey = uniqueKey;
                                            try {
                                                UpdatePatientDatabaseReference.setValue(newDoctor);
                                                mPatientListView.setVisibility(View.GONE);
                                                mEmptyPatientTextView1.setVisibility(View.VISIBLE);
                                                mEmptyPatientTextView2.setVisibility(View.VISIBLE);
                                                mEmptyPatientImage.setVisibility(View.VISIBLE);
                                                mEmptyPatientLayout.setVisibility(View.VISIBLE);
                                                spinner.setVisibility(View.GONE);
                                                Toast.makeText(getBaseContext(), "Welcome ,Doctor", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(getBaseContext(), "No Patients. Start by Adding some.", Toast.LENGTH_LONG).show();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            Log.e("Created", "New Doctor Added :" + newDoctor.getMail_id());
                                        }
                                    });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDoctorsDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
        //mPatientDatabaseReference.addChildEventListener(mChildEventListener);
    }
    //********************************************

    //********************************************
    public void firebasePatientDataFetch()
    {
        FirebaseDatabase.getInstance().getReference().child("patients").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Hello","for debug");
                patient = dataSnapshot.getValue(Patient.class);
                currentdoctorKey = patient.getDoctor_key();
                //getting entry
                dataSnapshot.getValue();
                if(patient!=null) {
                    FirebaseDatabase.getInstance().getReference().child("doctors").child(currentdoctorKey).child("patients").child(patient.getEntry_key()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //for average
                            Entry entry = dataSnapshot.getValue(Entry.class);
                            if (entry != null) {
                                if (!entry.getSmokeText().equals("")) {
                                    if (patient.getDay_map_smoke().size() != 0) {
                                        smoke_tv.setVisibility(View.VISIBLE);
                                        cig_tv.setVisibility(View.VISIBLE);
                                        float avg = 0;
                                        int sum = 0;
                                        Set<String> keys = patient.getDay_map_smoke().keySet();
                                        for (String key : keys) {
                                            sum += patient.getDay_map_smoke().get(key);
                                        }
                                        avg = (float) sum / patient.getDay_map_smoke().size();
                                        String s = String.format("%.2f", avg);
                                        cig_tv.setText(""+sum);
                                        smoke_tv.setText("" + s +" Cigs/day");
                                    }
                                } else {
                                    smoke_tv.setVisibility(View.GONE);
                                }
                                if (!entry.getChewText().equals("")) {
                                    if (!entry.getChewText().equals("")) {
                                        if (patient.getDay_map_chew().size() != 0) {
                                            chew_tv.setVisibility(View.VISIBLE);
                                            float avg = 0;
                                            int sum = 0;
                                            Set<String> keys = patient.getDay_map_chew().keySet();
                                            for (String key : keys) {
                                                sum += patient.getDay_map_chew().get(key);
                                            }
                                            avg = sum / patient.getDay_map_chew().size();
                                            String s = String.format("%.2f", avg);
                                            chew_tv.setText(""+s + " Packs/day");
                                        }
                                    }
                                }else {
                                    chew_tv.setVisibility(View.GONE);
                                }
                            }
                            //end
                            //for money
                            if(entry!=null)
                            {
                                if(!entry.getSmokeText().equals(""))
                                {
                                    money_tv.setVisibility(View.VISIBLE);
                                    float total_money=0;

                                    if (patient.getDay_map_smoke().size() != 0) {

                                        Set<String> keys = patient.getDay_map_smoke().keySet();
                                        for (String key : keys) {
                                            total_money += (patient.getDay_map_smoke().get(key)*entry.getSmoke_cost());
                                        }
                                    }
                                    money_tv.setText(""+total_money);
                                }
                            }
                            //end
                            //for minutes lost
                            if(entry!=null)
                            {
                                if (!entry.getSmokeText().equals("")) {
                                    life_tv.setVisibility(View.VISIBLE);
                                    if (patient.getDay_map_smoke().size() != 0) {
                                        smoke_tv.setVisibility(View.VISIBLE);
                                        int sum = 0;
                                        Set<String> keys = patient.getDay_map_smoke().keySet();
                                        for (String key : keys) {
                                            sum += patient.getDay_map_smoke().get(key);
                                        }
                                        life_tv.setText("" + (sum*9)+" mins");
                                    }
                                }
                            }
                            //end

                            //for salary
                            if(entry!=null)
                            {
                                if (!entry.getSmokeText().equals("")) {
                                    if (patient.getDay_map_smoke().size() != 0) {
                                        sal_tv.setVisibility(View.VISIBLE);
                                        float spent = 0;
                                        Set<String> keys = patient.getDay_map_smoke().keySet();
                                        for (String key : keys) {
                                            spent += (float)(patient.getDay_map_smoke().get(key)*entry.getSmoke_cost());
                                        }
                                        float earn=(float)(entry.getSalary());
                                        float fraction=(float)(spent/earn)*100;
                                        String s = String.format("%.2f", fraction);
                                        sal_tv.setText("" +s+" %");
                                    }
                                }
                            }
                            //end
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if(patient.getDay_map_smoke().size() > 0 && patient.getDay_map_chew().size() > 0)
                    {
                        lifeExpectancyChart.setVisibility(View.VISIBLE);
                        lifeExpectancyChart2.setVisibility(View.VISIBLE);
                        lifeExpectancyChart.getLayoutParams().height = dpToPx(400);
                        lifeExpectancyChart.requestLayout();
                        lifeExpectancyChart2.getLayoutParams().height = dpToPx(400);
                        lifeExpectancyChart2.requestLayout();
                    }
                    else if(patient.getDay_map_smoke().size() > 0)
                    {
                        lifeExpectancyChart.setVisibility(View.VISIBLE);
                        lifeExpectancyChart2.setVisibility(View.GONE);
                        lifeExpectancyChart.getLayoutParams().height = dpToPx(400);
                        lifeExpectancyChart.requestLayout();
                    }
                    else if(patient.getDay_map_chew().size() > 0)
                    {
                        lifeExpectancyChart2.setVisibility(View.VISIBLE);
                        lifeExpectancyChart.setVisibility(View.GONE);
                        lifeExpectancyChart2.getLayoutParams().height = dpToPx(400);
                        lifeExpectancyChart2.requestLayout();
                    }


                    if(patient.getDay_map_smoke().size() != 0) {
                        Set<String> temp = patient.getDay_map_smoke().keySet();
                        lifeExpectancyChartXAxis = new String[temp.size()];
                        int j = 0;
                        for (String s : temp) {
                            lifeExpectancyChartXAxis[j] = s;
                            j++;
                        }
                        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        Date[] arrayOfDates = new Date[lifeExpectancyChartXAxis.length];
                        for (int index = 0; index < lifeExpectancyChartXAxis.length; index++) {
                            try {
                                arrayOfDates[index] = format.parse(lifeExpectancyChartXAxis[index]);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        Arrays.sort(arrayOfDates);
                        for (int index = 0; index < lifeExpectancyChartXAxis.length; index++) {
                            lifeExpectancyChartXAxis[index] = format.format(arrayOfDates[index]);
                        }
                        int i = 0;
                        for (String s : lifeExpectancyChartXAxis) {
                            lifeExpectancyChartYAxis.add(new com.github.mikephil.charting.data.Entry(i, patient.getDay_map_smoke().get(s)));
                            i++;
                        }
                        PopulateChart();
                    }
                    if(patient.getDay_map_chew().size() != 0) {
                        Set<String> temp = patient.getDay_map_chew().keySet();
                        lifeExpectancyChart2XAxis = new String[temp.size()];
                        int j = 0;
                        for (String s : temp) {
                            lifeExpectancyChart2XAxis[j] = s;
                            j++;
                        }
                        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        Date[] arrayOfDates = new Date[lifeExpectancyChart2XAxis.length];
                        for (int index = 0; index < lifeExpectancyChart2XAxis.length; index++) {
                            try {
                                arrayOfDates[index] = format.parse(lifeExpectancyChart2XAxis[index]);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        Arrays.sort(arrayOfDates);
                        for (int index = 0; index < lifeExpectancyChart2XAxis.length; index++) {
                            lifeExpectancyChart2XAxis[index] = format.format(arrayOfDates[index]);
                        }
                        int i = 0;
                        for (String s : lifeExpectancyChart2XAxis) {
                            lifeExpectancyChart2YAxis.add(new com.github.mikephil.charting.data.Entry(i, patient.getDay_map_chew().get(s)));
                            i++;
                        }
                        PopulateChewChart();
                    }
                    if(patient.getDay_map_chew().size() == 0 && patient.getDay_map_smoke().size() == 0){
                        dataUnavailableVisible(false, false);
                    }
                }
                //by pulkit end
                spinner.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //mPatientDatabaseRefernce.addListenerForSingleValueEvent(mValueEventListener);
    }

    public int dpToPx(int dp) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    private void PopulateChewChart() {
        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(lifeExpectancyChart2YAxis, "Tobacco Consumption Frequency");
        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        // set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(false);

        XAxis xAxis = lifeExpectancyChart2.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return lifeExpectancyChart2XAxis[(int) value % lifeExpectancyChart2XAxis.length]; // xVal is a string array
            }
        });

        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45);

        lifeExpectancyChart2.setScaleEnabled(false);
        lifeExpectancyChart2.getDescription().setText("");

        LineData data = new LineData(set1);

        // set data
        lifeExpectancyChart2.setData(data);
        lifeExpectancyChart2.notifyDataSetChanged();
        lifeExpectancyChart2.invalidate();
    }

    private void PopulateChart() {
        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(lifeExpectancyChartYAxis, "Smoke Consumption Frequency");
        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        // set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(false);

        XAxis xAxis = lifeExpectancyChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return lifeExpectancyChartXAxis[(int) value%lifeExpectancyChartXAxis.length]; // xVal is a string array
            }
        });

        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45);

        lifeExpectancyChart.setScaleEnabled(false);
        lifeExpectancyChart.getDescription().setText("");

        LineData data = new LineData(set1);

        // set data
        lifeExpectancyChart.setData(data);
        lifeExpectancyChart.notifyDataSetChanged();
        lifeExpectancyChart.invalidate();
    }

    //****************SEARCH METHODS**************
    //2 methods for searching
    @Override
    public boolean onQueryTextSubmit(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<Entry> temp = new ArrayList<>();
        if(newText.equals("")){
            //mPatientAdapter.addThese(patientList);

            ArrayList<Entry> tempList = new ArrayList<Entry>();
            for(int i = 0;i<patientList.size();i++){
                tempList.add(patientList.get(i));
            }
            mPatientAdapter.clear();
            mPatientAdapter.addAll(tempList);
            mPatientAdapter.notifyDataSetChanged();
            patientList = tempList;
        }
        else{
            newText = newText.toLowerCase();
            for (Entry e : patientList){
                String name = e.getName().toLowerCase();
                if (name.contains(newText))
                    temp.add(e);
            }
            mPatientAdapter.clear();
            mPatientAdapter.addAll(temp);
            mPatientAdapter.notifyDataSetChanged();
        }
        return false;
    }
    //*********************************************

    public boolean onSupportNavigateUp() {
        Toast.makeText(this, "BackButton!", Toast.LENGTH_SHORT).show();
        return true;
    }

    //*****************Long press menu**************
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.editOpt:
                // add stuff here
                Intent intent2 = new Intent(MainActivity.this,UpdateActivity.class);
                Entry temp2 = patientList.get(info.position);
                Bundle B1 = new Bundle();
                B1.putParcelable("ClickedEntry", (Parcelable) temp2);
                intent2.putExtras(B1);
                startActivity(intent2);
                finish();
                return true;
            case R.id.deleteOpt:
                new AlertDialog.Builder(this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete? This record will be removed permanently from the Database.")
                        // .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                deletepatient(patientList.get(info.position));
                                Intent i = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void deletepatient(Entry patient) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query nameQuery = ref.child("doctors").child(MainActivity.currentdoctorKey).child("patients").orderByChild("name").equalTo(patient.getName());

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

    public void dataUnavailableVisible(boolean a, boolean b){
        if(!a && !b){
             smoke_tv.setVisibility(View.GONE);
            chew_tv.setVisibility(View.GONE);
            lifeExpectancyChart.setVisibility(View.GONE);
            lifeExpectancyChart2.setVisibility(View.GONE);
            data_unavailable_iv.setVisibility(View.VISIBLE);
            data_unavailable_tv.setVisibility(View.VISIBLE);
            data_unavailable_tv2.setVisibility(View.VISIBLE);
        }
    }
}