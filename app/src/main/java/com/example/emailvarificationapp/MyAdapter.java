package com.example.emailvarificationapp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAdapter extends ArrayAdapter {
   private final ArrayList<String> photo;
    private final  ArrayList<Date> postdate;
    private final ArrayList<String> name;
    private final ArrayList<String> caption;
    private final ArrayList<String> id;
    private final ArrayList<Integer> likes ;
    private final ArrayList<ArrayList<String>> likeby;
    private final ArrayList<ArrayList<String>> commentBy;
    private final ArrayList<ArrayList<String>> commentlist;
    
    private final Activity context;

    public MyAdapter(@NonNull Context context, ArrayList<Date> postdate, ArrayList<String> name, ArrayList<String> photo, ArrayList<String> caption, ArrayList<String> id, ArrayList<Integer> likes, ArrayList<ArrayList<String>> likeby, ArrayList<ArrayList<String>> commentBy, ArrayList<ArrayList<String>> commentlist) {
        super(context, R.layout.list_feed,name);
       this.photo = photo;
        this.postdate = postdate;
        this.name = name;
        this.context = (Activity) context;
        this.caption = caption;
        this.id = id;
        this.likes = likes;
        this.likeby = likeby;
        this.commentBy = commentBy;
        this.commentlist = commentlist;
    }

    public View getView(final int poition , View view, ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_feed,null,true);

        ImageView imageView=(ImageView) rowView.findViewById(R.id.post);
        TextView likedis = (TextView) rowView.findViewById(R.id.likedis);
         final ImageView likeimg =(ImageView) rowView.findViewById(R.id.likeimg);
        TextView poston =(TextView) rowView.findViewById(R.id.poston);
        TextView by =(TextView)rowView.findViewById(R.id.postby);
        TextView content =(TextView)rowView.findViewById(R.id.content);

        final Button comment =(Button)rowView.findViewById(R.id.comment);

        Picasso.with(getContext()).load(photo.get(poition)).into(imageView);
        poston.setText(String.valueOf(postdate.get(poition)));
        by.setText(name.get(poition));
        content.setText(caption.get(poition));
        likedis.setText(String.valueOf(likes.get(poition)));
        if(likeby.get(poition).contains(ParseUser.getCurrentUser().getUsername()))
        {
            likeimg.setImageResource(R.drawable.like);
        }
        else
        {
            likeimg.setImageResource(R.drawable.unlike);
        }


     rowView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {



             ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
             query.whereEqualTo("objectId",id.get(poition));
             query.findInBackground(new FindCallback<ParseObject>() {
                 @Override
                 public void done(List<ParseObject> objects, ParseException e) {
                     if(e==null)
                     {
                         if(objects.size()>0)
                         {
                             for (final ParseObject object:objects)
                             {
                                 ArrayList<String> testStringArrayList = (ArrayList<String>)object.get("likeby");
                                 String myString = ParseUser.getCurrentUser().getUsername();
                                if( testStringArrayList.contains(myString))
                                {
                                // Toast.makeText(getContext(),"find",Toast.LENGTH_SHORT).show();
                                    object.put("likes",object.getInt("likes")-1);
                                 testStringArrayList.remove(myString);
                                 object.put("likeby",testStringArrayList);
                                 object.saveInBackground(new SaveCallback() {
                                     @Override
                                     public void done(ParseException e) {
                                         if(e==null)
                                         {
                                             Toast.makeText(getContext(),"unliked",Toast.LENGTH_SHORT).show();
                                         }
                                     }
                                 });}
                                else
                                {
                                    object.put("likes",object.getInt("likes")+1);
                                    testStringArrayList.add(myString);
                                    object.put("likeby",testStringArrayList);
                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if(e==null)
                                            {
                                                Toast.makeText(getContext(),"liked",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }


                             }
                         }
                     }
                 }
             });

         }
     });
        likedis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,LikeByList.class);
                intent.putExtra("list",likeby.get(poition));
                context.startActivity(intent);

            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Intent intent = new Intent(context,Comment.class);
                intent.putExtra("commentBy",commentBy.get(poition));
               intent.putExtra("comment",commentlist.get(poition));
               intent.putExtra("id",id.get(poition));
                 context.startActivity(intent);


                Toast.makeText(getContext(),"click"+poition, Toast.LENGTH_SHORT).show();
            }


        });

        return rowView;
    };


}

