package com.example.cityquest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cityquest.adapter.PhotoAdapter;
import com.example.cityquest.model.Trip;
import com.example.cityquest.model.Trips;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.HeroCarouselStrategy;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class DestinationDetailsBottomSheet extends BottomSheetDialogFragment {


    private static final String ARG_PLACE_ID = "place_id";
    private static final String ARG_CITY_NAME = "city_name";
    private RecyclerView recyclerView;
    private Button selectBtn, viewOnMap;
    private TextView cityNameTextView, CityOverfiewTextView;
    private PhotoAdapter photosAdapter;
    private PlacesClient placesClient; // Declare PlacesClient here



    public static DestinationDetailsBottomSheet newInstance(String placeId, String cityName) {
        DestinationDetailsBottomSheet fragment = new DestinationDetailsBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_PLACE_ID, placeId);
        args.putString(ARG_CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public int getTheme() {
        return R.style.MyBottomSheetDialogTheme; // Use the defined theme here
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_destination_details, container, false);

        // Initialize ViewPager2
        recyclerView = view.findViewById(R.id.recyclerView_CityDetails);
        selectBtn = view.findViewById(R.id.select_btn_detail);
        cityNameTextView = view.findViewById(R.id.city_name_txt);
        CityOverfiewTextView = view.findViewById(R.id.city_details);
        photosAdapter = new PhotoAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new CarouselLayoutManager());
        recyclerView.setAdapter(photosAdapter);


        // Initialize PlacesClient
        placesClient = Places.createClient(requireContext());


        Bundle args = getArguments();
        if (args != null) {
            String placeId = args.getString(ARG_PLACE_ID);
            String cityName = args.getString(ARG_CITY_NAME);

            Trips trip = new Trips(); // Create a new Trip object
            trip.setDestination(cityName); // Set the destination
            trip.setTripId(placeId);


            fetchPlacePhotos(placeId);

            selectBtn.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DateRangeActivity.class);
                intent.putExtra("trip", trip);
                startActivity(intent);
            });
        }

        return view;
    }

    private void fetchPlacePhotos(String placeId) {
        // Request DISPLAY_NAME, PHOTO_METADATAS, and EDITORIAL_SUMMARY fields
        final List<Place.Field> fields = Arrays.asList(
                Place.Field.NAME,
                Place.Field.PHOTO_METADATAS,
                Place.Field.EDITORIAL_SUMMARY
        );

        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            final Place place = response.getPlace();
            // Get the city name (DISPLAY_NAME)
            String displayName = place.getName();
            if (displayName != null) {
                cityNameTextView.setText(displayName);
            }

            // Get the city overview (EDITORIAL_SUMMARY)
            Log.e("hellow", "place.getEditorialSummary()");
            String overview = place.getEditorialSummary() != null ? place.getEditorialSummary() : "No overview available";

            CityOverfiewTextView.setText(overview); // Set the city overview

            // Get the photo metadata
            final List<PhotoMetadata> metadataList = place.getPhotoMetadatas();
            if (metadataList == null || metadataList.isEmpty()) {
                Log.w("PlaceBottomSheet", "No photo metadata available.");
                return;
            }

            for (PhotoMetadata photoMetadata : metadataList) {
                // Create a FetchResolvedPhotoUriRequest for each photo
                final FetchResolvedPhotoUriRequest photoRequest = FetchResolvedPhotoUriRequest.builder(photoMetadata)
                        .setMaxWidth(500) // Adjust width and height as needed
                        .setMaxHeight(300)
                        .build();

                // Request the photo URI
                placesClient.fetchResolvedPhotoUri(photoRequest).addOnSuccessListener((fetchResolvedPhotoUriResponse) -> {
                    Uri uri = fetchResolvedPhotoUriResponse.getUri();
                    String photographer = photoMetadata.getAttributions(); // Get photographer attribution if available

                    // Pass the Uri and photographer name to the adapter
                    photosAdapter.addPhoto(uri, photographer);
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        final ApiException apiException = (ApiException) exception;
                        Log.e("PlaceBottomSheet", "Error fetching photo: " + exception.getMessage());
                    }
                });
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e("PlaceBottomSheet", "Error fetching place details: " + ((ApiException) exception).getStatusCode());
            }
        });
    }

}