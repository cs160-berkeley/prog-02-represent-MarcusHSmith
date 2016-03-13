package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CongressionalActivity extends FragmentActivity {

    public Candidate[] candidates;
    private TextView mTextView;
    private Button mVoteBtn;
    private Button mElectionBtn;
    private TextView mNameTextView;
    private ImageView mHeadImageView;
    private Button mRightButton;
    private Button mLeftButton;
    private Context context;
    public JSONObject response;
    public ViewPager viewPager;
    public int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);

        Log.d("CREATED", "WEAR CONGRESS");

        mElectionBtn = (Button) findViewById(R.id.electionButton);
        mNameTextView = (TextView) findViewById(R.id.nameTextView);
        mHeadImageView = (ImageView) findViewById(R.id.headImageView);
        mRightButton = (Button) findViewById(R.id.rightButton);
        mLeftButton = (Button) findViewById(R.id.leftButton);
        View myView = (View) findViewById(android.R.id.content);

        context = this;


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();


        for (String key: extras.keySet())
        {
            Log.d ("myApplication", key + " is a key in the bundle");
        }

        mNameTextView.setText("CANDIDATE");

        mLeftButton.setVisibility(View.VISIBLE);
        mRightButton.setVisibility(View.VISIBLE);


        View root = this.getWindow().getDecorView();
        if (extras != null) {

            //final int position = extras.getInt("CAT_NAME");
            position = 0;

            Log.d("FOUND EXTRAS", "FOUND EM");
            try {
                response = new JSONObject(getIntent().getStringExtra("JSON"));
                Log.d("JSON FROM CONGRESS", response.toString());
                int count = (int) response.getInt("count");
                position = (int) response.getInt("CAT_NAME");
                Log.d("THE POSITION IS", Integer.toString(position));
                candidates = new Candidate[count];
                JSONArray results = response.getJSONArray("results");
                for (int i = 0; i< count; i++){
                    JSONObject candidate = results.getJSONObject(i);
                    Candidate cur = new Candidate();
                    cur.id = candidate.getString("bioguide_id");
                    cur.name = candidate.getString("first_name") + " " + candidate.getString("last_name");
                    cur.party = candidate.getString("party");
                    cur.email = candidate.getString("oc_email");
                    cur.homepage = candidate.getString("website");
                    cur.twitter = candidate.getString("twitter_id");
                    cur.term = candidate.getString("term_end");
                    candidates[i] = cur;
                    Log.d("CREATED", cur.name);
                }
            } catch (JSONException e) {
                Log.d("WE FIALURED", "FAILED");
                e.printStackTrace();


            }



            Candidate current = candidates[position];
            Log.d("ARE WE GETTING THE GUY", current.name);


            mLeftButton.setAlpha(1);
            mLeftButton.setAlpha(1);
            if (position == 0){
                mLeftButton.setAlpha(0);
            }
            if (position == candidates.length - 1){
                mRightButton.setAlpha(0);
            }


            if (current.party.equals("R")) {
                mHeadImageView.setImageResource(this.getResources().getIdentifier("com.cs160.joleary.catnip:drawable/republican", null, null));
            } else if (current.party.equals("D")) {
                mHeadImageView.setImageResource(this.getResources().getIdentifier("com.cs160.joleary.catnip:drawable/democrat", null, null));
            } else {
            }


            mNameTextView.setText(current.name);

            mLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position - 1 >= 0) {


                        Log.d("T", "TRYING TO SEND TO WATCH");
                        Log.d("JSON:", response.toString());
                        try {
                            response.put("CAT_NAME", Integer.toString(position - 1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("JSON:", response.toString());


                        Intent intent = new Intent(context, WatchToPhoneService.class);
                        intent.putExtra("JSON", response.toString());
                        startService(intent);


                        Log.d("FINISHED UPDATE1", "LOCATION");
                    }
                }
            });

            mRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position + 1 <= candidates.length - 1) {
                        Log.d("T", "TRYING TO SEND TO WATCH");
                        Log.d("JSON:", response.toString());
                        try {
                            response.put("CAT_NAME", Integer.toString(position + 1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("JSON:", response.toString());


                        Intent intent = new Intent(context, WatchToPhoneService.class);
                        intent.putExtra("JSON", response.toString());
                        startService(intent);


                        Log.d("FINISHED UPDATE1", "LOCATION");
                    }
                }
            });





            View.OnTouchListener onThumbTouch = new View.OnTouchListener() {

                float previouspoint = 0 ;
                float startPoint=0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d("TRYING INSIDE OF SWIPER", "SWIPER");
                    switch(v.getId()) {
                        case android.R.id.content: // Give your R.id.sample ...
                            previouspoint = event.getX();
                            Log.d("X POINT IS:", Float.toString(previouspoint));
                            Log.d("START POINT IS", Float.toString(startPoint));
                            if (previouspoint > 170){
                                //SWIPE RIGHT
                                if (position + 1 <= candidates.length - 1) {
                                    Log.d("T", "TRYING TO SEND TO WATCH");
                                    Log.d("JSON:", response.toString());
                                    try {
                                        response.put("CAT_NAME", Integer.toString(position + 1));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("JSON:", response.toString());


                                    Intent intent = new Intent(context, WatchToPhoneService.class);
                                    intent.putExtra("JSON", response.toString());
                                    startService(intent);


                                    Log.d("FINISHED UPDATE1", "LOCATION");
                                }
                            } else if (previouspoint < 130){
                                //Swipe Left
                                if (position - 1 >= 0) {


                                    Log.d("T", "TRYING TO SEND TO WATCH");
                                    Log.d("JSON:", response.toString());
                                    try {
                                        response.put("CAT_NAME", Integer.toString(position - 1));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("JSON:", response.toString());


                                    Intent intent = new Intent(context, WatchToPhoneService.class);
                                    intent.putExtra("JSON", response.toString());
                                    startService(intent);


                                    Log.d("FINISHED UPDATE1", "LOCATION");
                                }
                            }
                            break;
                    }
                    return true;
                }
            };

            myView.setOnTouchListener(onThumbTouch);







        }

        mElectionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                Intent myNextActivity = new Intent(CongressionalActivity.this, ElectionActivity.class);
                myNextActivity.putExtra("JSON", response.toString());
                CongressionalActivity.this.startActivity(myNextActivity);
            }
        });


    }


}