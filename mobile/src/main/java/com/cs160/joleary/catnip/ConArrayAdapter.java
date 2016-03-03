package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.SyncStateContract.Constants;

import org.w3c.dom.Text;

/**
 * Created by Marcus on 2/29/16.
 */
public class ConArrayAdapter extends BaseAdapter {
    public Candidate[] candidates;
    public Context context;

    public ConArrayAdapter(Context context, Candidate[] candidates){
        this.context = context;
        this.candidates = candidates;
    }

    @Override
    public int getCount() {
        return candidates.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conRow = inflater.inflate(R.layout.con_layout, parent, false);

        final Candidate current = this.candidates[position];

        TextView nameView = (TextView) conRow.findViewById(R.id.NameTextView);
        TextView partyView = (TextView) conRow.findViewById(R.id.PartyTextView);
        TextView tweetView = (TextView) conRow.findViewById(R.id.tweetTextView);
        ImageView headView = (ImageView) conRow.findViewById(R.id.headImageView);
        ImageButton emailView = (ImageButton) conRow.findViewById(R.id.emailImageButton);
        ImageButton homepageView = (ImageButton) conRow.findViewById(R.id.homepageImageButton);

        nameView.setText(current.name);
        partyView.setText(current.party);
        tweetView.setText(current.tweet);
        headView.setImageResource(this.context.getResources().getIdentifier("com.cs160.joleary.catnip:drawable/" + current.head, null, null));
        emailView.setImageResource(R.drawable.email);
        homepageView.setImageResource(R.drawable.homepage);

        if (current.party == "R") {
            conRow.setBackgroundColor(0xFFFF0042);
        } else if (current.party == "D") {
            conRow.setBackgroundColor(0xFF2F329F);
        }

        conRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, current.name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle b = new Bundle();
                b.putInt("position", position);
                intent.putExtras(b);
                context.startActivity(intent);
            }
            /*public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                Intent newActivity = new Intent(this, DetailActivity.class);
                startActivity(newActivity);
            }*/
        });

        return conRow;
    }

/*    public void congressionalClicked(View v, int position){
        Intent intent = new Intent(this, DetailActivity.class);
        //intent.putExtra
        startActivity(intent);
    }*/
}

