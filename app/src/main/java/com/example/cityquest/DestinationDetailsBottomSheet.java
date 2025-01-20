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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityquest.adapter.PhotoAdapter;
import com.example.cityquest.adapter.PostImageSliderAdapter;
import com.example.cityquest.model.ReadyTrips;
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

    ViewPager2 viewPagerPhotos;
    ImageView leftArrow, rightArrow;

    private Button selectBtn, viewOnMap;
    private TextView cityNameTextView, CityOverfiewTextView;
    private PhotoAdapter photosAdapter;
    private PlacesClient placesClient; // Declare PlacesClient here
    private DestinationFragment parentFragment; // Reference to the parent fragment
    PostImageSliderAdapter postImageSliderAdapter;
    private TabLayout tabLayout;
    List<String> imageUrls =new ArrayList<>();




    public static DestinationDetailsBottomSheet newInstance(String placeId, String cityName, DestinationFragment parentFragment) {
        DestinationDetailsBottomSheet fragment = new DestinationDetailsBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_PLACE_ID, placeId);
        args.putString(ARG_CITY_NAME, cityName);
        fragment.setArguments(args);
        fragment.parentFragment = parentFragment;

        return fragment;
    }

    @NonNull
    @Override
    public int getTheme() {
        return R.style.MyBottomSheetDialogTheme; // Use the defined theme here
    }
//

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_destination_details, container, false);

        // Initialize ViewPager2

        selectBtn = view.findViewById(R.id.select_btn_detail);
        cityNameTextView = view.findViewById(R.id.city_name_txt);
        CityOverfiewTextView = view.findViewById(R.id.city_details);
        photosAdapter = new PhotoAdapter(new ArrayList<>());
        leftArrow = view.findViewById(R.id.left_arrow_dest);
        rightArrow = view.findViewById(R.id.right_arrow_dest);
        viewPagerPhotos = view.findViewById(R.id.view_pager_photos_dest);



        placesClient = Places.createClient(getContext());
        tabLayout = view.findViewById(R.id.tabLayout_dest);






        Bundle args = getArguments();
        if (args != null) {
            String placeId = args.getString(ARG_PLACE_ID);
            String cityName = args.getString(ARG_CITY_NAME);

            ReadyTrips trip = new ReadyTrips(); // Create a new Trip object
            trip.setCity(cityName); // Set the destination
            trip.setDestinationId(placeId);


            fetchPlacePhotos(placeId);

            selectBtn.setOnClickListener(v -> {
//                Intent intent = new Intent(getContext(), DateRangeActivity.class);
//                intent.putExtra("trip", trip);
//                startActivity(intent);


                Toast.makeText(getContext(),parentFragment +"", Toast.LENGTH_SHORT).show();
                if (parentFragment != null) {
                    parentFragment.navigateToDateFragment(trip);
                }
                dismiss();

            });
        }

        return view;
    }

    private void fetchPlacePhotos(String placeId) {

        postImageSliderAdapter = new PostImageSliderAdapter(getContext(),imageUrls);
        viewPagerPhotos.setAdapter(postImageSliderAdapter);
        // Request DISPLAY_NAME, PHOTO_METADATAS, and EDITORIAL_SUMMARY fields
        final List<Place.Field> fields = Arrays.asList(
                Place.Field.NAME,
                Place.Field.PHOTO_METADATAS
        );

        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            final Place place = response.getPlace();
            // Get the city name (DISPLAY_NAME)
            String displayName = place.getName();
            if (displayName != null) {
                cityNameTextView.setText(displayName);
            }



            // Get the photo metadata
            final List<PhotoMetadata> metadataList = place.getPhotoMetadatas();
            if (metadataList == null || metadataList.isEmpty()) {
                Log.w("PlaceBottomSheet", "No photo metadata available.");
                return;
            }


            imageUrls.clear();
            // Track successful and failed fetches
            final int[] fetchCount = {0};
            final int[] failureCount = {0};


            // Loop through each photo metadata and request the photo URI
            for (PhotoMetadata photoMetadata : metadataList) {
                // Create a FetchResolvedPhotoUriRequest for each photo
                FetchResolvedPhotoUriRequest photoRequest = FetchResolvedPhotoUriRequest.builder(photoMetadata)
                        .setMaxWidth(500) // Adjust width and height as needed
                        .setMaxHeight(300)
                        .build();

                // Request the photo URI
                placesClient.fetchResolvedPhotoUri(photoRequest).addOnSuccessListener(fetchResolvedPhotoUriResponse -> {
                    String uri = fetchResolvedPhotoUriResponse.getUri().toString();
                    String photographer = photoMetadata.getAttributions(); // Get photographer attribution if available

                    // Pass the Uri and photographer name to the adapter

                    imageUrls.add(uri);
                    // Increment the fetch count
                    fetchCount[0]++;

                    if (fetchCount[0] + failureCount[0] == metadataList.size()) {
                        // Notify the adapter that data has changed
                        postImageSliderAdapter.notifyDataSetChanged();

                        // Set the ViewPager adapter after data is available
                        viewPagerPhotos.setAdapter(postImageSliderAdapter);

                        // Update UI for visibility of arrows and tabs
                        updateUIForImages();
                    }



                }).addOnFailureListener(exception -> {
                    Log.e("PlaceBottomSheet", "Error fetching photo: " + exception.getMessage());
                    failureCount[0]++; // Increment failure count

                    // Continue with the fetch process even if one fails
                    if (fetchCount[0] + failureCount[0] == metadataList.size()) {
                        // Notify the adapter even if some photos failed
                        postImageSliderAdapter.notifyDataSetChanged();

                        // Set the ViewPager adapter after data is available
                        viewPagerPhotos.setAdapter(postImageSliderAdapter);

                        // Update UI for visibility of arrows and tabs
                        updateUIForImages();
                    }
                });
            }
            Toast.makeText(getContext(), "photos"+postImageSliderAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "photos"+postImageSliderAdapter, Toast.LENGTH_SHORT).show();



                if (imageUrls.size() > 0) {




                    if(imageUrls.size() >1){

                        leftArrow.setVisibility(View.VISIBLE);
                        rightArrow.setVisibility(View.VISIBLE);
                        new TabLayoutMediator(tabLayout, viewPagerPhotos, (tab, p) -> {
                            // Some implementation (if needed, e.g., set tab text)
                        }).attach();

                        // Update arrow visibility based on the current photo position
                        viewPagerPhotos.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageSelected(int position) {
                                super.onPageSelected(position);

                                // Show/hide left arrow
                                if (position == 0) {
                                    leftArrow.setVisibility(View.GONE);
                                } else {
                                    leftArrow.setVisibility(View.VISIBLE);
                                }

                                // Show/hide right arrow
                                if (position == imageUrls.size()  - 1) {
                                    rightArrow.setVisibility(View.GONE);
                                } else {
                                   rightArrow.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        // Arrow click listeners for manual navigation
                        leftArrow.setOnClickListener(v -> {
                            int currentItem = viewPagerPhotos.getCurrentItem();
                            if (currentItem > 0) {
                                viewPagerPhotos.setCurrentItem(currentItem - 1);
                            }
                        });

                        rightArrow.setOnClickListener(v -> {
                            int currentItem = viewPagerPhotos.getCurrentItem();
                            if (currentItem < imageUrls.size()  - 1) {
                                viewPagerPhotos.setCurrentItem(currentItem + 1);
                            }
                        });

                        tabLayout.setVisibility(View.VISIBLE);
                    }else {
                        tabLayout.setVisibility(View.GONE);

                        // Hide arrows if only one photo
                        leftArrow.setVisibility(View.GONE);
                        rightArrow.setVisibility(View.GONE);


                    }
                }
        }).addOnFailureListener(exception -> {
            if (exception instanceof ApiException) {
                Log.e("PlaceBottomSheet", "Error fetching place details: " + ((ApiException) exception).getStatusCode());
            }
        });

    }

    private void updateUIForImages() {
        if (imageUrls.size() > 0) {
            if (imageUrls.size() > 1) {
                // Show arrows and tabs when there are multiple images
                leftArrow.setVisibility(View.VISIBLE);
                rightArrow.setVisibility(View.VISIBLE);

                new TabLayoutMediator(tabLayout, viewPagerPhotos, (tab, position) -> {
                    // Optional: set tab text here
                }).attach();

                // Update arrow visibility based on the current photo position
                viewPagerPhotos.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);

                        // Show/hide left arrow
                        leftArrow.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

                        // Show/hide right arrow
                        rightArrow.setVisibility(position == imageUrls.size() - 1 ? View.GONE : View.VISIBLE);
                    }
                });

                // Arrow click listeners for manual navigation
                leftArrow.setOnClickListener(v -> {
                    int currentItem = viewPagerPhotos.getCurrentItem();
                    if (currentItem > 0) {
                        viewPagerPhotos.setCurrentItem(currentItem - 1);
                    }
                });

                rightArrow.setOnClickListener(v -> {
                    int currentItem = viewPagerPhotos.getCurrentItem();
                    if (currentItem < imageUrls.size() - 1) {
                        viewPagerPhotos.setCurrentItem(currentItem + 1);
                    }
                });

                tabLayout.setVisibility(View.VISIBLE);
            } else {
                // If only one image, hide the TabLayout and arrows
                tabLayout.setVisibility(View.GONE);
                leftArrow.setVisibility(View.GONE);
                rightArrow.setVisibility(View.GONE);
            }
        } else {
            // If no images are available, show a placeholder or error message
            Log.w("PlaceBottomSheet", "No images available to display.");
        }


    }
}