package com.example.cityquest.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.cityquest.R;
import com.example.cityquest.ReadyPlanDetailsActivity;
import com.example.cityquest.model.ReadyTrips;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private List<ReadyTrips> tripList;
    private Context context;

    public TripAdapter(Context context, List<ReadyTrips> tripList) {
        this.context = context;
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
        ReadyTrips trip = tripList.get(position);
        holder.name.setText(trip.getName());
        holder.rating.setText(String.valueOf(trip.getRating()));
        holder.startEndDate.setText(trip.getStartDate() + " - " + trip.getEndDate());
        holder.companion.setText(trip.getCompanionType());

        // Load the background image from the URL
        Glide.with(context)
                .load(trip.getPhotoUrl())
                .into(new CustomViewTarget<RelativeLayout, Drawable>(holder.tripItemLayout) {
                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                        // Clear any existing resources
                        holder.tripItemLayout.setBackground(null);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        // Set the loaded image as the background of the RelativeLayout
                        holder.tripItemLayout.setBackground(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        // Handle the error scenario (optional)
                        // Optionally set a default background
                        holder.tripItemLayout.setBackground(errorDrawable);
                    }
                });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReadyPlanDetailsActivity.class);
            intent.putExtra("tripId", trip.getTripId()); // Pass the trip ID to the detail activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView name, rating, startEndDate, companion;
        RelativeLayout tripItemLayout; // Add an ImageView for the trip photo

        public TripViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.trip_name_txt);
            rating = itemView.findViewById(R.id.rating_trip_txt);
            startEndDate = itemView.findViewById(R.id.trip_date_range);
            companion = itemView.findViewById(R.id.trip_companion_txt);
            tripItemLayout = itemView.findViewById(R.id.trip_item_rl);
        }
    }
}
