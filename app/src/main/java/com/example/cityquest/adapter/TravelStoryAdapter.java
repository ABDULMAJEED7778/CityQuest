package com.example.cityquest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.TravelStory;

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


        // Load the image using Glide
        if(story.getPhotoUrls() != null && !story.getPhotoUrls().isEmpty()){
            Glide.with(context)
                    .load(story.getPhotoUrls().get(0)) // Get the first photo URL
                    .into(holder.thumbnailImageView);
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
