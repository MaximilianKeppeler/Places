package com.mk.placesdrawer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mk.placesdrawer.R;
import com.mk.placesdrawer.models.PlaceDetail;

/**
 * Created by Max on 26.03.16.
 */
public class PlacesDetailAdapter extends RecyclerView.Adapter<PlacesDetailAdapter.ViewHolder> {
    private PlaceDetail[] itemsData;

    public PlacesDetailAdapter(PlaceDetail[] itemsData) {
        this.itemsData = itemsData;
    }

    // Create new Views of the Item View
    @Override
    public PlacesDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // new View
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_places_detail_item, parent, false);
//        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_places_detail_item, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.txtViewTitle.setText(itemsData[position].getTitle());
        viewHolder.txtViewText.setText(itemsData[position].getText());
        viewHolder.imgViewIcon.setImageResource(itemsData[position].getDrawable());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle, txtViewText;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.placesDetailTitle);
            txtViewText = (TextView) itemLayoutView.findViewById(R.id.placesDetailText);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.placesDetailDrawable);
        }
    }

    @Override
    public int getItemCount() {
        return itemsData.length;
    }
}
