package com.example.cityquest.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.cityquest.R;
import com.example.cityquest.ReadyPlanDetailsActivity;
import com.example.cityquest.SignInActivity;
import com.example.cityquest.model.ReadyTrips;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private List<ReadyTrips> tripList;
    private Context context;
    private OnFavoriteTripActionListener favoriteTripActionListener;
    private Set<String> savedTripIds;



    public TripAdapter(Context context, List<ReadyTrips> tripList, OnFavoriteTripActionListener listener) {
        this.context = context;
        this.tripList = tripList;
        this.favoriteTripActionListener = listener;
        this.savedTripIds = new HashSet<>(); // Initialize with an empty set


    }

    public void updateSavedTrips(Set<String> savedTripIds) {
        this.savedTripIds.clear();
        this.savedTripIds.addAll(savedTripIds);
        notifyDataSetChanged(); // Refresh UI
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

        // Set favorite icon based on saved state
        if (savedTripIds.contains(trip.getTripId())) {
            holder.favoriteTrip.setImageResource(R.drawable.heart_icon_filled); // Change to filled heart
        } else {
            holder.favoriteTrip.setImageResource(R.drawable.heart_icon); // Change to border heart
        }

        // Handle favoriteTrip click
        holder.favoriteTrip.setOnClickListener(v -> {
            if (favoriteTripActionListener.isUserSignedIn()) {
                if (savedTripIds.contains(trip.getTripId())) {
                    // Remove from saved trips
                    showRemoveDialog(trip,position);

                } else {
                    showSaveDialog(trip,position);
                }
            } else {
                favoriteTripActionListener.showSignInDialog(() -> {
                    // After successful sign-in, show save dialog

                        Log.e("TripAdapter", "savedTripIds"+savedTripIds +" \ntripId" +trip.getTripId());
                    if (savedTripIds.contains(trip.getTripId())) {
                        // Remove from saved trips
                        showRemoveDialog(trip,position);

                    } else {
                        showSaveDialog(trip,position);
                    }
                });
            }
        });
    }

    private void showSaveDialog(ReadyTrips trip, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Save Trip")
                .setMessage("Do you want to save this trip?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    // Add to saved trips
                    savedTripIds.add(trip.getTripId());
                    favoriteTripActionListener.saveTripToFirestore(trip);

                    notifyItemChanged(position); // Update the specific item

                })
                .setNegativeButton("No", null)
                .show();
    }
    private void showRemoveDialog(ReadyTrips trip, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Remove Trip")
                .setMessage("Remove this trip from your saved trips?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    // Remove from saved trips
                    savedTripIds.remove(trip.getTripId());
                    favoriteTripActionListener.removeTripFromFirestore(trip);

                    notifyItemChanged(position); // Update the specific item

                })
                .setNegativeButton("No", null)
                .show();
    }




    @Override
    public int getItemCount() {
        return tripList.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView name, rating, startEndDate, companion;
        ImageView tripItemImageView,favoriteTrip; // Add an ImageView for the trip photo

        public TripViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.trip_name_txt);
            rating = itemView.findViewById(R.id.rating_trip_txt);
            startEndDate = itemView.findViewById(R.id.trip_date_range);
            companion = itemView.findViewById(R.id.trip_companion_txt);
            tripItemImageView = itemView.findViewById(R.id.trip_image_view);
            favoriteTrip = itemView.findViewById(R.id.favorite_trip);
        }
    }

    public interface OnFavoriteTripActionListener {
        boolean isUserSignedIn();
        void showSignInDialog(OnSignInSuccessListener listener);
        void saveTripToFirestore(ReadyTrips trip);
        void removeTripFromFirestore(ReadyTrips trip);


        interface OnSignInSuccessListener {
            void onSignInSuccess();
        }
    }

}
