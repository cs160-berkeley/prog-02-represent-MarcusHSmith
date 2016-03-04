package com.cs160.joleary.catnip;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import org.w3c.dom.Text;

public class DetailActivity extends Activity {

    public Candidate [] candidates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Candidate a1 = new Candidate(1, "Matt Burke", "R", "mburke@gmail.com", "www.mattburke.com", "mattburke", "LAST TWEET", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        Candidate b1 = new Candidate(2, "Derek Edgington", "D", "mburke@gmail.com", "www.mattburke.com", "derekedgington", "MY LAST TWEET", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        Candidate c1 = new Candidate(3, "Kevin Buscheck", "R", "kbiker@gmail.com", "www.kevinBuscheck.com", "kevinbuscheck", "YOUR LAST TWEET", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        this.candidates = new Candidate[]{a1,b1,c1};

        Log.d("T", "YOLO");

        Bundle b = getIntent().getExtras();
        int position = b.getInt("position");
        String fromWatch = b.getString("send_toast");

        //Log.d("T", fromWatch);

        for (String key: b.keySet())
        {
            Log.d ("myApplication", key + " is a key in the bundle");
        }

        Candidate current = this.candidates[position];

        TextView name = (TextView)findViewById(R.id.nameTextView);
        TextView term = (TextView)findViewById(R.id.termEndTextView);
        ImageView head = (ImageView)findViewById(R.id.headImageView);
        ImageView party = (ImageView)findViewById(R.id.partyImageView);
        ListView commitees = (ListView) findViewById(R.id.commiteeListView);
        ListView bills = (ListView) findViewById(R.id.billListView);
        TextView commiteesText = (TextView) findViewById(R.id.commiteesTextView);
        TextView billsText = (TextView) findViewById(R.id.billsTextView);

        name.setText(current.name);
        term.setText("TERM END: " + current.term);
        head.setImageResource(this.getResources().getIdentifier("com.cs160.joleary.catnip:drawable/" + current.head, null, null));

        View root = this.getWindow().getDecorView();
        if (current.party == "R") {
            root.setBackgroundColor(0xFFFF0042);
            party.setImageResource(this.getResources().getIdentifier("com.cs160.joleary.catnip:drawable/republican", null, null));
        } else if (current.party == "D") {
            root.setBackgroundColor(0xFF2F329F);
            party.setImageResource(this.getResources().getIdentifier("com.cs160.joleary.catnip:drawable/democrat", null, null));
        }

        ArrayAdapter<String> commiteesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, current.commitees);
        ArrayAdapter<String> billssAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, current.bills);

        commitees.setAdapter(commiteesAdapter);
        bills.setAdapter(billssAdapter);

        String styledText = "<u>Commitees</u>";
        commiteesText.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        styledText = "<u>Bills</u>";
        billsText.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

        /*Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        String positionString = Integer.toString(position);
        sendIntent.putExtra("POSITION", positionString);
        startService(sendIntent);*/

        Log.d("T", "TRYING TO SEND TO WATCH");

        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("CAT_NAME", Integer.toString(position));
        startService(sendIntent);

        Log.d("T", "FINISHED SENDING TO WATCH");


    }

}
