package com.example.cityquest.adapter;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.ReadyTrips;

import java.util.ArrayList;
public class UploadedPostPhotosAdapter extends RecyclerView.Adapter<UploadedPostPhotosAdapter.PhotoViewHolder> {

    private final ArrayList<Uri> photoList;
    Context context;

    public UploadedPostPhotosAdapter(Context context,ArrayList<Uri> photoList) {
        this.photoList = photoList;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uploaded_post_images_item, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri photoUri = photoList.get(position);


        Glide.with(holder.photoImageView.getContext())
                .load(photoUri)
                .into(holder.photoImageView);

        if (photoList.size() <= 1) {
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            holder.deleteButton.setVisibility(View.VISIBLE);
        }
        holder.deleteButton.setOnClickListener(v -> {

            showDeleteDialog(position);
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        ImageButton deleteButton;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.uploaded_photo_imageView);
            deleteButton = itemView.findViewById(R.id.delete_photos_btn);
        }
    }
    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Photo")
                .setMessage("Are you sure you want to delete this photo?")
                .setPositiveButton("DELETE", (dialog, which) -> {


                    if (position >= 0 && position < photoList.size()) {
                        photoList.remove(position);
                        notifyDataSetChanged(); // Safely update the entire list
                    } else {
                        Log.e("DeletePhotoError", "Invalid position: " + position);
                    }


                })
                .setNegativeButton("CANCEL", null)
                .show();
    }
}