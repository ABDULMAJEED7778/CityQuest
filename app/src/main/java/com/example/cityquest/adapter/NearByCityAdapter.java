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
import com.example.cityquest.model.City;
import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.PlaceDetails;

import java.util.List;



public class NearByCityAdapter extends RecyclerView.Adapter<NearByCityAdapter.ViewHolder> {

    private List<PlaceDetails> cities;
    Context context;

    public NearByCityAdapter(Context context,List<PlaceDetails> cities) {

        this.cities = cities;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_city_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the view
        PlaceDetails city = cities.get(position);
        holder.placeName.setText(city.getName());
        holder.placeDescription.setText(city.getFormattedAddress());



        // Load photo bitmap if available, otherwise use Glide with placeholder
        Bitmap photoBitmap = city.getPhotoBitmap();
        if (photoBitmap != null) {
            holder.placeImage.setImageBitmap(photoBitmap);
        } else {
            Glide.with(context)
                    .load(R.drawable.image_placeholder) // Placeholder image in drawable
                    .placeholder(R.drawable.image_placeholder) // Placeholder image
                    .error(R.drawable.image_placeholder)
                    .into(holder.placeImage);
        }
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, placeDescription;
        ImageView favoriteButton, placeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.near_by_city_name);
            placeDescription = itemView.findViewById(R.id.near_by_city_desc);
            placeImage = itemView.findViewById(R.id.near_by_city_img);
        }
    }
}

