package com.cs160.joleary.catnip;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import org.w3c.dom.Text;

public class ElectionActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "13Xv2wmepyNMwJjPeoV2T4TBf";
    private static final String TWITTER_SECRET = "1eCoyImr7KVqZOpWuTLRbWlW7ojyMxZU6cdH6i1kKcHdoMjiBZ";


    private TextView mTextView;
    private TextView obama;
    private TextView obamaValue;
    private TextView romey;
    private TextView romeyValue;
    private TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_election);

        obamaValue = (TextView)findViewById(R.id.obamaValueTextView);
        romeyValue = (TextView)findViewById(R.id.romeyValueTextView);
        location = (TextView)findViewById(R.id.locationTextView);

        obamaValue.setText("63");
        romeyValue.setText("37");
        location.setText("94704");


    }

    public void onShuffleClicked(View v) {
        Log.d("SHUFFLE", "SHUFFLE");
        obamaValue = (TextView)findViewById(R.id.obamaValueTextView);
        romeyValue = (TextView)findViewById(R.id.romeyValueTextView);
        location = (TextView)findViewById(R.id.locationTextView);

        obamaValue.setText("54");
        romeyValue.setText("46");
        location.setText("94588");
    }
}
