package com.example.edu.menutb.model.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.edu.menutb.view.profile.FollowerFragment;
import com.example.edu.menutb.view.profile.FollowingFragment;
import com.example.edu.menutb.view.profile.PhotoProfileFragment;

/**
 * Created by jeffkenichi on 10/8/17.
 */

public class ChallengePagerAdapter extends FragmentStatePagerAdapter {

    // guarda os títulos das tabs que irão ser passadas quando o viewPagerAdapter for criado
    CharSequence titles[];
    // guarda o número de tabs
    int numberOfTabs;
    Bundle bundle;


    public ChallengePagerAdapter(FragmentManager fm, CharSequence[] titles, int numberOfTabs, String idString) {
        super(fm);
        this.titles = titles;
        this.numberOfTabs = numberOfTabs;
        bundle = new Bundle();
        bundle.putString("idString", idString);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PhotoProfileFragment photoProfileFragment = new PhotoProfileFragment();
                photoProfileFragment.setArguments(bundle);
                return photoProfileFragment;
            case 1:
                FollowerFragment followerFragment = new FollowerFragment();
                followerFragment.setArguments(bundle);
                return followerFragment;
            case 2:
                FollowingFragment followingFragment= new FollowingFragment();
                followingFragment.setArguments(bundle);
                return followingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return this.numberOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.titles[position];
    }
}
