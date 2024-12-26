package com.example.cityquest.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.City;
import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.TripDay;

import java.util.ArrayList;
import java.util.List;

public class ThingsToDoAdapter extends RecyclerView.Adapter<ThingsToDoAdapter.ViewHolder> {

    private List<ItineraryPlace> places;
    Context context;

    public ThingsToDoAdapter(Context context,List<ItineraryPlace> places) {

        this.places = places;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.things_to_do_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the view
        ItineraryPlace place = places.get(position);
        holder.placeName.setText(place.getPlaceName());
        holder.placeDescription.setText(place.getOverview());

        Glide.with(context)
                .load(place.getPhotoUrl()) // Placeholder image in drawable
                .into(holder.placeImage);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, placeDescription;
        ImageView favoriteButton, placeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.things_to_do_place_name);
            placeDescription = itemView.findViewById(R.id.things_to_do_place_desc);
            favoriteButton = itemView.findViewById(R.id.things_to_do_favorite);
            placeImage = itemView.findViewById(R.id.things_to_do_img);
        }
    }
}
