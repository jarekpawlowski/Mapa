package com.example.jarek.mymapapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by jarek on 12.01.2017.
 */

public class PlacesList extends ArrayAdapter<String> {

    private final Context context;
    private final String[] list;

    public PlacesList(Context context,
                      String [] values) {
        super(context, R.layout.list_places, values);
        this.context=context;
        this.list = values;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView= inflater.inflate(R.layout.list_places, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textRow);
        txtTitle.setText(list[position]);
        return rowView;
    }
}
