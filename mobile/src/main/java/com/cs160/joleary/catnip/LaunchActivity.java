package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    public void onCurrentLocationButtonClick(View v) {
        Log.d("WHAT's MY CURRENT Location", "...");
        onLocationFound();
    }

    public void onZipcodeEditTextUpdate(View v){
        EditText rawZipcode = (EditText)findViewById(R.id.zipcodeEditText);
        Log.d("FIND MY LOCATION FROM ZIPCODE", "...");
        onLocationFound();
    }

    public void onLocationFound(){
        Intent myNextActivity = new Intent(LaunchActivity.this, CongressionalActivity.class);
        LaunchActivity.this.startActivity(myNextActivity);
    }
}
