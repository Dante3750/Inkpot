package com.onlineinkpot.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.onlineinkpot.fragments.UnitTabFragment;
import com.onlineinkpot.models.ModSemest;

import java.util.List;

/**
 * Created by USER on 9/5/2017.
 */

public class DetailUnitAdapter extends FragmentPagerAdapter {
    private List<ModSemest> semList;

    public DetailUnitAdapter(FragmentManager fm, List<ModSemest> list) {
        super(fm);
        this.semList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return UnitTabFragment.newInstance(position);
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
