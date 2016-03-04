package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    public ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);

        /*viewPager = (ViewPager)findViewById(R.id.view_pager);
        SwipeAdapter2 swipeAdapter = new SwipeAdapter2(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);*/



        Candidate a1 = new Candidate(1, "Matt Burke", "R", "mburke@gmail.com", "www.mattburke.com", "mattburke", "LAST TWEET", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        Candidate b1 = new Candidate(2, "Derek Edgington", "D", "mburke@gmail.com", "www.mattburke.com", "derekedgington", "MY LAST TWEET", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        Candidate c1 = new Candidate(3, "Kevin Buscheck", "R", "kbiker@gmail.com", "www.kevinBuscheck.com", "kevinbuscheck", "YOUR LAST TWEET", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        this.candidates = new Candidate[]{a1,b1,c1};

        mElectionBtn = (Button) findViewById(R.id.electionButton);
        mNameTextView = (TextView) findViewById(R.id.nameTextView);
        mHeadImageView = (ImageView) findViewById(R.id.headImageView);
        mRightButton = (Button) findViewById(R.id.rightButton);
        mLeftButton = (Button) findViewById(R.id.leftButton);

        context = this;


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mNameTextView.setText("CANDIDATE");

        mLeftButton.setVisibility(View.VISIBLE);
        mRightButton.setVisibility(View.VISIBLE);


        View root = this.getWindow().getDecorView();
        if (extras != null) {
            final int position = extras.getInt("CAT_NAME");
            Candidate current = this.candidates[position];
            mNameTextView.setText(current.name);
            mHeadImageView.setImageResource(this.getResources().getIdentifier("com.cs160.joleary.catnip:drawable/" + current.head, null, null));

            mLeftButton.setAlpha(1);
            mLeftButton.setAlpha(1);
            if (position == 0){
                mLeftButton.setAlpha(0);
            }
            if (position == candidates.length - 1){
                mRightButton.setAlpha(0);
            }


            if (current.party == "R") {
                root.setBackgroundColor(0xFFFF0042);
            } else if (current.party == "D") {
                root.setBackgroundColor(0xFF2F329F);
            }

            mLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position - 1 >= 0) {
                        Intent intent = new Intent(context, WatchToPhoneService.class);
                        Bundle b = new Bundle();
                        String newInt = Integer.toString(position - 1);
                        b.putString("CAT_NAME", newInt);
                        intent.putExtras(b);
                        startService(intent);
                        Log.d("FINISHED UPDATE1", "LOCATION");
                    }
                }
            });

            mRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position + 1 <= candidates.length - 1) {
                        Intent intent = new Intent(context, WatchToPhoneService.class);
                        Bundle b = new Bundle();
                        String newInt = Integer.toString(position + 1);
                        b.putString("CAT_NAME", newInt);
                        intent.putExtras(b);
                        startService(intent);
                        Log.d("FINISHED UPDATE2", "LOCATION");
                    }
                }
            });


        }



        mElectionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myNextActivity = new Intent(CongressionalActivity.this, ElectionActivity.class);
                CongressionalActivity.this.startActivity(myNextActivity);
            }
        });


    }


}