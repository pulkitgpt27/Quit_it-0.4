package com.example.android.quitit;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener{

    private ChildEventListener mChildEventListener;
    private DatabaseReference mPatientDatabaseReference;
    private ListView mPatientListView;
    private EntriesListAdapter mPatientAdapter;
    private ArrayList<Entry> patientList;
    private ProgressBar spinner;
    private Boolean empty;
    SearchManager searchManager;
    SearchView searchView;
    private TextView usernameTxt,emailTxt;
    private ImageView userImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.AppThemeNoBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main); //changed due to navbar;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        usernameTxt = (TextView) headerLayout.findViewById(R.id.usernameTxt);
        emailTxt = (TextView) headerLayout.findViewById(R.id.emailTxt);
        userImageView = (ImageView) headerLayout.findViewById(R.id.imageView);

        usernameTxt.setText(getIntent().getStringExtra("displayName"));
        emailTxt.setText(getIntent().getStringExtra("displayEmail"));
        if(getIntent().getStringExtra("displayImage")!=null) {
            Picasso.with(this).load(Uri.parse(getIntent().getStringExtra("displayImage"))).into(userImageView);
        }



                //******************FIREBASE BEGINS HERE*******************
        empty=true;
        spinner=(ProgressBar) findViewById(R.id.spinner);


        //firebase reference

        mPatientDatabaseReference=FirebaseMethods.getFirebaseReference("doctors");

        //setting list view
        mPatientListView=(ListView) findViewById(R.id.listView);
        patientList=new ArrayList<Entry>();


        //fetching data from firebase
        firebaseDataFetch();

        mPatientListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,ReportActivity.class);
                Entry temp = patientList.get(i);
                Bundle B = new Bundle();
                B.putParcelable("ClickedEntry", (Parcelable) temp);
                intent.putExtras(B);
                startActivity(intent);
            }
        });

        FloatingActionButton newEntryFab = (FloatingActionButton) findViewById(R.id.fab);

        newEntryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewEntryActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_activity_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                return true;
            case R.id.search_icon:
                searchView = (SearchView)MenuItemCompat.getActionView(item);
                searchView.setSubmitButtonEnabled(true);
                searchView.setOnQueryTextListener(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    //2 mothods for searching
    @Override
    public boolean onQueryTextSubmit(String newText) {
      //  newText = newText.toLowerCase();
       // ArrayList<Entry> temp = new ArrayList<>();
       // for(Entry e: patientList){
       //     String name = e.getName().toLowerCase();
        //    if(name.contains(newText))
        //        temp.add(e);
       // }3
       // mPatientAdapter.setFilter(temp);
       return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Entry> temp = new ArrayList<>();
        for(Entry e: patientList){
            String name = e.getName().toLowerCase();
            if(name.contains(newText))
                temp.add(e);
        }
        mPatientAdapter.setFilter(temp);
        return true;
    }

    //*********after the search, to reinflate*********
    SearchView.OnCloseListener closeListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            //mPatientAdapter.clear();
            mPatientAdapter=new EntriesListAdapter(MainActivity.this,R.layout.list_item,patientList);
            mPatientListView.setAdapter(mPatientAdapter);
            //mPatientAdapter.getFilter().filter("");
            return true;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.basisOfSmoking:
                Intent intent = new Intent(MainActivity.this, AnalyticsMPChartSmoking.class);
                Bundle args = new Bundle();
                args.putParcelableArrayList("ARRAYLIST", patientList);
                args.putString("ChartType","Smoking");
                intent.putExtras(args);
                startActivity(intent);
                return true;
            case R.id.basisOfChewing:
                intent = new Intent(MainActivity.this, AnalyticsMPChartChewing.class);
                args = new Bundle();
                args.putParcelableArrayList("ARRAYLIST", patientList);
                args.putString("ChartType","Chewing");
                intent.putExtras(args);
                startActivity(intent);
                return true;
            case R.id.logout:
                AuthUI.getInstance().signOut(this);
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //**************UTILITY METHODS***************
    public void firebaseDataFetch()
    {
        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Doctor doctor = dataSnapshot.getValue(Doctor.class);
                Log.d("findme", "doc_id: "+ doctor.getMail_id()+"net_id"+FirebaseMethods.getUserId());
                if(doctor.getMail_id().equals(FirebaseMethods.getUserId())){
                    HashMap<String,Entry> patients=doctor.getPatients();
                    if(patients!=null) {
                        Set<String> ks = patients.keySet();
                        for (String key : ks) {
                            patientList.add(patients.get(key));
                        }
                    }
                    mPatientAdapter=new EntriesListAdapter(MainActivity.this,R.layout.list_item,patientList);
                    mPatientListView.setAdapter(mPatientAdapter);

                if(!patientList.isEmpty())
                {
                    spinner.setVisibility(View.GONE);
                }
                    return;
                }
//                patientList.add(patient);
//
//                if(!patientList.isEmpty())
//                {
//                    spinner.setVisibility(View.GONE);
//                }
//                mPatientAdapter=new EntriesListAdapter(MainActivity.this,R.layout.list_item,patientList);
//                mPatientListView.setAdapter(mPatientAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mPatientDatabaseReference.addChildEventListener(mChildEventListener);
    }
}