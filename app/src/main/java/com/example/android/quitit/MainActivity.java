package com.example.android.quitit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ChildEventListener mChildEventListener;
    private DatabaseReference mPatientDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private ListView mPatientListView;
    private EntriesListAdapter mPatientAdapter;
    private ArrayList<Entry> patientList;

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

        if(drawer!=null)
            drawer.setDrawerListener(toggle);
        else
            Log.e("GADBAD","DRAWER IS NULL");

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //******************FIREBASE BEGINS HERE********************
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mPatientDatabaseReference=mFirebaseDatabase.getReference().child("patient");


        mPatientListView=(ListView) findViewById(R.id.listView);

        patientList=new ArrayList<Entry>();
        mPatientAdapter=new EntriesListAdapter(MainActivity.this,R.layout.list_item,patientList);

        //View emptyView=findViewById(R.id.empty_view);
        //mPatientListView.setEmptyView(emptyView);

        mPatientListView.setAdapter(mPatientAdapter);

        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Entry patient = dataSnapshot.getValue(Entry.class);
                patientList.add(patient);
                mPatientAdapter=new EntriesListAdapter(MainActivity.this,R.layout.list_item,patientList);
                mPatientListView.setAdapter(mPatientAdapter);
               // mPatientAdapter.add(patient);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
