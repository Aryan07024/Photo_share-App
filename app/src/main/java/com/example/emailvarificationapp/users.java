package com.example.emailvarificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class users extends AppCompatActivity {

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

    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setTitle("BOND");

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
       // Toast.makeText(this,"" + "User",Toast.LENGTH_SHORT).show();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.users);

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
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }


                return false;
            }
        });



       final ListView userlist =(ListView)findViewById(R.id.userlist);
        userlist.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked,users);
        userlist.setAdapter(arrayAdapter);

        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView) view;

                if(checkedTextView.isChecked()){
                   // Toast.makeText(getApplicationContext(),"row checked",Toast.LENGTH_SHORT).show();

                    ParseUser.getCurrentUser().getList("isFollowing").add(users.get(position));
                    ParseUser.getCurrentUser().saveInBackground();
                }else
                {


                    ParseUser.getCurrentUser().getList("isFollowing").remove(users.get(position));
                  ParseUser.getCurrentUser().saveInBackground();
                }

            }
        });

        users.clear();

        if(ParseUser.getCurrentUser()==null)
        {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        ParseQuery<ParseUser> query =ParseUser.getQuery();

        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size() >0)
                    {
                        for(ParseUser user:objects )
                        {
                            users.add(user.getUsername());
                        }
                        arrayAdapter.notifyDataSetChanged();

                        for (String username : users) {

                            if (ParseUser.getCurrentUser().getList("isFollowing").contains(username)) {

                                userlist.setItemChecked(users.indexOf(username), true);

                            }

                        }


                    }
                }
            }
        });

    }
}
