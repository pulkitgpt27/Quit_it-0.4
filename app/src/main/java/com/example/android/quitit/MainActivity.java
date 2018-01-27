package com.example.android.quitit;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.example.android.quitit.FirebaseMethods.getUserId;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener{

    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private DatabaseReference mDoctorsDatabaseReference;
    private ListView mPatientListView;
    private LinearLayout mEmptyPatientLayout;
    private ImageView mEmptyPatientImage;
    private TextView mEmptyPatientTextView1;
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

    //private boolean found=false;
    //private static int doctorCount = 0;
    public static String currentdoctorKey;
    private GoogleApiClient mGoogleApiClient;

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
        patientList=new ArrayList<Entry>();
        allPatients=new ArrayList<Entry>();
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
        mDoctorsDatabaseReference=FirebaseMethods.getFirebaseReference("doctors");
        //setting list view
        mPatientListView=(ListView) findViewById(R.id.listView);
        mEmptyPatientLayout = (LinearLayout) findViewById(R.id.empty_layout);
        mEmptyPatientImage = (ImageView) findViewById(R.id.empty_image_view);
        mEmptyPatientTextView1 = (TextView) findViewById(R.id.empty_textView_1);
        mEmptyPatientTextView2= (TextView) findViewById(R.id.empty_textView_2);

        /// /fetching data from firebase
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
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
                return true;
            case R.id.basisOfChewing:
                intent = new Intent(MainActivity.this, AnalyticsMPChartChewing.class);
                args = new Bundle();
                args.putParcelableArrayList("ARRAYLIST", allPatients);
                args.putString("ChartType","Chewing");
                intent.putExtras(args);
                startActivity(intent);
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
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDoctorsDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
        //mPatientDatabaseReference.addChildEventListener(mChildEventListener);
    }
    //********************************************

    //****************SEARCH METHODS**************
    //2 mothods for searching
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
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void deletepatient(Entry patient ) {
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
}