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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

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
    private JSONArray rawarray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_election);

        obamaValue = (TextView)findViewById(R.id.obamaValueTextView);
        romeyValue = (TextView)findViewById(R.id.romeyValueTextView);
        location = (TextView)findViewById(R.id.locationTextView);

        rawarray = loadJSONFromAsset();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {


                JSONObject response = new JSONObject(getIntent().getStringExtra("JSON"));
                Log.d("JSON FROM CONGRESS", response.toString());
                String county = response.getString("county");
                String state = response.getString("state");
                Log.d("HELLO", "HELLO");
                Log.d("state", state);
                Log.d("county", county);

                JSONArray data = loadJSONFromAsset();
                JSONObject fin = searchJSON(data, county, state);
                obamaValue.setText(fin.getString("obama-percentage"));
                romeyValue.setText(fin.getString("romney-percentage"));
                Log.d("DATA", fin.toString());
                location.setText(county + " , " + state);

            } catch (JSONException e) {
                Log.d("NO INTENT WAS GIVEN", "GIVEN");
                e.printStackTrace();
            }
        } else {
            Log.d("YOLO", "YOLO");
            obamaValue.setText("--");
            romeyValue.setText("--");
            location.setText("County, State");

        }
    }

    public JSONObject searchJSON(JSONArray array, String county, String state){
        county = county.substring(0, county.length() - 7);
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = null;
            try {
                o = array.getJSONObject(i);

                if (o.getString("state-postal").equals(state) && o.getString("county-name").equals(county)){
                    return o;
                }

            } catch (JSONException e) {
            e.printStackTrace();
            }
        }
        return null;
    }

    public void onShuffleClicked(View v) {
        Log.d("SHUFFLE", "SHUFFLE");
        obamaValue = (TextView)findViewById(R.id.obamaValueTextView);
        romeyValue = (TextView)findViewById(R.id.romeyValueTextView);
        location = (TextView)findViewById(R.id.locationTextView);

        Log.d("CHOSE RANDOMS", "RANDOMS");
        Random rand = new Random();
        int randomNum = rand.nextInt((500 - 1) + 1) + 1;
        Log.d("RANDOM", Integer.toString(randomNum));

        JSONObject o = null;
        try {
            o = (JSONObject) rawarray.get(randomNum);
            location.setText(o.getString("county-name") + ", " + o.getString("state-postal"));
            obamaValue.setText(o.getString("obama-percentage"));
            romeyValue.setText(o.getString("romney-percentage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public JSONArray loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.electiondata);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Log.d("JSON:", json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            return new JSONArray(json);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
