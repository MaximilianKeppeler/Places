package com.mk.places.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.google.gson.Gson;
import com.mk.places.R;
import com.mk.places.activities.MainActivity;
import com.mk.places.activities.PlaceView;
import com.mk.places.adapters.PlaceAdapter;
import com.mk.places.models.Place;
import com.mk.places.models.PlaceGSON;
import com.mk.places.models.Places;
import com.mk.places.utilities.Preferences;

import java.util.ArrayList;


public class DrawerPlacesNew extends Fragment {

    private static final String TAG = "DrawerPlaces";
    public static PlaceAdapter adapter;
    private static View view;
    private static RecyclerView recyclerView;
    private static Activity context;
    private static SwipeRefreshLayout refreshLayout;
    private static Preferences preferences;

    public static void searchFilter(String key) {

        ArrayList<Place> filter = new ArrayList<>();

        int x = 0;

        for (int j = 0; j < Places.getPlacesList().size(); j++) {

            if (
                    Places.getPlacesList().get(j).getLocation().toLowerCase().contains(key.toLowerCase())


                    ) {

                filter.add(x, Places.getPlacesList().get(j));
                x++;
            }

        }

        createLayout(true, filter);
    }

    public static void loadPlacesList(final Context context) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                String json = "https://drive.google.com/uc?id=0B6ky9fzTGl9XNFBhTTlxU2hhQmM";

                Gson gson = new Gson();
                PlaceGSON[] arr = gson.fromJson(json, PlaceGSON[].class);

//                TODO parse json with gson

                Log.d(TAG, "doInBackground: " + arr[0].getId());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                PlaceGSON placeGson = new PlaceGSON();
                Log.d(TAG, "doInBackground: " + placeGson.getId());

            }
        }.execute();

    }

    public static void createLayout(final boolean otherList, final ArrayList<Place> arrayList) {

        if (Places.getPlacesList() != null && Places.getPlacesList().size() > 0) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new PlaceAdapter(context,
                            new PlaceAdapter.ClickListener() {
                                @Override
                                public void onClick(PlaceAdapter.PlacesViewHolder view, final int position, boolean longClick) {

                                    Intent intent = new Intent(context, PlaceView.class);

                                    if (intent != null) {
                                        intent.putExtra("item", Places.getPlacesList().get(position));
                                        intent.putExtra("pos", position);
                                        context.startActivity(intent);
                                    }

                                }
                            });

                    if (!otherList) {
                        adapter.setData(Places.getPlacesList());
                        recyclerView.setAdapter(adapter);

                        Preferences mPref = new Preferences(context);
                        mPref.setPlacesSize(Places.getPlacesList().size());

                    } else {
                        adapter.setData(arrayList);
                        recyclerView.setAdapter(adapter);
                    }

                }
            });
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.actions_places, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView sv = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setActionView(item, sv);
        sv.setQueryHint("Berlin, Germany");
        sv.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String key) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String key) {
                searchFilter(key);
                MainActivity.drawerFilter.setSelection(0);
                return false;
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {

        int id = menu.getItemId();
        switch (id) {
            case R.id.drawer:
                MainActivity.drawerFilter.openDrawer();
                break;
        }

        return super.onOptionsItemSelected(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.drawer_places, null);

        setHasOptionsMenu(true);
        context = getActivity();

        preferences = new Preferences(context);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.placeRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
//                        loadPlacesList(context);
                        MainActivity.drawerFilter.setSelection(0);
                    }
                }
        );

        recyclerView = (RecyclerView) view.findViewById(R.id.placesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, preferences.getColumns(), 1, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        if (Places.getPlacesList() == null)
//            DrawerPlacesNew.loadPlacesList(context);

            createLayout(false, Places.getPlacesList());

        return view;
    }


}