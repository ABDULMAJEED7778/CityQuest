package com.example.cityquest;

import static java.lang.Integer.parseInt;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;

import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.model.TripDay;
import com.example.cityquest.utils.NonScrollableScrollView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.time.format.DateTimeFormatter;

import android.widget.TextView;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.gridlayout.widget.GridLayout;

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
    private FrameLayout exploreFragmentContainer;
    private int numberOfDays;
    private String tripId;
    private String tripIdForCustomTrip;
    boolean directFromDestActivity = false;
    ArrayList<TripDay> itinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_plan_page);
        EdgeToEdge.enable(this);




        // Initialize UI elements
        detailsTabLayout = findViewById(R.id.tab_layout_details);
        overviewSection = findViewById(R.id.overview_section);
       // exploreSection = findViewById(R.id.explore_section);
        scrollView = findViewById(R.id.scrollview);
//        diningRecommendationBtn = findViewById(R.id.dining_recommendation_listing_btn);
//        diningRecommendationList = findViewById(R.id.dining_recomendation_grid_layout);
        planName = findViewById(R.id.plan_name);
        noOfDays = findViewById(R.id.no_of_days);
        cityCountry = findViewById(R.id.city_location);
        weatherCondition = findViewById(R.id.weather_condation);
        planRating = findViewById(R.id.plan_rating);
        tripType = findViewById(R.id.trip_type);
        companion = findViewById(R.id.companion_type);
        itineraryFragmentContainer = findViewById(R.id.itinerary_fragment_container);
        exploreFragmentContainer = findViewById(R.id.explore_fragment_container);

        scrollView.setScrollable(true);




        // Attempt to retrieve the tripId
         tripId = getIntent().getStringExtra("tripId");

        // Attempt to retrieve the trip object
        ReadyTrips trip = getIntent().getParcelableExtra("trip");

        itinerary = getIntent().getParcelableArrayListExtra("itinerary");


        if (trip != null) {
            // Use the trip object directly
            displayTripDetails(trip);

            tripIdForCustomTrip = trip.getTripId();

            if(itinerary!=null){
                directFromDestActivity = true;
                Toast.makeText(this, "size of itinerary"+itinerary.size(), Toast.LENGTH_SHORT).show();
            }
        } else if (tripId != null) {
            // Fetch trip details using the tripId
            fetchTripDetails(tripId);
        } else {
            // Handle the case where neither is provided
            Toast.makeText(this, "No trip information provided", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity gracefully
        }




        AtomicInteger currentTabPosition = new AtomicInteger(0);
        detailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            private boolean scrolled = false;
              @Override
              public void onTabSelected(@NonNull TabLayout.Tab tab) {
                  int selectedTabPosition = tab.getPosition();
                  scrollView.post(() -> {
                      switch (selectedTabPosition) {
                          case 0: // First Tab (Overview)
                              currentTabPosition.set(0);
                              overviewSection.setVisibility(View.VISIBLE);
                              itineraryFragmentContainer.setVisibility(View.GONE);
                              exploreFragmentContainer.setVisibility(View.GONE);
                              resetTabLayoutStyles();
                              removeItineraryFragment();
                              removeExploreFragment();
                              scrollView.setScrollable(true);
                              break;

                          case 1: // Second Tab (Itinerary)
                              overviewSection.setVisibility(View.GONE);
                              exploreFragmentContainer.setVisibility(View.INVISIBLE);
                              itineraryFragmentContainer.setVisibility(View.VISIBLE);

                              loadItineraryFragment(new ItineraryLoadCallback() {

                                  @Override
                                  public void onItineraryLoaded() {
                                      if (currentTabPosition.get() == 0) {
                                          smoothScrollToView(scrollView, detailsTabLayout);
                                          updateTabLayoutStyles();
                                          currentTabPosition.set(1);
                                      }
                                      scrollView.setScrollable(false);
                                  }
                              });
                              removeExploreFragment();

                              break;

                          case 2: // Third Tab (Explore)
                              overviewSection.setVisibility(View.GONE);
                              itineraryFragmentContainer.setVisibility(View.INVISIBLE);
                              exploreFragmentContainer.setVisibility(View.VISIBLE);

                              loadExploreFragment(new ExploreLoadCallback() {
                                  @Override
                                  public void onExploreLoaded() {
                                      if (currentTabPosition.get() == 0) {
                                          smoothScrollToView(scrollView, detailsTabLayout);
                                          updateTabLayoutStyles();
                                          currentTabPosition.set(2);
                                      }
                                      scrollView.setScrollable(false);
                                  }
                              });
                              removeItineraryFragment();
                              break;
                      }
                  });
              }


              @Override
              public void onTabUnselected(TabLayout.Tab tab) {
                  if (tab.getPosition() == 1|| tab.getPosition() == 2) {
                      scrollView.setScrollable(true);
                  }
              }

              @Override
              public void onTabReselected(TabLayout.Tab tab) {
                  if (tab.getPosition() == 1|| tab.getPosition() == 2) {
                      scrollView.setScrollable(false);
                  }
                  Toast.makeText(ReadyPlanDetailsActivity.this, "onTabReselected"+tab.getPosition(), Toast.LENGTH_SHORT).show();
              }
          }
        );
    }

    // Function to load Itinerary Fragment
    private void loadItineraryFragment(final ItineraryLoadCallback callback) {
        ItineraryFragment itineraryFragment;
        if(directFromDestActivity||tripIdForCustomTrip!=null){
             itineraryFragment = new ItineraryFragment().newInstance(itinerary, tripIdForCustomTrip); // Create the fragment

        }else {
             itineraryFragment = new ItineraryFragment(); // Create the fragment
        }
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
    private void updateTabLayoutStyles() {
        detailsTabLayout.setBackgroundResource(R.color.primary_color_light);
        detailsTabLayout.setSelectedTabIndicatorColor(
                ResourcesCompat.getColor(getResources(), R.color.primary_color, getTheme())
        );
        detailsTabLayout.setTabTextColors(
                ResourcesCompat.getColor(getResources(), R.color.background_color, getTheme()), // Unselected
                ResourcesCompat.getColor(getResources(), R.color.primary_color, getTheme()) // Selected
        );
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_color_light));
    }

    private void resetTabLayoutStyles() {
        detailsTabLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.background_color, getTheme()));
        detailsTabLayout.setSelectedTabIndicatorColor(ResourcesCompat.getColor(getResources(), R.color.primary_color, getTheme()));
        detailsTabLayout.setTabTextColors(
                ResourcesCompat.getColor(getResources(), R.color.gray, getTheme()), // Unselected
                ResourcesCompat.getColor(getResources(), R.color.primary_color, getTheme()) // Selected
        );
        getWindow().setStatusBarColor(getColor(R.color.transparent));
    }

    private void loadExploreFragment(final ExploreLoadCallback callback) {
        ExploreFragment exploreFragment = new ExploreFragment(); // Create the fragment

        // Set the load complete listener before committing the transaction
        exploreFragment.setLoadCompleteListener(new ExploreLoadCallback() {
            @Override
            public void onExploreLoaded() {
                if (callback != null) {
                    callback.onExploreLoaded(); // Notify the callback
                }
            }
        });

        // Begin the transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.explore_fragment_container, exploreFragment); // Ensure correct container ID
        transaction.commit(); // Commit the transaction
    }




    public interface ExploreLoadCallback {
        void onExploreLoaded();
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
    private void removeExploreFragment() {
        Fragment exploreFragment = getSupportFragmentManager().findFragmentById(R.id.explore_fragment_container); // Use the correct container ID
        if (exploreFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(exploreFragment).commit();
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



        if (view.getVisibility() == View.VISIBLE) {
            int y = view.getTop(); // Get the top position of the view
            ObjectAnimator animator = ObjectAnimator.ofInt(scrollView, "scrollY", scrollView.getScrollY(), y - 80);
            animator.setDuration(500); // Adjust the duration for smoother scrolling
            animator.start();
        }else{
        Toast.makeText(this, "view not visible", Toast.LENGTH_SHORT).show();
        }

        }

    private void fastScrollToView(NonScrollableScrollView scrollView, View view) {



        if (view.getVisibility() == View.VISIBLE) {
            int y = view.getTop(); // Get the top position of the view
            ObjectAnimator animator = ObjectAnimator.ofInt(scrollView, "scrollY", scrollView.getScrollY(), y - 80);
            animator.setDuration(0); // Adjust the duration for smoother scrolling
            animator.start();
        }else{
            Toast.makeText(this, "view not visible", Toast.LENGTH_SHORT).show();
        }

    }



    // Show dining recommendation section
//    private void setupDiningRecommendation() {
//        diningRecommendationBtn.setOnClickListener(v -> {
//            // Toggle visibility of dining recommendations
//            findViewById(R.id.dining_recomendation_grid_layout).setVisibility(
//                    findViewById(R.id.dining_recomendation_grid_layout).getVisibility() == View.VISIBLE
//                            ? View.GONE
//                            : View.VISIBLE
//            );
//        });
//    }

}
