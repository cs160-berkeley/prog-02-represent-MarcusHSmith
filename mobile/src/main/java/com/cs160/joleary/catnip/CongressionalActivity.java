package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CongressionalActivity extends Activity {

    public Candidate [] candidates;

    public String passResponse;
    public String testing;
    public String[] tweets;

    private static final String TWITTER_KEY = "2qRkKj88jzA0bUbBRaXkJssyo";
    private static final String TWITTER_SECRET = "qUUQ5GOFF5MBf3ujlXXyXe7uQOt3gH7CsF3qkjY12aDWtnsrui";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        passResponse = "";
        JSONObject response = null;
        try {


            response = new JSONObject(getIntent().getStringExtra("JSON"));
            passResponse = response.toString();
            Log.d("JSON FROM CONGRESS", response.toString());
            int count = (int) response.getInt("count");
            candidates = new Candidate[count];
            JSONArray results = response.getJSONArray("results");
            tweets = new String[count];
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
                getTwitter(cur.twitter, i);

                candidates[i] = cur;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ListView congressionalList = (ListView)findViewById(R.id.congressionalListView);
        final ConArrayAdapter adapter = new ConArrayAdapter(this, candidates, passResponse);
        congressionalList.setAdapter(adapter);
        adapter.updateTweet(tweets);
    }

    public Candidate getCandidate(int position){
        return candidates[position];
    }

    public void getTwitter(final String handle, final int position){
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                twitterApiClient.getStatusesService().userTimeline(null, handle, 1, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        for (Tweet t : result.data) {
                            android.util.Log.d("twittercommunity", "tweet is " + t.text);
                            tweets[position] = t.text;
                            candidates[position].tweet = t.text;
                            final ListView congressionalList = (ListView)findViewById(R.id.congressionalListView);
                            Log.d("PASSSING IT ALLOW", passResponse);
                            final ConArrayAdapter adapter = new ConArrayAdapter(CongressionalActivity.this, candidates, passResponse);
                            congressionalList.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void failure(TwitterException exception) {
                        android.util.Log.d("twittercommunity", "exception " + exception);
                    }
                });
            }
            @Override
            public void failure(TwitterException exception){
                android.util.Log.d("twittercommunity", "exception " + exception);
            }
        });
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
                    Log.d("JSON FROM CONGRESS", response.toString());
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
                testing = "YOLO";

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("FAILURE", errorResponse.toString());
                String[] commitees;
                String[] commiteeIDs;
                commitees = new String[0];
                commiteeIDs = new String[0];
            }
        });
    }


}



