package com.example.android.quitit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ayush vaid on 12-06-2017.
 */
public class EntriesListAdapter extends ArrayAdapter<Entry>{

    public EntriesListAdapter(Activity context, int resource, List<Entry> objects) {
        super(context,resource, objects);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_item, parent, false);
        }

        Entry current = getItem(position);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
        nameTextView.setText(current.getName());

        TextView ageTextView = (TextView) convertView.findViewById(R.id.age);
        ageTextView.setText(""+current.getAge());

        TextView sexTextView = (TextView) convertView.findViewById(R.id.sex);
        sexTextView.setText(current.getSex());

        TextView interestTextView = (TextView) convertView.findViewById(R.id.interest);
        interestTextView.setText(current.getInterest());

        return convertView;
    }
}
