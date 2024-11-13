package com.example.cityquest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.PopupMenu;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cityquest.R;
import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.TravelInfo;
import com.example.cityquest.model.TravelInfoCache;
import com.example.cityquest.utils.GoogleMapsAPIsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    private Context context;
    private List<ItineraryPlace> places;

    private String selectedTravelMode = "driving"; // Default travel mode







    public PlacesAdapter(Context context, List<ItineraryPlace> places) {
        this.context = context;
        this.places = places;

        calculateDistancesForAllPlaces();



    }


    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itinerary_place_details_item, parent, false);
        return new PlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int position) {
        ItineraryPlace place = places.get(position);
        holder.placeName.setText(place.getPlaceName());
        holder.placeOverview.setText(place.getOverview());
        int placeNo = (int) (position + 1);
        holder.placeNumber.setText(placeNo+"");

        // Hide commute info for the last item or when only one place is present
        holder.commuteInfoLayout.setVisibility((position == (places.size() - 1) || places.size() == 1) ? View.GONE : View.VISIBLE);


        // Commute button click listener for mode change
        holder.commuteTypeBtn.setOnClickListener(v -> showCommuteOptionsMenu(holder));

        // Check for cached travel info
        TravelInfo travelInfo = TravelInfoCache.getInstance().getTravelInfo(selectedTravelMode, place.getPlaceId());
        if (travelInfo != null) {
            holder.commuteTime.setText(travelInfo.getDuration() + " (" + travelInfo.getDistance() + ")");
        } else {
            holder.commuteTime.setText("Loading...");
        }
        // Load the image using Glide or any other image loading library
//        Glide.with(context).load(place.getPhotoUrl()).into(holder.placeImage);
        Glide.with(context)
                .load(place.getPhotoUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16))) // Change radius as needed
                .into(holder.placeImage);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class PlacesViewHolder extends RecyclerView.ViewHolder {

        TextView placeName;
        TextView placeOverview;
        TextView commuteTime;
        ImageView placeImage;
        TextView placeNumber;
        LinearLayout commuteInfoLayout;
        ImageView commuteTypeBtn;

        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName= itemView.findViewById(R.id.place_name);
            placeOverview = itemView.findViewById(R.id.place_overview_adapter);
            placeImage = itemView.findViewById(R.id.place_image_adapter);
            placeNumber = itemView.findViewById(R.id.place_number);
            commuteInfoLayout = itemView.findViewById(R.id.commute_info_layout);
            commuteTypeBtn = itemView.findViewById(R.id.commute_type_btn);
            commuteTime = itemView.findViewById(R.id.commute_time); // Initialize the TextView
        }
    }

    private void showCommuteOptionsMenu(PlacesViewHolder holder) {
        Context wrapper = new ContextThemeWrapper(context, R.style.CustomPopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, holder.commuteTypeBtn);
        popup.getMenuInflater().inflate(R.menu.commute_time_popup_menu, popup.getMenu());

        // Set the previously selected item as checked
        switch (selectedTravelMode) {
            case "walking":
                popup.getMenu().findItem(R.id.walking).setChecked(true);
                break;
            case "driving":
                popup.getMenu().findItem(R.id.car).setChecked(true);
                popup.getMenu().findItem(R.id.bike).setChecked(true); // if bike is selected
                break;
            case "transit":
                popup.getMenu().findItem(R.id.metro).setChecked(true); // if metro is selected
                popup.getMenu().findItem(R.id.bus).setChecked(true); // if bus is selected
                break;
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Uncheck all items first
                for (int i = 0; i < popup.getMenu().size(); i++) {
                    popup.getMenu().getItem(i).setChecked(false);
                }
                // Handle the selected travel mode
                switch (item.getItemId()) {
                    case R.id.walking:
                        selectedTravelMode = "walking";
                        item.setChecked(true);
                        break;
                    case R.id.car:
                        selectedTravelMode = "driving";
                        item.setChecked(true);
                        break;
                    case R.id.bike:
                        selectedTravelMode = "driving";
                        item.setChecked(true);
                        break;
                    case R.id.metro:
                        selectedTravelMode = "transit";
                        item.setChecked(true);
                        break;
                    case R.id.bus:
                        selectedTravelMode = "transit";
                        item.setChecked(true);
                        break;
                    default:
                        return false;
                }
                calculateDistancesForAllPlaces();
                return true;
            }
        });

        popup.show();
    }

    // Method to calculate distances for all places using a single API call
    private void calculateDistancesForAllPlaces() {
        List<String> placeIds = new ArrayList<>();
        for (ItineraryPlace place : places) {
            placeIds.add(place.getPlaceId());
        }

        Log.e("travel", "from calculate distances for all places selectedTravelMode is " + selectedTravelMode);

        // If we already have cached data for the selected travel mode, just notify the adapter
        if (TravelInfoCache.getInstance().getTravelInfo(selectedTravelMode, placeIds.get(0)) != null) {
            notifyDataSetChanged();
            return;
        }

        GoogleMapsAPIsUtils googleMapsAPIsUtils = new GoogleMapsAPIsUtils(context, context.getString(R.string.google_maps_api_key));//TODO add api key

        googleMapsAPIsUtils.getDistancesForDay(placeIds, selectedTravelMode, new GoogleMapsAPIsUtils.DistanceCallback() {
            @Override
            public void onSuccess(List<TravelInfo> travelInfoList) {
                for (int i = 0; i < travelInfoList.size(); i++) {
                    TravelInfo travelInfo = travelInfoList.get(i);
                    // Cache the travel information using the singleton instance
                    TravelInfoCache.getInstance().addOrUpdateCache(selectedTravelMode, places.get(i).getPlaceId(), travelInfo);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("DistanceError", errorMessage);
            }
        });
    }



}
