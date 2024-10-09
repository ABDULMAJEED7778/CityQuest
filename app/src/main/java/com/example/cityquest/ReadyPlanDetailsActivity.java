package com.example.cityquest;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;

import com.example.cityquest.adapter.PlacesAdapter;
import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.TripDay;
import com.example.cityquest.utils.NonScrollableScrollView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.time.format.DateTimeFormatter;

import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReadyPlanDetailsActivity extends AppCompatActivity {

    private NonScrollableScrollView scrollView;
    private TabLayout detailsTabLayout;
    private TextView overviewSection;
    private LinearLayout exploreSection;
    private TextView planName;
    private TextView noOfDays;
    private TextView cityCountry ;
    private TextView weatherCondition;
    private TextView planRating;
    private TextView tripType;
    private TextView companion;
    private ImageButton diningRecommendationBtn;
    private GridLayout diningRecommendationList;
    private FrameLayout itineraryFragmentContainer;
    private int numberOfDays;
    private String tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_plan_page);



        // Initialize UI elements
        detailsTabLayout = findViewById(R.id.tab_layout_details);
        overviewSection = findViewById(R.id.overview_section);
        exploreSection = findViewById(R.id.explore_section);
        scrollView = findViewById(R.id.scrollview);
        diningRecommendationBtn = findViewById(R.id.dining_recommendation_listing_btn);
        diningRecommendationList = findViewById(R.id.dining_recomendation_grid_layout);
        planName = findViewById(R.id.plan_name);
        noOfDays = findViewById(R.id.no_of_days);
        cityCountry = findViewById(R.id.city_location);
        weatherCondition = findViewById(R.id.weather_condation);
        planRating = findViewById(R.id.plan_rating);
        tripType = findViewById(R.id.trip_type);
        companion = findViewById(R.id.companion_type);
        itineraryFragmentContainer = findViewById(R.id.itinerary_fragment_container);

        scrollView.setScrollable(true);

        // Get trip ID from intent
        tripId = getIntent().getStringExtra("tripId");

        // Fetch trip details from Firestore
        fetchTripDetails(tripId);

        detailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
              @Override
              public void onTabSelected(@NonNull TabLayout.Tab tab) {
                  scrollView.post(() -> {
                      switch (tab.getPosition()) {
                          case 0: // First Tab (Overview)
                              overviewSection.setVisibility(View.VISIBLE);
                              removeItineraryFragment();
                              exploreSection.setVisibility(View.GONE);
                              itineraryFragmentContainer.setVisibility(View.GONE);
                              break;

                          case 1: // Second Tab (Itinerary)
                              overviewSection.setVisibility(View.GONE);
                              itineraryFragmentContainer.setVisibility(View.VISIBLE);
                              loadItineraryFragment(new ItineraryLoadCallback() {
                                  @Override
                                  public void onItineraryLoaded() {
                                      // Use post to ensure the layout is updated before scrolling
                                      smoothScrollToView(scrollView, detailsTabLayout);
                                      detailsTabLayout.setBackgroundResource(R.color.primary_color_light);
                                      detailsTabLayout.setSelectedTabIndicatorColor(
                                              ResourcesCompat.getColor(getResources(), R.color.primary_color, getTheme())
                                      );
                                      // Set the text color for unselected and selected tabs
                                      detailsTabLayout.setTabTextColors(
                                              ResourcesCompat.getColor(getResources(), R.color.background_color, getTheme()), // Unselected
                                              ResourcesCompat.getColor(getResources(), R.color.primary_color, getTheme()) // Selected
                                      );

//                                      // Set background color for the itinerary tab
//                                      tab.view.setBackgroundColor(
//                                              ResourcesCompat.getColor(getResources(), R.color.background_color, getTheme())
//                                      );
                                      getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_color_light));
                                  }
                              });
                              exploreSection.setVisibility(View.GONE);
                              scrollView.setScrollable(false);

                              break;

                          case 2: // Third Tab (Explore)
                              overviewSection.setVisibility(View.GONE);
                              itineraryFragmentContainer.setVisibility(View.GONE);
                              removeItineraryFragment();
                              exploreSection.setVisibility(View.VISIBLE);
                              setupDiningRecommendation();

                              // Scroll to the Explore Section
                              scrollView.smoothScrollTo(0, overviewSection.getHeight() + detailsTabLayout.getHeight() + itineraryFragmentContainer.getHeight());

                              break;
                      }
                  });
              }


              @Override
              public void onTabUnselected(TabLayout.Tab tab) {
                  if (tab.getPosition() == 1) {
                      scrollView.setScrollable(true);
                      // Reset background and tab indicator colors
                      detailsTabLayout.setBackgroundColor(
                              ResourcesCompat.getColor(getResources(), R.color.background_color, getTheme())
                      );
                      detailsTabLayout.setSelectedTabIndicatorColor(
                              ResourcesCompat.getColor(getResources(), R.color.primary_color, getTheme())
                      );

                      // Set the text color for unselected and selected tabs
                      detailsTabLayout.setTabTextColors(
                              ResourcesCompat.getColor(getResources(), R.color.gray, getTheme()), // Unselected
                              ResourcesCompat.getColor(getResources(), R.color.primary_color, getTheme()) // Selected
                      );

                      // Reset the status bar color
                      getWindow().setStatusBarColor(getColor(R.color.transparent));
                  }

              }

              @Override
              public void onTabReselected(TabLayout.Tab tab) {
                  if (tab.getPosition() == 1) {
                      scrollView.setScrollable(false);
                  }

              }
          }
        );
    }

    // Function to load Itinerary Fragment
    private void loadItineraryFragment(final ItineraryLoadCallback callback) {
        ItineraryFragment itineraryFragment = new ItineraryFragment(); // Create the fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.itinerary_fragment_container, itineraryFragment);
        transaction.commit(); // Commit the transaction

        // Assuming you have a method to check when the fragment is ready
        itineraryFragment.setLoadCompleteListener(new ItineraryFragment.LoadCompleteListener() {
            @Override
            public void onLoadComplete() {
                if (callback != null) {
                    callback.onItineraryLoaded();
                }
            }
        });
    }

    public interface ItineraryLoadCallback {
        void onItineraryLoaded();
    }


    // Function to remove Itinerary Fragment
    private void removeItineraryFragment() {
        Fragment itineraryFragment = getSupportFragmentManager().findFragmentById(R.id.itinerary_fragment_container);
        if (itineraryFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(itineraryFragment).commit();
        }
    }
    private void fetchTripDetails(String tripId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("readyTrips").document(tripId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ReadyTrips trip = documentSnapshot.toObject(ReadyTrips.class);
                        displayTripDetails(trip); // Display details in the UI
                    }
                });
    }

    // Display trip details on the UI
    private void displayTripDetails(ReadyTrips trip) {
        planName.setText(trip.getName());
        cityCountry.setText(trip.getCity() + ", " + trip.getCountry());
        planRating.setText(String.valueOf(trip.getRating()));
        overviewSection.setText(trip.getOverview());
        companion.setText(trip.getCompanionType());
        tripType.setText(trip.getTripType());

        // Define the date format (adjust based on your date format)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Define start and end dates
        LocalDate startDate = LocalDate.parse(trip.getStartDate(),formatter); // Year, Month, Day
        LocalDate endDate = LocalDate.parse(trip.getEndDate(), formatter); // Year, Month, Day


        numberOfDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        // Calculate the number of days between the start and end date
        noOfDays.setText(String.format("%d \nDays", numberOfDays));
    }

    // Fetch all trip days and their details from Firestore
//    private void fetchTripDays(String tripId) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("readyTrips").document(tripId).collection("itinerary")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            TripDay tripDay = document.toObject(TripDay.class);
//                            int dayNumber = tripDay.getDayNumber();
//                            // Cache each day's itinerary
//                            dayItineraryCache.put(dayNumber, tripDay);
//                        }
//                    } else {
//                        Log.w("Firestore", "Error getting itinerary documents.", task.getException());
//                    }
//                });
//    }

    // Set up day tabs dynamically
//    private void setupDayTabs() {
//        if (numberOfDays > 0 && dayTabLayout.getTabCount() == 0) {
//            for (int i = 1; i <= numberOfDays; i++) {
//                TabLayout.Tab daysTab = dayTabLayout.newTab();
//                daysTab.setText("Day " + i);
//                dayTabLayout.addTab(daysTab);
//            }
//
//            dayTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                @Override
//                public void onTabSelected(TabLayout.Tab tab) {
//                    int dayNumber = tab.getPosition() + 1;
//                    fetchDayItinerary(dayNumber); // Fetch or load cached itinerary for the selected day
//                }
//
//                @Override
//                public void onTabUnselected(TabLayout.Tab tab) {}
//
//                @Override
//                public void onTabReselected(TabLayout.Tab tab) {}
//            });
//        }
//    }

    // Fetch or load cached itinerary for a specific day
//    private void fetchDayItinerary(int dayNumber) {
////        if (dayItineraryCache.containsKey(dayNumber)) {
////            // Use cached data
////            updateItinerarySection(dayItineraryCache.get(dayNumber).getPlaces());
////        } else {
//            // Fetch from Firestore if not in cache
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.collection("readyTrips").document(tripId).collection("itinerary").document(dayNumber+"")
//                    .get()
//                    .addOnSuccessListener(documentSnapshot -> {
//                        if (documentSnapshot.exists()) {
//                            TripDay tripDay = documentSnapshot.toObject(TripDay.class);
//                            dayItineraryCache.put(dayNumber, tripDay);
//                            updateItinerarySection(tripDay.getPlaces());
//                            Log.i("",tripDay.getPlaces()+"");
//                        }else {
//                            Log.w("Firestore", "No itinerary found for day " + dayNumber);
//                        }
//                    })
//                    .addOnFailureListener(e -> Log.w("Firestore", "Error fetching itinerary for day " + dayNumber, e));
////        }
//    }


    // Method to update the itinerary section with a list of places
//    private void updateItinerarySection(List<ItineraryPlace> places) {
//        // Check if the adapter is null, then initialize it with the places list
////        if (placesAdapter == null) {
//            placesAdapter = new PlacesAdapter(this, places);
//            itineraryRecyclerView.setAdapter(placesAdapter);
////        } else {
////            // Update the adapter's data and refresh the RecyclerView
////            placesAdapter.notifyDataSetChanged();
////        }
//    }

    private void smoothScrollToView(NonScrollableScrollView scrollView, View view) {
        int y = view.getTop(); // Get the top position of the view
        ObjectAnimator animator = ObjectAnimator.ofInt(scrollView, "scrollY", scrollView.getScrollY(), y);
        animator.setDuration(500); // Adjust the duration for smoother scrolling
        animator.start();    }


    // Show dining recommendation section
    private void setupDiningRecommendation() {
        diningRecommendationBtn.setOnClickListener(v -> {
            // Toggle visibility of dining recommendations
            findViewById(R.id.dining_recomendation_grid_layout).setVisibility(
                    findViewById(R.id.dining_recomendation_grid_layout).getVisibility() == View.VISIBLE
                            ? View.GONE
                            : View.VISIBLE
            );
        });
    }

}
