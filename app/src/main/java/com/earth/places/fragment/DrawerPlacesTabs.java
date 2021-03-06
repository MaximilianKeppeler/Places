package com.earth.places.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.earth.places.R;
import com.earth.places.activities.MainActivity;
import com.earth.places.adapters.PagerAdapter;
import com.earth.places.utilities.Constants;

import butterknife.ButterKnife;

public class DrawerPlacesTabs extends Fragment {

    private static final String TAG = "DrawerPlacesTabs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Activity context = getActivity();
        ButterKnife.bind(context);

        final View view = inflater.inflate(R.layout.places_tab_layout, null);
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        final PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), MainActivity.tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(MainActivity.tabLayout));
        MainActivity.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                /*
                Check if the use has searched but swiped to another fragment.
                If the user has done that:
                1. Break searching and old list of places/ bookmarks.
                2. Hide System Soft Keyboard automatically
                */

                if (MainActivity.index == Constants.ID_DRAWER_PLACES) {
                    if (tab.getPosition() == 1)
                        if (FragmentPlaces.filter != null && !FragmentPlaces.searchView.isIconified()) {
                            FragmentPlaces.updateLayout(false, null);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    if (tab.getPosition() == 0) {
                        if (FragmentBookmarks.filter != null && !FragmentBookmarks.searchView.isIconified()) {
                            FragmentBookmarks.updateLayout(false, null);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }

}