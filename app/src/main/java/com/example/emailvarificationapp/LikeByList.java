package com.example.emailvarificationapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LikeByList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_by_list);
        setTitle("Liked By");
        ListView listoflikes = (ListView)findViewById(R.id.listoflikes);
        ArrayList arrayList = (ArrayList<String>) getIntent().getSerializableExtra("list");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listoflikes.setAdapter(arrayAdapter);

    }
}
