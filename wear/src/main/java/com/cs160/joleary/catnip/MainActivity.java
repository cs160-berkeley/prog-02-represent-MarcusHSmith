package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    public Candidate[] candidates;
    private TextView mTextView;
    private Button mFeedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Candidate a = new Candidate(1, "Matt Burke", "R", "mburke@gmail.com", "www.mattburke.com", "mattburke", "LAST TWEET", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        Candidate b = new Candidate(2, "Derek Edgington", "D", "mburke@gmail.com", "www.mattburke.com", "derekedgington", "MY LAST TWEET", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        Candidate c = new Candidate(3, "Kevin Buscheck", "R", "kbiker@gmail.com", "www.kevinBuscheck.com", "kevinbuscheck", "YOUR LAST TWEET", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        this.candidates = new Candidate[]{a,b,c};

        mFeedBtn = (Button) findViewById(R.id.feed_btn);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        /*if (extras != null) {
            int position = extras.getInt("CAT_NAME");
            Candidate current = this.candidates[position];
            mFeedBtn.setText(current.name);

        }*/

        mFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                startService(sendIntent);
            }
        });
    }
}
