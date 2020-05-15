package com.example.emailvarificationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private ArrayList<List_Data> list_data;
    private GridView gridView;
    GridAdapter adapter;
    ArrayList<ArrayList<String>> commentBy=new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> commentlist =new ArrayList<ArrayList<String>>();
    ArrayList<String> id = new ArrayList<>();
    ArrayList<ArrayList<String>> likeby =new ArrayList<ArrayList<String>>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;

            default: return false;

        }}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("PROFILE");
        gridView=(GridView)findViewById(R.id.gridView);
        list_data=new ArrayList<>();

        getData();

        if(ParseUser.getCurrentUser()==null)
        {
            Toast.makeText(this,"You are Logout Please Signin back",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,LoginActivity.class));
        }

       // Toast.makeText(this,"" + "profile",Toast.LENGTH_SHORT).show();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(),Add.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),inside.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.users:
                        startActivity(new Intent(getApplicationContext(),users.class));
                        overridePendingTransition(0,0);
                        return true;
                }


                return false;
            }
        });

        GridView gridview = (GridView) findViewById(R.id.gridView);
       // gridview.setAdapter(new ImageAdapter(this));

    }
    private void getData() {

        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("Image");

        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null)
                {
                    // Toast.makeText(getApplicationContext(),String.valueOf(objects.size()),Toast.LENGTH_SHORT).show();
                    if(objects.size()>0)
                    {

                        for(ParseObject object : objects)
                        {
                            ParseFile file=(ParseFile)object.getParseFile("image");

                            List_Data listData=new List_Data(object.getString("Content"),file.getUrl());
                            list_data.add(listData);
                            ArrayList<String> testStringArrayList = (ArrayList<String>)object.get("likeby");
                            likeby.add(testStringArrayList);
                            ArrayList<String> testStringArrayList1 = (ArrayList<String>)object.get("commentBy");
                            commentBy.add(testStringArrayList1);
                            ArrayList<String> testStringArrayList2 = (ArrayList<String>)object.get("comment");
                            commentlist.add(testStringArrayList2);
                            id.add(object.getObjectId());
                            adapter.notifyDataSetChanged();
                        }

                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"ERROR : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }



            }
        });

        adapter=new GridAdapter(this,R.layout.grid_list,list_data, likeby, commentBy, commentlist, id);
        gridView.setAdapter(adapter);

    }}
  /*  List_Data listData=new List_Data(ob.getString("name"),ob.getString("imageurl"));
                        list_data.add(listData);
                                }
                                adapter=new GridAdapter(getApplicationContext(),R.layout.grid_list,list_data);
                                gridView.setAdapter(adapter);*/