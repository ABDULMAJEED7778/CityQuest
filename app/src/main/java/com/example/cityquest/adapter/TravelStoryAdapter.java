package com.example.cityquest.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cityquest.R;
import com.example.cityquest.ViewPostCommentsActivity;
import com.example.cityquest.model.TravelStory;
import com.example.cityquest.utils.PlayerManager;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class TravelStoryAdapter extends RecyclerView.Adapter<TravelStoryAdapter.ViewHolder> {

    private Context context;
    private List<TravelStory> travelStories;

    // Constructor
    public TravelStoryAdapter(Context context, List<TravelStory> travelStories) {
        this.context = context;
        this.travelStories = travelStories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.travel_story_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TravelStory story = travelStories.get(position);

        holder.titleTextView.setText(story.getTitle());
        holder.authorTextView.setText("By: " + story.getUserName());

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, ViewPostCommentsActivity.class);
            intent.putExtra("postId", story.getPostId());
            context.startActivity(intent);

        });



        if (story.getVideoUrl() != null && !story.getVideoUrl().isEmpty()) {

            RequestOptions requestOptions = new RequestOptions();
            Glide.with(context)
                    .load(story.getVideoUrl())
                    .apply(requestOptions)
                    .thumbnail(Glide.with(context).load(story.getVideoUrl()))
                    .placeholder(R.drawable.image_placeholder) // Replace with your placeholder drawable resource
                    .error(R.drawable.image_placeholder) // Optional: Image to display if loading fails
                    .into(holder.thumbnailImageView);
        } else if (story.getPhotoUrls() != null && !story.getPhotoUrls().isEmpty()) {


            Glide.with(context)
                    .load(story.getPhotoUrls().get(0)) // Get the first photo URL
                    .placeholder(R.drawable.image_placeholder) // Replace with your placeholder drawable resource
                    .error(R.drawable.image_placeholder) // Optional: Image to display if loading fails
                    .into(holder.thumbnailImageView);
        } else {

        }


    }

    @Override
    public int getItemCount() {
        return travelStories.size();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView;
        ImageView thumbnailImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.story_tilte);
            authorTextView = itemView.findViewById(R.id.story_author_name);

            thumbnailImageView = itemView.findViewById(R.id.story_img);
        }
    }



}
