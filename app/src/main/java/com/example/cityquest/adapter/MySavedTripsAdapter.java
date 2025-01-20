package com.example.cityquest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.ReadyPlanDetailsActivity;
import com.example.cityquest.model.ReadyTrips;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MySavedTripsAdapter extends RecyclerView.Adapter<MySavedTripsAdapter.TripViewHolder> {
    private List<ReadyTrips> tripList;
    private Context context;

    public MySavedTripsAdapter(Context context, List<ReadyTrips> tripList) {
        this.context = context;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_trips_item, parent, false);
        return new MySavedTripsAdapter.TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        ReadyTrips trip = tripList.get(position);
        holder.name.setText(trip.getName());

        // Calculate and set days left
        long daysLeft = getDaysLeft(trip.getStartDate());
        holder.daysLeft.setText(daysLeft > 0 ? daysLeft + " days left" : "Started");



        holder.startEndDate.setText(trip.getStartDate() + " - " + trip.getEndDate());
        holder.companion.setText(trip.getCompanionType());



        Glide.with(context)
                .load(trip.getPhotoUrl())
                .placeholder(R.drawable.image_placeholder) // Replace with your placeholder drawable resource
                .error(R.drawable.image_placeholder) // Optional: Image to display if loading fails
                .into(holder.tripItemImageView);


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
        TextView name, startEndDate, companion,daysLeft;
        ImageView tripItemImageView; // Add an ImageView for the trip photo

        public TripViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.trip_name_txt_myTrips);
            daysLeft = itemView.findViewById(R.id.days_left_myTrips);
            startEndDate = itemView.findViewById(R.id.trip_date_range_myTrips);
            companion = itemView.findViewById(R.id.trip_companion_txt_myTrips);
            tripItemImageView = itemView.findViewById(R.id.trip_image_myTrips);
        }
    }

    private long getDaysLeft(String startDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = dateFormat.parse(startDateString);
            Date currentDate = new Date(); // Current date
            long diffInMillis = startDate.getTime() - currentDate.getTime();
            return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Return 0 if there was an error parsing
        }
    }

}