package com.example.cityquest.adapter;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.R;
import com.example.cityquest.model.TripDay;

import java.util.ArrayList;
import java.util.List;

public class DaysDetailsAdapter extends RecyclerView.Adapter<DaysDetailsAdapter.DaysViewHolder> {

    private Context context;
    private List<TripDay> days;
    private List<Boolean> expandedStates; // Track expanded/collapsed states for each day

    public DaysDetailsAdapter(Context context, List<TripDay> days) {
        this.context = context;
        this.days = days;

        // Initialize expanded state to false for all days
        this.expandedStates = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            expandedStates.add(false); // By default, all items are collapsed
        }
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

        // Check if the current day is expanded or collapsed
        boolean isExpanded = expandedStates.get(position);
        holder.placesRecyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // Change the arrow direction based on the expanded state
        holder.toggleButton.setImageResource(isExpanded ? R.drawable.down_arrow_icon_vector : R.drawable.right_arrow_icon_vector);
        holder.editButton.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // Handle clicks to expand/collapse the card
        holder.dayNameLinearLayout.setOnClickListener(v -> toggleDayExpansion(holder, position));
        holder.toggleButton.setOnClickListener(v -> toggleDayExpansion(holder, position));
    }

    // Toggle the expansion of a specific day card
    private void toggleDayExpansion(DaysViewHolder holder, int position) {
        boolean currentlyExpanded = expandedStates.get(position);

        // Toggle the expanded state
        expandedStates.set(position, !currentlyExpanded);

        // Animate the layout changes
        TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);

        // Notify the adapter to refresh the item
        notifyItemChanged(position);
    }

    // Method to programmatically expand a specific day (called when a tab is selected)
    public void expandDay(int dayPosition) {
        // Collapse all other days
        for (int i = 0; i < expandedStates.size(); i++) {
            expandedStates.set(i, i == dayPosition); // Only expand the selected day
        }

        // Notify the adapter to update all items
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class DaysViewHolder extends RecyclerView.ViewHolder {

        TextView dayNumber;
        RecyclerView placesRecyclerView;
        ImageButton toggleButton;
        LinearLayout dayNameLinearLayout;
        ImageButton editButton;

        public DaysViewHolder(@NonNull View itemView) {
            super(itemView);
            dayNumber = itemView.findViewById(R.id.day_name);
            placesRecyclerView = itemView.findViewById(R.id.places_recycler_view);
            toggleButton = itemView.findViewById(R.id.toggle_button);
            dayNameLinearLayout = itemView.findViewById(R.id.day_name_linear_layout);
            editButton = itemView.findViewById(R.id.itinerary_edit_btn);
        }
    }

    public void updateDays(List<TripDay> newDays) {
        // Update the current list of days
        this.days = newDays;

        // Reset the expanded states
        expandedStates.clear();
        for (int i = 0; i < newDays.size(); i++) {
            expandedStates.add(false);
        }

        // Notify the adapter that the data has changed
        notifyDataSetChanged();
    }
}
