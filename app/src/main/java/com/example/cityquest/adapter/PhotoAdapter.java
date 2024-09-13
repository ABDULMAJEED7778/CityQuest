package com.example.cityquest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<Photo> photos;

    public PhotoAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_details_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        Glide.with(holder.imageView.getContext())
                .load(photo.getUrl())
                .into(holder.imageView);

        // If you want to display photographer's name
        holder.photographerName.setText(photo.getPhotographer());
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        // Define view elements
        ImageView imageView;
         TextView photographerName;


        public PhotoViewHolder(View itemView) {
            super(itemView);
            // Initialize view elements
             imageView = itemView.findViewById(R.id.photo_image);
             photographerName = itemView.findViewById(R.id.photographer_name);
        }
    }
}

