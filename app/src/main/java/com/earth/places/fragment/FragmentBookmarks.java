package com.earth.places.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.afollestad.inquiry.Inquiry;
import com.earth.places.adapters.ParallaxAdapter;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.earth.places.R;
import com.earth.places.activities.MainActivity;
import com.earth.places.activities.PlaceView;
import com.earth.places.models.Place;
import com.earth.places.models.Places;
import com.earth.places.utilities.Bookmarks;
import com.earth.places.utilities.Constants;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentBookmarks extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FragmentBookmarks";
    public static SwipeRefreshLayout refreshLayout;
    public static ArrayList<Place> filter;
    public static SearchView searchView;
    private static ArrayList<Place> bookmarks = new ArrayList<>();
    private static ParallaxAdapter mAdapter;
    private static RecyclerView mRecyclerView;
    private static Activity context;
    private Menu bookmarksMenu;

    public static ArrayList<Place> getBookmarks() {
        return bookmarks;
    }

    public static void updateLayout(final boolean filtering, final ArrayList<Place> searchFiltering) {

        if (bookmarks != null && bookmarks.size() > 0) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter = new ParallaxAdapter(context,
                            new ParallaxAdapter.ClickListener() {
                                @Override
                                public void onClick(ParallaxAdapter.ParallaxViewHolder v, int position) {

                                    Intent intent = new Intent(context, PlaceView.class);
                                    if (filtering)
                                        intent.putExtra("item", searchFiltering.get(position));
                                    else intent.putExtra("item", bookmarks.get(position));
                                    intent.putExtra("pos", position);
                                    intent.putExtra("color", Places.getPlacesList().get(position).getColor());
                                    context.startActivity(intent);

                                }
                            });

                    if (filtering) {

                        if (searchFiltering != null) {
                            mAdapter.setData(searchFiltering);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.setData(filter);
                            mRecyclerView.setAdapter(mAdapter);
                        }

                    } else {
                        mAdapter.setData(bookmarks);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
            });
        }

        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public static void searchFilter(String key) {

        filter = new ArrayList<>();
        int x = 0;
        for (int j = 0; j < bookmarks.size(); j++) {

            if (bookmarks.get(j).getPlace().toLowerCase().contains(key.toLowerCase())) {
                filter.add(x, bookmarks.get(j));
                x++;
            }
        }
        updateLayout(true, filter);

        MainActivity.updateTabTexts(0, Places.getPlacesList().size(), filter.size());
    }

    public static void loadBookmarks(final Activity context) {

        bookmarks.clear();

        Bookmarks.init(context);

        if (Bookmarks.getDB() != null) {

            for (int j = 0; j < Places.getPlacesList().size(); j++) {

                for (int i = 0; i < Bookmarks.getDB().length; i++) {

                    if (Places.getPlacesList().get(j).getId().equals(Bookmarks.getDB()[i].getID())) {

                        if (Bookmarks.isBookmarked(Bookmarks.getDB()[i].getID())) {

                            bookmarks.add(Places.getPlacesList().get(j));
                            Log.i(TAG, "Bookmarked Place: " + Bookmarks.getDB()[i].getID());
                        }
                    }
                }
            }


        } else mRecyclerView.setAdapter(mAdapter);

        Inquiry.deinit();

        MainActivity.updateTabTexts(0, Places.getPlacesList().size(), bookmarks.size());

        updateLayout(false, null);

        if (refreshLayout != null) refreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.actions_places, menu);

        bookmarksMenu = menu;

        MenuItem item = menu.findItem(R.id.search);
        searchView = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setActionView(item, searchView);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                updateLayout(false, null);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String key) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String key) {
                searchFilter(key);
                MainActivity.drawerFilter.setSelection(Constants.NO_SELECTION);
                return false;
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {

        int id = menu.getItemId();

        switch (id) {
            case R.id.shuffle:
                ArrayList<Place> shuffled = bookmarks;
                Collections.shuffle(shuffled);
                updateLayout(true, shuffled);
                break;
            case R.id.drawer:
                MainActivity.drawerFilter.openDrawer();
                break;
        }

        return super.onOptionsItemSelected(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        context = getActivity();

        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.bookmarksRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.bookmarksRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onRefresh() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        loadBookmarks(context);
        MainActivity.drawerFilter.setSelection(Constants.NO_SELECTION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bookmarksMenu != null)
        bookmarksMenu.clear();
    }

}
