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

import org.w3c.dom.Text;

public class CongressionalActivity extends Activity {

    public Candidate [] candidates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        Candidate a = new Candidate(1, "Matt Burke", "R", "mburke@gmail.com", "www.mattburke.com", "mattburke", "VOTE FOR ME, BECAUSE I AM THE BEST CANDIDATE", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        Candidate b = new Candidate(2, "Derek Edgington", "D", "mburke@gmail.com", "www.mattburke.com", "derekedgington", "@MATTBURKE lets take some money from these companies", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        Candidate c = new Candidate(3, "Kevin Buscheck", "R", "kbiker@gmail.com", "www.kevinBuscheck.com", "kevinbuscheck", "Tonights dinner was eggs and waffle #umm", "Feb. 2018", new String[]{"Asses of America", "4H of America", "Alcoholic's Annoymous"}, new String[]{"No Child Left Behind", "Legalize Marijuana", "9/11 Conspiracy"});
        this.candidates = new Candidate[]{a,b,c};

        final ListView congressionalList = (ListView)findViewById(R.id.congressionalListView);
        final ConArrayAdapter adapter = new ConArrayAdapter(this, candidates);

        congressionalList.setAdapter(adapter);

    }

    public Candidate getCandidate(int position){
        return candidates[position];
    }

}
