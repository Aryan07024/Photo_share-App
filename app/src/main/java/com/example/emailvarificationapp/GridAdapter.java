package com.example.emailvarificationapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends ArrayAdapter {
    ArrayList<List_Data> listdata;
    private final Activity context;
    int resource;
    private final ArrayList<ArrayList<String>> likeby;
    private final ArrayList<ArrayList<String>> commentBy;
    private final ArrayList<ArrayList<String>> commentlist;
    private final ArrayList<String> id;
    public GridAdapter(@NonNull Context context, int resource, @NonNull ArrayList<List_Data> listdata, ArrayList<ArrayList<String>> likeby, ArrayList<ArrayList<String>> commentBy, ArrayList<ArrayList<String>> commentlist, ArrayList<String> id) {
        super(context, resource, listdata);
        this.listdata=listdata;
        this.context= (Activity) context;
        this.resource=resource;
        this.likeby = likeby;
        this.commentBy = commentBy;
        this.commentlist = commentlist;
        this.id = id;
    }




    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.grid_list,null,true);
        }
        List_Data listdata= (List_Data) getItem(position);
        ImageView img=(ImageView)convertView.findViewById(R.id.image_view);
        Button likecheck =(Button)convertView.findViewById(R.id.likecheck);
        Button commentcheck = (Button)convertView.findViewById(R.id.commentcheck);
        Picasso.with(context)
                .load(listdata.getImageurl())
                .into(img);
        TextView likes = (TextView)convertView.findViewById(R.id.likeinprofile);
      likes.setText(String.valueOf(listdata.getContent()));

      likecheck.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(getContext(),"click",Toast.LENGTH_SHORT).show();
              Intent intent = new Intent(context, LikeByList.class);
              intent.putExtra("list",likeby.get(position));
              context.startActivity(intent);
          }
      });
        commentcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Comment.class);
                intent.putExtra("commentBy",commentBy.get(position));
                intent.putExtra("comment",commentlist.get(position));
                intent.putExtra("id",id.get(position));
                context.startActivity(intent);
                Toast.makeText(getContext(),"click",Toast.LENGTH_SHORT).show();
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(),"Delete",Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete")
                        .setMessage("Are you sure that u want to delete this post")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
                                query.whereEqualTo("objectId",id.get(position));
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if(e==null)
                                        {
                                            if(objects.size()>0)
                                            {
                                                for (ParseObject object:objects)
                                                {
                                                    object.deleteInBackground(new DeleteCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if(e==null)
                                                            {
                                                                new AlertDialog.Builder(getContext())
                                                                        .setTitle("Delete")
                                                                        .setMessage("delete Confirm").show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Delete")
                                        .setMessage("delete Abort").show();


                            }
                        })
                        .show();
                return false;
            }
        });


        return convertView;
    }
}
