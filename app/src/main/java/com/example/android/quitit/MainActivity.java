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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
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
import java.util.Iterator;
import java.util.Map;
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
                if(LoginActivity.isGmailSigned == false) {
                    AuthUI.getInstance().signOut(this);
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    LoginActivity.isGmailSigned = false;
                    mGoogleApiClient.disconnect();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            finish();
                        }
                    });
                }
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
                if(doctor.getMail_id() != null && doctor.getMail_id().equals(FirebaseMethods.getUserId())){
                    HashMap<String,Entry> patients=doctor.getPatients();
                    if(patients != null) {
                        Set<String> ks = patients.keySet();
                        for (String key : ks) {
                            patientList.add(patients.get(key));
                        }
                    }
                    mPatientAdapter=new EntriesListAdapter(MainActivity.this,R.layout.list_item,patientList);
                    mPatientListView.setAdapter(mPatientAdapter);
                    registerForContextMenu(mPatientListView);
                    /*mPatientListView.setLongClickable(true);
                    mPatientListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.e("ListView","Long Clicked");
                            return true;
                        }
                    });*/

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
                return true;
            case R.id.deleteOpt:
                new AlertDialog.Builder(this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete? This record will be removed permanently from the Database.")
                        // .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                deletepatient(info.position);
                                Intent i = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(i);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        if(LoginActivity.isGmailSigned == true) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
        }
        super.onStart();
    }
    public void deletepatient(int position) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query nameQuery = ref.child("patient").orderByChild("name").equalTo(patientList.get(position).getName());

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
