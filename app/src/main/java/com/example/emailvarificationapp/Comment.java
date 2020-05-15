package com.example.emailvarificationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class Comment extends AppCompatActivity {
    TextView addcom;
    String id;

    public void send(View view)
    {
        final String com =String.valueOf(addcom.getText());

        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("objectId",id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for (ParseObject object:objects)
                        {
                            if(object.getList("commentBy")==null)
                            {
                                List<String> emptyList1 = new ArrayList<>();
                                object.put("commentBy",emptyList1);
                            }
                            if(object.getList("comment")==null)
                            {
                                List<String> emptyList1 = new ArrayList<>();
                                object.put("comment",emptyList1);
                            }
                            ArrayList<String> testStringArrayList = (ArrayList<String>)object.get("commentBy");
                            ArrayList<String> testStringArrayList1 = (ArrayList<String>)object.get("comment");
                            String myString = ParseUser.getCurrentUser().getUsername();
                            testStringArrayList1.add(com);
                            object.put("comment",testStringArrayList1);
                            testStringArrayList.add(myString);
                            object.put("commentBy",testStringArrayList);
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null)
                                    {
                                        Toast.makeText(getApplicationContext(),"Commented",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setTitle("Comment");
        addcom = (TextView)findViewById(R.id.commentText);

       id = getIntent().getExtras().getString("id");
        ArrayList comment = (ArrayList<String>) getIntent().getSerializableExtra("comment");
        ArrayList commentBy = (ArrayList<String>) getIntent().getSerializableExtra("commentBy");
        CommentAdapter adapter=new CommentAdapter(this, commentBy, comment);
        ListView commentlist=(ListView)findViewById(R.id.commentlist);
        if(commentBy!=null)
        {
        commentlist.setAdapter(adapter);}
        else
        {
            Toast.makeText(getApplicationContext(),"No Comment Till Now Be The First One",Toast.LENGTH_SHORT).show();
        }
    }
}
