package com.earth.places.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.earth.places.activities.MainActivity;
import com.earth.places.fragment.DrawerEmpty;
import com.earth.places.fragment.FragmentBookmarks;
import com.earth.places.fragment.FragmentDisasters;
import com.earth.places.fragment.FragmentGoodActs;
import com.earth.places.fragment.FragmentPlaces;
import com.earth.places.utilities.Constants;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int tabAmount;

    public PagerAdapter(FragmentManager fm, int tabAmount) {
        super(fm);
        this.tabAmount = tabAmount;
    }


    /**
     * Depending on the index from the main activity, other tabs will be shown.
     * @param index
     * @return
     */
    @Override
    public Fragment getItem(int index) {

        // Drawer Places
        if (MainActivity.index == Constants.ID_DRAWER_PLACES)
            switch (index) {
                case 0: return new FragmentPlaces();
                case 1: return new FragmentBookmarks();
            }


        // Drawer Nature
        if (MainActivity.index == Constants.ID_DRAWER_NATURE)
            switch (index) {
                case 0: return new FragmentDisasters();
                case 1: return new FragmentGoodActs();
            }

        // Drawer Hall of Honor
        if (MainActivity.index == Constants.ID_DRAWER_HALL)
            switch (index) {
                case 0: return new DrawerEmpty();
                case 1: return new DrawerEmpty();
            }

        return null;

    }

    @Override
    public int getCount() {
        return tabAmount;
    }

}
