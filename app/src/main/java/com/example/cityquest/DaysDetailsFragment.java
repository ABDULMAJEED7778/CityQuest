//package com.example.cityquest;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.MenuInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.cityquest.adapter.PlacesAdapter;
//import com.example.cityquest.model.ItineraryPlace;
//import com.example.cityquest.model.TripDay;
//import com.google.android.material.tabs.TabLayout;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//public class DaysDetailsFragment extends Fragment {
//    private RecyclerView placesRecyclerView;
//    private PlacesAdapter placesAdapter;
//    private String tripId;// Add tripId if you want to fetch data based on trip ID
//    private ImageView commuteTypeBtn;
//    private TabLayout dayTabLayout;
//
//
//    // Cache to store itinerary data for each day
//    private HashMap<Integer, TripDay> dayItineraryCache = new HashMap<>();
//    private int numberOfDays;
//
//
//    public DaysDetailsFragment() {
//        super(R.layout.day_itinerary_card_item); // Reference to your fragment layout
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        tripId = getActivity().getIntent().getStringExtra("tripId"); // Retrieve trip ID from activity
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.day_itinerary_card_item, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        commuteTypeBtn = view.findViewById(R.id.commute_type_btn);
//        placesRecyclerView = view.findViewById(R.id.places_recycler_view);
//        dayTabLayout = view.findViewById(R.id.dayTabLayout);
//
//        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        dfetchTripDays(tripId);
//
//        // Set up the commute type button click listener
//        commuteTypeBtn.setOnClickListener(v -> showCommutePopup(v));
//    }
//
//    // Fetch trip days and their details from Firestore
//    private void fetchTripDays(String tripId) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("readyTrips").document(tripId).collection("itinerary")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        List<TripDay> tripDays = new ArrayList<>();
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            TripDay tripDay = document.toObject(TripDay.class);
//                            int dayNumber = tripDay.getDayNumber();
//                            // Cache each day's itinerary
//                            dayItineraryCache.put(dayNumber, tripDay);
//                            tripDays.add(tripDay);
//                        }
//                        // Set the number of days based on the fetched data
//                        numberOfDays = tripDays.size();
//                        setupDayTabs(tripDays);
//                    }
//                });
//    }
//
//    private void setupDayTabs(List<TripDay> tripDays) {
//        if (numberOfDays > 0) {
//            for (TripDay tripDay : tripDays) {
//                TabLayout.Tab daysTab = dayTabLayout.newTab();
//                daysTab.setText("Day " + tripDay.getDayNumber());
//                dayTabLayout.addTab(daysTab);
//            }
//
//            dayTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                @Override
//                public void onTabSelected(TabLayout.Tab tab) {
//                    int dayNumber = tab.getPosition() + 1;
//                    fetchDayItinerary(dayNumber); // Fetch itinerary for the selected day
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
//
//    private void fetchDayItinerary(int dayNumber) {
//        if (dayItineraryCache.containsKey(dayNumber)) {
//            // Use cached data
//            updateItinerarySection(dayItineraryCache.get(dayNumber).getPlaces());
//        } else {
//            // Fetch from Firestore if not in cache
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.collection("readyTrips").document(tripId).collection("itinerary")
//                    .document(String.valueOf(dayNumber))
//                    .get()
//                    .addOnSuccessListener(documentSnapshot -> {
//                        if (documentSnapshot.exists()) {
//                            TripDay tripDay = documentSnapshot.toObject(TripDay.class);
//                            dayItineraryCache.put(dayNumber, tripDay);
//                            updateItinerarySection(tripDay.getPlaces());
//                        }
//                    });
//        }
//    }
//
//    // Method to update the itinerary section with a list of places
//    private void updateItinerarySection(List<ItineraryPlace> places) {
//        placesAdapter = new PlacesAdapter(getActivity(), places);
//        placesRecyclerView.setAdapter(placesAdapter);
//    }
//
//    // Method to show the popup menu
//    private void showCommutePopup (View view) {
//        // Create a PopupMenu
//        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
//        MenuInflater inflater = popupMenu.getMenuInflater();
//        inflater.inflate(R.menu.commute_time_popup_menu, popupMenu.getMenu());
//
//
//        // Set up a click listener for menu item clicks
//        popupMenu.setOnMenuItemClickListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.walking:
//                    commuteTypeBtn.setImageResource(R.drawable.walk_icon);
//                    return true;
//                case R.id.car:
//                    commuteTypeBtn.setImageResource(R.drawable.car_icon);
//                    return true;
//                case R.id.metro:
//                    commuteTypeBtn.setImageResource(R.drawable.metro_icon);
//                    return true;
//                case R.id.bus:
//                    commuteTypeBtn.setImageResource(R.drawable.bus_icon);
//                    return true;
//                case R.id.bike:
//                    commuteTypeBtn.setImageResource(R.drawable.bike_icon);
//                    return true;
//                default:
//                    return false;
//            }
//        });
//
//        // Show the popup menu
//        popupMenu.show();
//    }
//}
