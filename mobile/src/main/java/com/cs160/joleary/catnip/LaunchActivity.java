package com.cs160.joleary.catnip;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import cz.msebera.android.httpclient.entity.StringEntity;
import io.fabric.sdk.android.Fabric;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.KeyEvent;

import com.loopj.android.http.*;
import org.json.*;
import org.json.JSONObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetUtils;

import cz.msebera.android.httpclient.Header;


public class LaunchActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "2qRkKj88jzA0bUbBRaXkJssyo";
    private static final String TWITTER_SECRET = "qUUQ5GOFF5MBf3ujlXXyXe7uQOt3gH7CsF3qkjY12aDWtnsrui";



    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    protected LocationManager locationManager;
    protected Button currentLocationButton;
    protected EditText zipcode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_launch);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        currentLocationButton = (Button) findViewById(R.id.currentLocationButton);
        zipcode = (EditText) findViewById(R.id.zipcodeEditText);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                locationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );


        currentLocationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("WHAT's MY CURRENT Location", "...");
                showCurrentLocation();
                //onLocationFound();
            }
        });

        zipcode.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (!event.isShiftPressed()) {
                        String rawZipCode = zipcode.getText().toString();
                        Log.d("FIND MY LOCATION FROM ZIPCODE", rawZipCode);
                        getLegislators(LaunchActivity.this, rawZipCode);

                        return true;
                    }
                }
                return false;
            }
        });
    }

    protected void showCurrentLocation() {
        Log.d("SEARCHING FOR MY LOCATION", "...");
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());
            Toast.makeText(LaunchActivity.this, message, Toast.LENGTH_LONG).show();
            Log.d("ABOUT TO GET", "LEGGGGIES");
            getLegislators(LaunchActivity.this, location.getLatitude(), location.getLongitude());
            //getCountyNameFromLatLong(LaunchActivity.this, a, response);
        } else {
            Log.d("IT's A NULL", "...");
            getLegislators(LaunchActivity.this, location.getLatitude(), location.getLongitude());
        }

    }


    public void getCountyNameFromLatLong(Context context, Address address, final JSONObject passThrough){
        Log.d("ABOUT TO TRY", "TRYING NOW");
        RequestParams params = new RequestParams();
        params.put("address", address.getLocality().toString());
        params.put("key", "AIzaSyBufb_k8pfht9kwPjxbzHe1kYxJU7GR2Hw" );
        Log.d("Address", address.toString());
        GoogleRestClient.get("", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESSzz", response.toString());




                JSONArray a = null;
                try {
                    a = response.getJSONArray("results");

                    Log.d("RESULT: ", a.toString());
                    JSONObject b = a.getJSONObject(0);
                    JSONArray c = b.getJSONArray("address_components");
                    Log.d("address compoent: ", c.toString());
                    JSONObject d= c.getJSONObject(0);
                    String city = d.getString("long_name");
                    Log.d("city", city);
                    JSONObject t= c.getJSONObject(1);
                    String county = t.getString("long_name");
                    Log.d("county", county);
                    JSONObject f= c.getJSONObject(2);
                    String state = f.getString("short_name");
                    Log.d("state", state);

                    passThrough.put("state", state);
                    passThrough.put("county", county);

                    Intent myNextActivity = new Intent(LaunchActivity.this, CongressionalActivity.class);
                    myNextActivity.putExtra("JSON", passThrough.toString());
                    LaunchActivity.this.startActivity(myNextActivity);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("FAILURE", errorResponse.toString());
            }
        });
    }

    public static String getZipcodeFromLatLong(Context context, double latitude, double longitude){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {

                return addresses.get(0).getPostalCode();
            }
            return null;
        } catch (IOException ignored) {
            //do something
            return "FAILURE";
        }
    }

    public static Address getAddressFromLatLong(Context context, double latitude, double longitude){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0);
            }
            return null;
        } catch (IOException ignored) {
            //do something
            return null;
        }
    }

    public static Address getAddressFromZipcode(Context context, String zipcode){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(zipcode, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0);
            }
            return null;
        } catch (IOException ignored) {
            //do something
            return null;
        }
    }

    public void getLegislators(final Context context, final Double latitude, final Double longitude ){
        RequestParams params = new RequestParams();
        params.put("apikey", "204dda0dd1c441cbbaf7514e87ac1743");
        Log.d("LAT", Double.toString(latitude));
        Log.d("Lon", Double.toString(longitude));
        params.put("latitude", Double.toString(latitude));
        params.put("longitude", Double.toString(longitude));
        SunlightRestClient.get("legislators/locate", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESS", response.toString());



                Address a = getAddressFromLatLong(LaunchActivity.this, latitude, longitude);
                getCountyNameFromLatLong(LaunchActivity.this, a, response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("FAILURE", errorResponse.toString());
            }
        });
    }
    public void getLegislators(final Context context, final String zipcode){
        RequestParams params = new RequestParams();
        params.put("apikey", "204dda0dd1c441cbbaf7514e87ac1743");
        params.put("zip", zipcode);
        SunlightRestClient.get("legislators/locate", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESS", response.toString());
                try {
                    response.put("ZIPCODE", zipcode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Address a = getAddressFromZipcode(LaunchActivity.this, zipcode);
                getCountyNameFromLatLong(LaunchActivity.this, a, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("FAILURE", errorResponse.toString());
            }
        });
    }






    public void onLocationFound(){
        Intent myNextActivity = new Intent(LaunchActivity.this, CongressionalActivity.class);
        LaunchActivity.this.startActivity(myNextActivity);
    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(LaunchActivity.this, message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(LaunchActivity.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(LaunchActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(LaunchActivity.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

    }

}