package com.example.cityquest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.PopupMenu;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cityquest.R;
import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.TravelInfo;
import com.example.cityquest.model.TravelInfoCache;
import com.example.cityquest.utils.GoogleMapsAPIsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditPlacesAdapter extends RecyclerView.Adapter<EditPlacesAdapter.EditPlacesViewHolder> {

    private Context context;
    private List<ItineraryPlace> places;


    public EditPlacesAdapter(Context context, List<ItineraryPlace> places) {
        this.context = context;
        this.places = places;

    }


    @NonNull
    @Override
    public EditPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_page_place_details_item, parent, false);
        return new EditPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditPlacesViewHolder holder, int position) {
        ItineraryPlace place = places.get(position);
        holder.placeName.setText(place.getPlaceName());
        holder.placeOverview.setText(place.getOverview());
        int placeNo = (int) (position + 1);
        holder.placeNumber.setText(placeNo+"");



    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class EditPlacesViewHolder extends RecyclerView.ViewHolder {

        TextView placeName;
        TextView placeOverview;
        TextView placeNumber;

        public EditPlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName= itemView.findViewById(R.id.place_name);
            placeOverview = itemView.findViewById(R.id.place_overview_adapter);
            placeNumber = itemView.findViewById(R.id.place_number);
        }
    }


}
