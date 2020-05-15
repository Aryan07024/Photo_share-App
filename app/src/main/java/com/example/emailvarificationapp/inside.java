package com.example.emailvarificationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.Date;
import java.util.List;



public class inside extends AppCompatActivity {
     ArrayList<Date>postdate= new ArrayList<>();
     ArrayList<String> name=new ArrayList<>();
    ArrayList<String> caption=new ArrayList<>();
    ArrayList<String> photo =new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    ArrayList<ArrayList<String>> likeby =new ArrayList<ArrayList<String>>();
    ArrayList<Integer> likes = new ArrayList<>();
    ListView feed;
    MyAdapter adapter;
    ArrayList<ArrayList<String>> commentBy=new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> comment =new ArrayList<ArrayList<String>>();


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

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);
        setTitle("KNOW THE WORLD");

        if(ParseUser.getCurrentUser()==null)
        {
            Toast.makeText(this,"You are Logout Please Signin back",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,LoginActivity.class));
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.add:
                    startActivity(new Intent(getApplicationContext(),Add.class));
                    overridePendingTransition(0,0);
                    return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
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


        if(ParseUser.getCurrentUser()==null)
        {
            Toast.makeText(this,"You are Logout Please Signin back",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,LoginActivity.class));
        }

        if(ParseUser.getCurrentUser().get("isFollowing")==null)
        {
            List<String> emptyList = new ArrayList<>();
            ParseUser.getCurrentUser().put("isFollowing",emptyList);
        }


        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("Image");

        query.whereContainedIn("username",ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null)
                {

                    if(objects.size()>0)
                    {

                        for(ParseObject object : objects)
                        {
                            ParseFile file=(ParseFile)object.getParseFile("image");
                            photo.add(file.getUrl());


                            postdate.add(object.getDate("postdate"));
                           name.add(object.getString("username"));
                           caption.add(object.getString("Content"));
                           id.add(object.getObjectId());
                           likes.add(object.getInt("likes"));
                            ArrayList<String> testStringArrayList = (ArrayList<String>)object.get("likeby");
                            likeby.add(testStringArrayList);
                            ArrayList<String> testStringArrayList1 = (ArrayList<String>)object.get("commentBy");
                            commentBy.add(testStringArrayList1);
                            ArrayList<String> testStringArrayList2 = (ArrayList<String>)object.get("comment");
                            comment.add(testStringArrayList2);
                          // Toast.makeText(getApplicationContext(),object.getObjectId(),Toast.LENGTH_SHORT).show();

//                            Toast.makeText(getApplicationContext(),String.valueOf(id.size())+id.get(0),Toast.LENGTH_SHORT).show();
                          adapter.notifyDataSetChanged();
                        }
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"ERROR : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });

       // Toast.makeText(getApplicationContext(),id.size(),Toast.LENGTH_SHORT).show();

        adapter=new MyAdapter(this,postdate,name,photo, caption, id, likes, likeby, commentBy, comment);
        feed=(ListView)findViewById(R.id.listfeed);
        feed.setAdapter(adapter);
        feed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ckeck click","done"+position);
            }
        });


    }



}
