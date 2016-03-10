package com.cs160.joleary.catnip;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.KeyEvent;

import com.loopj.android.http.*;
import org.json.*;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class LaunchActivity extends Activity {


    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    protected LocationManager locationManager;
    protected Button currentLocationButton;
    protected EditText zipcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        getDistrict(LaunchActivity.this, rawZipCode);

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
            String county = getCountyNameFromLatLong(LaunchActivity.this, location.getLatitude(), location.getLongitude());
            Toast.makeText(LaunchActivity.this, county, Toast.LENGTH_LONG).show();
        } else {
            Log.d("IT's A NULL", "...");
        }
        getDistrict(LaunchActivity.this, location.getLongitude(), location.getLatitude());
    }

    public static String getCountyNameFromLatLong(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getLocality();
            }
            return null;
        } catch (IOException ignored) {
            //do something
            return "FAILURE";
        }

    }

    public void getDistrict(final Context context, Double latitude, Double longitude ){
        RequestParams params = new RequestParams();
        params.put("apikey", "204dda0dd1c441cbbaf7514e87ac1743");
        params.put("latitude", Double.toString(latitude));
        params.put("longitude", Double.toString(longitude));
        SunlightRestClient.get("districts/locate", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESS", response.toString());


                try {
                    JSONObject results = response.getJSONObject("results");
                    String district = results.getString("district");
                    Log.d("district", district);
                    //getMembers(context, district)
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
    public void getDistrict(final Context context, String zipcode){
        RequestParams params = new RequestParams();
        params.put("apikey", "204dda0dd1c441cbbaf7514e87ac1743");
        params.put("zip", zipcode);
        SunlightRestClient.get("districts/locate", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESS", response.toString());

                try {
                    JSONArray resultsArray = (JSONArray) response.getJSONArray("results");
                    int count = (int) response.get("count");
                    String[] includedDistricts = new String[count];
                    for (int i = 0; i < count; i++) {
                        JSONObject result = resultsArray.getJSONObject(i);
                        String district = (String) result.getString("district");
                        includedDistricts[i] = district;
                    }
                    getLegislators(context, includedDistricts);
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

    public void getLegislators(Context context, String[] districts){
        for (int i = 0; i < districts.length; i++){
            RequestParams params = new RequestParams();
            params.put("apikey", "204dda0dd1c441cbbaf7514e87ac1743");
            params.put("district", Integer.parseInt(districts[i]));
            Log.d("Params", params.toString());
            SunlightRestClient.get("legislators", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("SUCCESS", response.toString());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                    Log.d("FAILURE", errorResponse.toString());
                }
            });
        }


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
