package com.example.cityquest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.TripDay;

import java.util.List;

public class DaysDetailsAdapter extends  RecyclerView.Adapter<DaysDetailsAdapter.DaysViewHolder>{

    private Context context;
    private List<TripDay> days;

    public DaysDetailsAdapter (Context context, List<TripDay> days) {
        this.context = context;
        this.days = days;
    }

    @NonNull
    @Override
    public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_itinerary_card_item, parent, false);
        return new DaysViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysViewHolder holder, int position) {
        TripDay day = days.get(position);
        holder.dayNumber.setText("Day " + (position + 1));

        // Set up the PlacesAdapter for the nested RecyclerView
        PlacesAdapter placesAdapter = new PlacesAdapter(context, day.getPlaces());
        holder.placesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.placesRecyclerView.setAdapter(placesAdapter);

    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class DaysViewHolder extends RecyclerView.ViewHolder {

        TextView dayNumber;
        RecyclerView placesRecyclerView;
        public DaysViewHolder (@NonNull View itemView) {
            super(itemView);
            dayNumber = itemView.findViewById(R.id.day_name);
            placesRecyclerView = itemView.findViewById(R.id.places_recycler_view);

        }
    }

    public void updateDays(List<TripDay> newDays) {
        // Update the current list of days
        this.days = newDays;

        // Notify the adapter that the data has changed
        notifyDataSetChanged();
    }

}
