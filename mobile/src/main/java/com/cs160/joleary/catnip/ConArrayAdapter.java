package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Marcus on 2/29/16.
 */
public class ConArrayAdapter extends BaseAdapter {
    public Candidate[] candidates;
    public Context context;
    public JSONObject response;
    public int rowPos;

    public ConArrayAdapter(Context context, Candidate[] candidates, String response){
        this.context = context;
        this.candidates = candidates;
        try {
            this.response = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateTweet(String[] tweets){
        Log.d("TWEETS", rowPos + " " + tweets);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conRow = inflater.inflate(R.layout.con_layout, parent, false);

        final Candidate current = this.candidates[position];
        rowPos = position;

        TextView nameView = (TextView) conRow.findViewById(R.id.NameTextView);
        TextView partyView = (TextView) conRow.findViewById(R.id.PartyTextView);
        TextView tweetView = (TextView) conRow.findViewById(R.id.tweetTextView);
        ImageView headView = (ImageView) conRow.findViewById(R.id.headImageView);
        ImageButton emailView = (ImageButton) conRow.findViewById(R.id.emailImageButton);
        ImageButton homepageView = (ImageButton) conRow.findViewById(R.id.homepageImageButton);
        TextView line = (TextView) conRow.findViewById(R.id.line);

        nameView.setText(current.name);
        partyView.setText(current.party);

        if (current.tweet != null){
            tweetView.setText(current.tweet);
        }

        Picasso.with(context).load("https://theunitedstates.io/images/congress/450x550/" + current.id + ".jpg").into(headView);
        emailView.setImageResource(R.drawable.email);
        homepageView.setImageResource(R.drawable.homepage);

        Log.d("PARTY is:", current.party);
        if (current.party.equals("R")) {
            line.setBackgroundColor(Color.rgb(0, 0, 255));
            partyView.setTextColor(Color.rgb(0,0,255));
        } else if (current.party.equals("D")) {
            //conRow.setBackgroundColor(0xFF2F329F);
            //conRow.setBackgroundColor(Color.rgb(255,0,0));
            line.setBackgroundColor(Color.rgb(255,0,0));
            partyView.setTextColor(Color.rgb(255,0,0));
        } else {
            line.setBackgroundColor(Color.rgb(192,192,192));
            partyView.setTextColor(Color.rgb(192,192,192));
        }
        if (current.room.equals("house")){
            partyView.setText("Representative");
        } else {
            partyView.setText("Senator");
        }

        conRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("T", "TRYING TO SEND TO WATCH");
                Log.d("JSON:", response.toString());
                try {
                    response.put("CAT_NAME", Integer.toString(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("JSON:", response.toString());


                Toast.makeText(context, current.name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("JSON", response.toString());
                context.startActivity(intent);
            }
        });
        return conRow;
    }
}

