package com.example.android.quitit;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ChildEventListener mChildEventListener;
    private DatabaseReference mPatientDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private ListView mPatientListView;
    private EntriesListAdapter mPatientAdapter;
    private ArrayList<Entry> patientList;
    private ProgressBar spinner;
    private Boolean empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        empty=true;
        setContentView(R.layout.activity_main);
        spinner=(ProgressBar) findViewById(R.id.spinner);
        final View emptyView=findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);

        //firebase reference
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mPatientDatabaseReference=mFirebaseDatabase.getReference().child("patient");


        //setting list view
        mPatientListView=(ListView) findViewById(R.id.listView);
        patientList=new ArrayList<Entry>();
        Log.e("myLog","size"+patientList.size());

        //setting adapter
        mPatientAdapter=new EntriesListAdapter(MainActivity.this,R.layout.list_item,patientList);
        mPatientListView.setAdapter(mPatientAdapter);





        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Entry patient = dataSnapshot.getValue(Entry.class);
                patientList.add(patient);

                if(!patientList.isEmpty())
                {
                    spinner.setVisibility(View.GONE);
                    empty=false;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                return  true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
