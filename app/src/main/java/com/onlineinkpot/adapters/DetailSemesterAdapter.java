package com.onlineinkpot.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.onlineinkpot.fragments.SemesterTabFragment;
import com.onlineinkpot.models.ModSemest;

import java.util.List;

/**
 * Created by USER on 8/26/2017.
 */

public class DetailSemesterAdapter extends FragmentPagerAdapter {
    private List<ModSemest> semList;

    public DetailSemesterAdapter(FragmentManager fm, List<ModSemest> list) {
        super(fm);
        this.semList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return SemesterTabFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return semList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return semList.get(position).getSemName();
    }
}