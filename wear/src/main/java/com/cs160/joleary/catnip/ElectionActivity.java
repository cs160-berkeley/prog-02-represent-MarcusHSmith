package com.cs160.joleary.catnip;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ElectionActivity extends Activity {

    private TextView mTextView;
    private TextView obama;
    private TextView obamaValue;
    private TextView romey;
    private TextView romeyValue;
    private TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
