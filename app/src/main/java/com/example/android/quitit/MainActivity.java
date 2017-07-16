package com.example.android.quitit;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ChildEventListener mChildEventListener;
    private DatabaseReference mPatientDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private ListView mPatientListView;
    private EntriesListAdapter mPatientAdapter;
    private ArrayList<Entry> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mPatientDatabaseReference=mFirebaseDatabase.getReference().child("patient");


        mPatientListView=(ListView) findViewById(R.id.listView);

        patientList=new ArrayList<Entry>();
        mPatientAdapter=new EntriesListAdapter(MainActivity.this,R.layout.list_item,patientList);
       // View emptyView=findViewById(R.id.empty_view);
        // mPatientListView.setEmptyView(emptyView);
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
}
