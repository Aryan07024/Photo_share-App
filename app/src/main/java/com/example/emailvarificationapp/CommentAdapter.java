package com.example.emailvarificationapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter {
    private final Activity context;
    private final ArrayList<String> maintitle;
    private final ArrayList<String> subtitle;


    public CommentAdapter(Activity context, ArrayList<String> maintitle,ArrayList<String> subtitle) {
        super(context, R.layout.commentlay, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.commentlay, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.commentby);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.com);

        titleText.setText(maintitle.get(position));
        subtitleText.setText(subtitle.get(position));

        return rowView;

    };
}


