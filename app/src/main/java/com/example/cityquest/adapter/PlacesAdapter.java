package com.example.cityquest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.ItineraryPlace;

import java.util.List;


public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    private Context context;
    private List<ItineraryPlace> places;

    public PlacesAdapter(Context context, List<ItineraryPlace> places) {
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itinerary_place_details_item, parent, false);
        return new PlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int position) {
        ItineraryPlace place = places.get(position);
        holder.placeName.setText(place.getPlaceName());
        holder.placeOverview.setText(place.getOverview());
        int placeNo = (int) (position + 1);
        holder.placeNumber.setText(placeNo+"");



        if(position == (places.size() - 1) || places.size() == 1) {
            holder.commuteInfoLayout.setVisibility(View.GONE);
        }
        // Load the image using Glide or any other image loading library
        Glide.with(context).load(place.getPhotoUrl()).into(holder.placeImage);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class PlacesViewHolder extends RecyclerView.ViewHolder {

        TextView placeName;
        TextView placeOverview;
        ImageView placeImage;
        TextView placeNumber;
        LinearLayout commuteInfoLayout;

        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName= itemView.findViewById(R.id.place_name);
            placeOverview = itemView.findViewById(R.id.place_overview_adapter);
            placeImage = itemView.findViewById(R.id.place_image_adapter);
            placeNumber = itemView.findViewById(R.id.place_number);
            commuteInfoLayout = itemView.findViewById(R.id.commute_info_layout);
        }
    }
}
