package com.cs160.joleary.catnip;

import android.media.Image;

/**
 * Created by Marcus on 2/25/16.
 */
public class Candidate {
    public String id;
    public String name;
    public String party;
    public String email;
    public String homepage;
    public String tweet;
    public String head;
    public String term;
    public String twitter;
    public String room;
    public String[] commitees;
    public String[] bills;

    public Candidate(String id, String name, String party, String email, String homepage, String head, String tweet, String term, String[] commitees, String[] bills){
        this.id = id;
        this.name = name;
        this.party = party;
        this.email = email;
        this.homepage = homepage;
        this.tweet = tweet;
        this.term = term;
        this.commitees = commitees;
        this.bills = bills;
        this.twitter = "";

    }
    public Candidate(){
    }
}