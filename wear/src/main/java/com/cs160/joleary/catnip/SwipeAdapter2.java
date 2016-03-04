package com.cs160.joleary.catnip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Marcus on 3/3/16.
 */

public class SwipeAdapter2 extends FragmentStatePagerAdapter {

    public SwipeAdapter2(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("HERE HERE HERE" , "HERE HERE HERE" + position);
        Fragment fragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("count", position+1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        //TODO
        return 3;
    }
}
