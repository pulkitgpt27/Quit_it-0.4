package com.example.android.quitit;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

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

        //******************FIREBASE BEGINS HERE*******************
        empty=true;
        spinner=(ProgressBar) findViewById(R.id.spinner);


        //firebase reference

        mPatientDatabaseReference=FirebaseMethods.getFirebaseReference("patient");

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
       // }
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
            case R.id.basisOfAge:
                Intent intent = new Intent(MainActivity.this, Analytics.class);
                Bundle args = new Bundle();
                args.putParcelableArrayList("ARRAYLIST", patientList);
                intent.putExtras(args);
                startActivity(intent);
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
                Entry patient = dataSnapshot.getValue(Entry.class);
                patientList.add(patient);

                if(!patientList.isEmpty())
                {
                    spinner.setVisibility(View.GONE);
                }
                mPatientAdapter=new EntriesListAdapter(MainActivity.this,R.layout.list_item,patientList);
                mPatientListView.setAdapter(mPatientAdapter);
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