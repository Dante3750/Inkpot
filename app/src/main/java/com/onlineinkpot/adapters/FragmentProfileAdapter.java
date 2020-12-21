package com.onlineinkpot.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.onlineinkpot.fragments.UpdatePasswordFragment;
import com.onlineinkpot.fragments.UpdateProfileFragment;
import com.onlineinkpot.fragments.WalletFragment;


public class FragmentProfileAdapter extends FragmentStatePagerAdapter {
    public int mtabcount;
    UpdateProfileFragment tab1;
    UpdatePasswordFragment tab2;

    public FragmentProfileAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.mtabcount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                tab1 = new UpdateProfileFragment();
                return tab1;
            case 1:
                tab2 = new UpdatePasswordFragment();
                return tab2;
            default:
                return null;
        }
    }

    public void refreshFragment(int position) {
        switch (position) {
            case 1:
                tab1.refreshApi();
                break;
            case 2:
                tab2.refreshApi();
                break;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
