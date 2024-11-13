package com.example.cityquest.adapter;

import android.content.Context;
import android.content.Intent;
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

import com.example.cityquest.EditTripActivity;
import com.example.cityquest.R;
import com.example.cityquest.model.TripDay;

import java.util.ArrayList;
import java.util.List;

public class EditDaysDetailsAdapter extends RecyclerView.Adapter<EditDaysDetailsAdapter.EditDaysViewHolder> {

    private Context context;
    private List<TripDay> days;
    private List<Boolean> expandedStates; // Track expanded/collapsed states for each day


    public EditDaysDetailsAdapter (Context context, List<TripDay> days) {
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
    public EditDaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_page_day_card_item, parent, false);
        return new EditDaysViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditDaysViewHolder holder, int position) {
        TripDay day = days.get(position);
        holder.dayNumber.setText("Day " + (position + 1));

        // Set up the PlacesAdapter for the nested RecyclerView
        EditPlacesAdapter editPlacesAdapter = new EditPlacesAdapter(context, day.getPlaces());
        holder.editPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.editPlacesRecyclerView.setAdapter(editPlacesAdapter);

        // Check if the current day is expanded or collapsed
        boolean isExpanded = expandedStates.get(position);
        holder.placesLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        // Change the arrow direction based on the expanded state
        holder.toggleButton.setImageResource(isExpanded ? R.drawable.down_arrow_icon_vector : R.drawable.right_arrow_icon_vector);

        // Handle clicks to expand/collapse the card
        holder.dayNameLinearLayout.setOnClickListener(v -> toggleDayExpansion(holder, position));
        holder.toggleButton.setOnClickListener(v -> toggleDayExpansion(holder, position));

    }

    // Toggle the expansion of a specific day card
    private void toggleDayExpansion(EditDaysViewHolder holder, int position) {
        boolean currentlyExpanded = expandedStates.get(position);

        // Toggle the expanded state
        expandedStates.set(position, !currentlyExpanded);

        // Animate the layout changes
        TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);

        // Notify the adapter to refresh the item
        notifyItemChanged(position);
    }

    // Method to programmatically expand a specific day (called when a tab is selected)
    public void expandDay (int dayPosition) {
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

    public static class EditDaysViewHolder extends RecyclerView.ViewHolder {

        TextView dayNumber;
        RecyclerView editPlacesRecyclerView;
        ImageButton toggleButton;
        LinearLayout dayNameLinearLayout;
        ImageButton daysDeleteBtn;
        ImageButton daysReorderBtn;
        LinearLayout placesLayout;

        public EditDaysViewHolder(@NonNull View itemView) {
            super(itemView);
            dayNumber = itemView.findViewById(R.id.day_name);
            editPlacesRecyclerView = itemView.findViewById(R.id.edit_places_recycler_view);
            toggleButton = itemView.findViewById(R.id.toggle_button);
            dayNameLinearLayout = itemView.findViewById(R.id.day_name_linear_layout);
            daysDeleteBtn = itemView.findViewById(R.id.days_delete_button);
            daysReorderBtn = itemView.findViewById(R.id.days_reorder_button);
            placesLayout = itemView.findViewById(R.id.places_layout);
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
