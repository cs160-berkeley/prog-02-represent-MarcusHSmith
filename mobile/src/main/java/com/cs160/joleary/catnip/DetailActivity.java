package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class DetailActivity extends Activity {

    public Candidate [] candidates;
    public int posBill = 0;
    public String[] comIDs;
    public String[] billNames;
    public JSONObject response;
    public int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Log.d("IN DETAIL", "DETAIL");


        Log.d("IN DETAIL", "DETAIL");

        try {
            response = new JSONObject(getIntent().getStringExtra("JSON"));

            Log.d("JSON FROM CONGRESS", response.toString());
            position = (int) response.getInt("CAT_NAME");
            Log.d("THE POSITION IS", Integer.toString(position));

            Log.d("JSON FROM CONGRESS", response.toString());
            int count = (int) response.getInt("count");
            candidates = new Candidate[count];
            JSONArray results = response.getJSONArray("results");
            int i = position;
            JSONObject candidate = results.getJSONObject(i);
            Candidate cur = new Candidate();
            cur.id = candidate.getString("bioguide_id");
            cur.name = candidate.getString("first_name") + " " + candidate.getString("last_name");
            cur.party = candidate.getString("party");
            cur.email = candidate.getString("oc_email");
            cur.homepage = candidate.getString("website");
            cur.twitter = candidate.getString("twitter_id");
            cur.term = candidate.getString("term_end");
            cur.room = candidate.getString("chamber");
            cur.bills = new String[] {"YES", "NO"};
            cur.commitees = new String[] {"YES", "NO"};
            getCommitee(DetailActivity.this, cur);
            getBills(DetailActivity.this, cur.id);
            candidates[i] = cur;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("T", "YOLO");


        Candidate current = this.candidates[position];

        TextView name = (TextView)findViewById(R.id.nameTextView);
        TextView term = (TextView)findViewById(R.id.termEndTextView);
        ImageView head = (ImageView)findViewById(R.id.headImageView);
        ImageView party = (ImageView)findViewById(R.id.partyImageView);
        ListView commitees = (ListView) findViewById(R.id.commiteeListView);
        ListView bills = (ListView) findViewById(R.id.billListView);
        TextView commiteesText = (TextView) findViewById(R.id.commiteesTextView);
        TextView billsText = (TextView) findViewById(R.id.billsTextView);
        TextView room = (TextView) findViewById(R.id.roomTextView);

        name.setText(current.name);
        term.setText("TERM END: " + current.term);
        Picasso.with(DetailActivity.this).load("https://theunitedstates.io/images/congress/450x550/" + current.id + ".jpg").into(head);

        room.setText("PENIS");

        View root = this.getWindow().getDecorView();
        if (current.party.equals("R")) {
            room.setTextColor(Color.rgb(0, 0, 255));
            party.setImageResource(this.getResources().getIdentifier("com.cs160.joleary.catnip:drawable/republican", null, null));
        } else if (current.party.equals("D")) {
            room.setTextColor(Color.rgb(255, 0, 0));
            party.setImageResource(this.getResources().getIdentifier("com.cs160.joleary.catnip:drawable/democrat", null, null));
        } else {
            room.setTextColor(Color.rgb(192,192,192));
        }

        if (current.room.equals("house")){
            room.setText("Representative");
        } else {
            room.setText("Senator");
        }

        String styledText = "<u>Commitees</u>";
        commiteesText.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        styledText = "<u>Bills</u>";
        billsText.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

        Log.d("T", "TRYING TO SEND TO WATCH");
        Log.d("JSON:", response.toString());
        try {
            response.put("CAT_NAME", Integer.toString(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON:", response.toString());

        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("JSON", response.toString());
        startService(sendIntent);

        Log.d("T", "FINISHED SENDING TO WATCH");
    }

    public void getCommitee(final Context context, Candidate cur){
        RequestParams params = new RequestParams();
        params.put("apikey", "204dda0dd1c441cbbaf7514e87ac1743");
        params.put("member_ids", cur.id);
        SunlightRestClient.get("committees", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESS", response.toString());
                String[] commitees;
                String[] commiteeIDs;
                try {
                    int count = (int) response.getInt("count");
                    commitees = new String[count];
                    commiteeIDs = new String[count];
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < count; i++) {
                        JSONObject commitee = results.getJSONObject(i);
                        commitees[i] = commitee.getString("name");
                        commiteeIDs[i] = commitee.getString("committee_id");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    commitees = new String[0];
                    commiteeIDs = new String[0];
                }
                ListView commiteesLV = (ListView) findViewById(R.id.commiteeListView);
                ArrayAdapter<String> commiteesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, commitees);
                commiteesLV.setAdapter(commiteesAdapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("FAILURE", errorResponse.toString());
            }
        });
    }

    public void getBills(final Context context, String sponsor){
        RequestParams params = new RequestParams();
        params.put("apikey", "204dda0dd1c441cbbaf7514e87ac1743");
        params.put("sponsor_id", sponsor);
        SunlightRestClient.get("bills", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SUCCESS", response.toString());
                String[] bills;
                try {
                    int count = (int) response.getInt("count");
                    bills = new String[count];
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < count; i++) {
                        JSONObject bill = results.getJSONObject(i);
                        bills[i] = bill.getString("short_title");
                    }
                    ListView billsLV = (ListView) findViewById(R.id.billListView);
                    //remove null from array

                    List<String> list = new ArrayList<String>();

                    for(String s : bills) {
                        if(s != null && s.length() > 0) {
                            if (s.equals("null")){
                                Log.d("IT's Equal", "EQUAL");
                            } else {
                                list.add(s);
                            }
                        }
                    }

                    String[] newBills = list.toArray(new String[list.size()]);

                    ArrayAdapter<String> billsAdapter = new ArrayAdapter<String>(DetailActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, newBills);
                    billsLV.setAdapter(billsAdapter);
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

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }








}
