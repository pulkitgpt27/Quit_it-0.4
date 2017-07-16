package com.example.android.quitit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ayush vaid on 13-06-2017.
 */
public class AllEntries extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.layout.list_of_all_entries);
    }

    /*************** this is where we pick the value up from database and Enter it into an ArrayList<Entry>
     *               Phir uske constructor call karenge jaise miwok mein hota tha and Name, Age and Date
     *               pass karenge constructor ko.
     *               Then uska EntriesListAdapter se combine kar ke List dispay karenge
     *
     *
     *
     *
     */
}
