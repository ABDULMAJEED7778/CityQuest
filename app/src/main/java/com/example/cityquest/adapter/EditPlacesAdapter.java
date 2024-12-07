package com.example.cityquest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.PopupMenu;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cityquest.R;
import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.TravelInfo;
import com.example.cityquest.model.TravelInfoCache;
import com.example.cityquest.utils.GoogleMapsAPIsUtils;
import com.example.cityquest.utils.ItemMoveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EditPlacesAdapter extends RecyclerView.Adapter<EditPlacesAdapter.EditPlacesViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private Context context;
    private List<ItineraryPlace> places;
    private ItemTouchHelper placesItemTouchHelper;


    public EditPlacesAdapter(Context context, List<ItineraryPlace> places) {
        this.context = context;
        this.places = places;

    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.placesItemTouchHelper = itemTouchHelper;
    }


    @NonNull
    @Override
    public EditPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_page_place_details_item, parent, false);
        return new EditPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditPlacesViewHolder holder, int position) {
        ItineraryPlace place = places.get(position);
        holder.placeName.setText(place.getPlaceName());
        holder.placeOverview.setText(place.getOverview());
        int placeNo = (int) (position + 1);
        holder.placeNumber.setText(placeNo+"");

        // Handle the delete button click
        holder.deletePlaceButton.setOnClickListener(v -> {
            places.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, places.size());
        });

        holder.reorderPlaceButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    placesItemTouchHelper.startDrag(holder); // Start drag on button touch
                    return true; // Indicate that the event is consumed
                case MotionEvent.ACTION_UP:
                    // Call performClick() when the button is released
                    return true; // Indicate that the event is consumed
                case MotionEvent.ACTION_CANCEL:
                    // Handle cancel event
                    return true;
            }
            return false;
        });


    }
    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(places, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(places, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(fromPosition, Math.abs(toPosition - fromPosition) + 1);
        notifyItemChanged(toPosition); // Notify change at the new position

    }

    @Override
    public void onRowSelected(RecyclerView.ViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(RecyclerView.ViewHolder myViewHolder) {

    }


    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class EditPlacesViewHolder extends RecyclerView.ViewHolder {

        TextView placeName;
        TextView placeOverview;
        TextView placeNumber;
        ImageButton deletePlaceButton;
        ImageButton reorderPlaceButton;

        public EditPlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName= itemView.findViewById(R.id.place_name);
            placeOverview = itemView.findViewById(R.id.place_overview_adapter);
            placeNumber = itemView.findViewById(R.id.place_number);
            deletePlaceButton = itemView.findViewById(R.id.remove_place_button);
            reorderPlaceButton = itemView.findViewById(R.id.reorder_button);
        }
    }


}
