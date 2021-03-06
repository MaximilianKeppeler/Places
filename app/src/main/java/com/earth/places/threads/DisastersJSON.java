package com.earth.places.threads;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.earth.places.R;
import com.earth.places.activities.MainActivity;
import com.earth.places.fragment.FragmentDisasters;
import com.earth.places.models.Disaster;
import com.earth.places.models.Disasters;
import com.earth.places.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisastersJSON extends AsyncTask<Void, Void, Void> {

    private final static ArrayList<Disaster> disasters = new ArrayList<>();
    private static final String TAG = "DisastersJSON";
    private long startTime;
    private Activity context;

    public DisastersJSON(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Disasters.clearList();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected Void doInBackground(Void... params) {

        JSONObject json = JSONParser.getJSONFromURL(context.getResources().getString(R.string.Sins_JSON));

        if (json != null)
            try {
                JSONArray jsonarray = json.getJSONArray("disasters");

                for (int i = 0; i < jsonarray.length(); i++) {

                    json = jsonarray.getJSONObject(i);

                    disasters.add(new Disaster(
                            json.getString("title"),
                            json.getString("images"),
                            json.getString("url")
                    ));
                }

                Disasters.createDisastersList(disasters);

            } catch (JSONException e) {
                Log.e(TAG, " Problem with the JSON API", e);
            }

        return null;
    }

    @Override
    protected void onPostExecute(Void args) {
        Log.d(TAG, "Task took " + String.valueOf((System.currentTimeMillis() - startTime) / 1000) + " seconds. Loaded Disasters: " + Disasters.getDisastersList().size());

        if (FragmentDisasters.refreshLayout != null && FragmentDisasters.recyclerView != null) {

            FragmentDisasters.updateLayout();
            FragmentDisasters.refreshLayout.setRefreshing(false);
            FragmentDisasters.recyclerView.setVisibility(View.VISIBLE);
        }

        if (MainActivity.tabLayout.getTabAt(1).isSelected())
        MainActivity.updateTabTexts(1, Disasters.getDisastersList().size(), 0);

    }


}


