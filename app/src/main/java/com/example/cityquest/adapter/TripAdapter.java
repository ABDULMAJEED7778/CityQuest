package com.example.cityquest.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.R;
import com.example.cityquest.model.City;
import com.example.cityquest.model.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private List<Trip> tripList;

    public TripAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.textViewTripName.setText(trip.getName());
        holder.textViewCompanion.setText(trip.getCompanion());
        holder.textViewTripRating.setText(trip.getRating());
        holder.textViewTripDateRange.setText(trip.getDateRange());


        // Assume you have a method to load images
        //Glide.with(holder.imageViewCity.getContext()).load(city.getImageUrl()).into(holder.imageViewCity);
        holder.relativeLayout.setBackgroundResource(trip.getBackgroundDrawable());

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView textViewTripName, textViewCompanion,textViewTripRating,textViewTripDateRange;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.trip_item_rl);
            textViewTripName = itemView.findViewById(R.id.trip_name_txt);
            textViewCompanion = itemView.findViewById(R.id.trip_companion_txt);
            textViewTripRating = itemView.findViewById(R.id.rating_trip_txt);
            textViewTripDateRange = itemView.findViewById(R.id.trip_date_range);
        }
    }
}

