package com.example.cityquest.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.Photo;

import java.util.List;



public class PostImageSliderAdapter extends RecyclerView.Adapter<PostImageSliderAdapter.ImageViewHolder> {
    private Context context;
    private List<String> imageUrls; // List of image URLs or URIs

    public PostImageSliderAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    public void addPhoto(String uri) {
        // Create a Photo object with the URL and photographer name

        imageUrls.add(uri);

        notifyItemInserted(imageUrls.size() - 1);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_image_slider_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Glide.with(context)
                .load(imageUrl) // Load image using Glide or Picasso
                .placeholder(R.drawable.image_placeholder) // Placeholder image
                .error(R.drawable.image_placeholder) // Error image
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;


        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.post_image_slider_imageView);

        }
    }
}


