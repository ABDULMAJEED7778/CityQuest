package com.example.cityquest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.PlaceDetails;

import java.util.List;

public class ExplorePlaceAdapter extends RecyclerView.Adapter<ExplorePlaceAdapter.PlaceViewHolder> {

    private List<PlaceDetails> placeDetailsList;
    private Context context;

    public ExplorePlaceAdapter(Context context, List<PlaceDetails> placeDetailsList) {
        this.context = context;
        this.placeDetailsList = placeDetailsList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.explore_places_item, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        PlaceDetails placeDetails = placeDetailsList.get(position);
        // Set name
        holder.nameTextView.setText(placeDetails.getName() != null ? placeDetails.getName() : "Unknown Place");

        // Set formatted address
        holder.addressTextView.setText(placeDetails.getFormattedAddress() != null ? placeDetails.getFormattedAddress() : "Address not available");

        // Set open status
        holder.openNowTextView.setText(placeDetails.isOpenNow() ? "Open Now" : "Closed");

        // Set rating
        holder.ratingTextView.setText(String.valueOf(placeDetails.getRating()));

        // Load photo bitmap if available, otherwise use Glide with placeholder
        Bitmap photoBitmap = placeDetails.getPhotoBitmap();
        if (photoBitmap != null) {
            holder.photoImageView.setImageBitmap(photoBitmap);
        } else {
            Glide.with(context)
                    .load(R.drawable.bangalore) // Placeholder image in drawable
                    .placeholder(R.drawable.image_placeholder) // Placeholder image
                    .error(R.drawable.image_placeholder)
                    .into(holder.photoImageView);
        }
    }

    @Override
    public int getItemCount() {
        return placeDetailsList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, addressTextView, ratingTextView,openNowTextView,priceTextView;
        ImageView photoImageView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.place_name_explore);
            addressTextView = itemView.findViewById(R.id.place_location_explore);
            ratingTextView = itemView.findViewById(R.id.place_rating_explore);
            openNowTextView = itemView.findViewById(R.id.place_status_openNow);

            photoImageView = itemView.findViewById(R.id.place_image_explore);
        }
    }
}

