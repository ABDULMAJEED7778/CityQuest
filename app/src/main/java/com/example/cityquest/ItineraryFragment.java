package com.example.cityquest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.adapter.DaysDetailsAdapter;
import com.example.cityquest.adapter.PlacesAdapter;
import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.TripDay;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItineraryFragment extends Fragment {
    private RecyclerView daysRecyclerView;
    private DaysDetailsAdapter daysDetailsAdapter;
    private String tripId;// Add tripId if you want to fetch data based on trip ID
//    private ImageView commuteTypeBtn;
    private TabLayout dayTabLayout;
    private LinearLayoutManager layoutManager;

    public interface LoadCompleteListener {
        void onLoadComplete();
    }
    private LoadCompleteListener loadCompleteListener;

    // Cache to store itinerary data for each day
    private HashMap<Integer, TripDay> dayItineraryCache = new HashMap<>();
    private int numberOfDays;


    public ItineraryFragment() {
        super(R.layout.fragment_itinerary); // Reference to your fragment layout
    }

    // Define the LoadCompleteListener interface inside your fragment class


    public void setLoadCompleteListener(LoadCompleteListener listener) {
        this.loadCompleteListener = listener;
    }

    // Call this method once your data is loaded
    private void onDataLoaded() {
        if (loadCompleteListener != null) {
            loadCompleteListener.onLoadComplete();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripId = getActivity().getIntent().getStringExtra("tripId"); // Retrieve trip ID from activity
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_itinerary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        daysRecyclerView = view.findViewById(R.id.days_recycler_view);
        dayTabLayout = view.findViewById(R.id.dayTabLayout);

        layoutManager = new LinearLayoutManager(getContext());
        daysRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchTripDays(tripId);

        // Add scroll listener to track the visible day and update TabLayout
//        daysRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                // Find the current visible day position
//                int visibleDayPosition = layoutManager.findFirstVisibleItemPosition();
//
//                // Update the TabLayout to reflect the current day being viewed
//                if (visibleDayPosition != TabLayout.Tab.INVALID_POSITION) {
//                    TabLayout.Tab tab = dayTabLayout.getTabAt(visibleDayPosition);
//                    if (tab != null && !tab.isSelected()) {
//                        tab.select();
//                    }
//                }
//            }
//        });

    }

    // Fetch trip days and their details from Firestore
    private void fetchTripDays(String tripId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("readyTrips").document(tripId).collection("itinerary")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<TripDay> tripDays = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TripDay tripDay = document.toObject(TripDay.class);
                            int dayNumber = tripDay.getDayNumber();
                            // Cache each day's itinerary
                            dayItineraryCache.put(dayNumber, tripDay);
                            tripDays.add(tripDay);
                        }
                        // Set the number of days based on the fetched data
                        numberOfDays = tripDays.size();

                        updateItinerarySection(tripDays); // Update the RecyclerView with the days and their places
                        onDataLoaded();
                        setupDayTabs(tripDays);
                    }
                });
    }

    private void setupDayTabs(List<TripDay> tripDays) {
        if (numberOfDays > 0) {
            for (TripDay tripDay : tripDays) {
                TabLayout.Tab daysTab = dayTabLayout.newTab();
                daysTab.setText("Day " + tripDay.getDayNumber());
                dayTabLayout.addTab(daysTab);
            }

            dayTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int dayNumber = tab.getPosition();
                    // Use post to ensure that the UI is updated before attempting to scroll
                    daysRecyclerView.post(() -> {
                        // Scroll to the selected day in the RecyclerView
                        daysRecyclerView.smoothScrollToPosition(dayNumber);
                    });
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

    private void fetchDayItinerary(int dayNumber) {
        if (dayItineraryCache.containsKey(dayNumber)) {
            // Use cached data
            updateItinerarySection(Arrays.asList(dayItineraryCache.get(dayNumber)));
        } else {
            // Fetch from Firestore if not in cache
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("readyTrips").document(tripId).collection("itinerary")
                    .document(String.valueOf(dayNumber))
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            TripDay tripDay = documentSnapshot.toObject(TripDay.class);
                            dayItineraryCache.put(dayNumber, tripDay);
                            updateItinerarySection(Arrays.asList(tripDay));
                        }
                    });
        }
    }

    private void updateItinerarySection(List<TripDay> days) {
        if (daysDetailsAdapter == null) {
            daysDetailsAdapter = new DaysDetailsAdapter(getActivity(), days);
            daysRecyclerView.setAdapter(daysDetailsAdapter);
        } else {
            daysDetailsAdapter.updateDays(days); // If necessary, update the adapter with new data
        }
    }

}
