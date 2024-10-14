package com.example.cityquest;


import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.adapter.TripAdapter;
import com.example.cityquest.model.ReadyTrips;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private List<ReadyTrips> trips; // Use ReadyTrips for your trips
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Button locationCityName;
    private LinearLayout filtersLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_trips);

        // Inflate the fragment layout
        LayoutInflater themedInflater = inflater.cloneInContext(new ContextThemeWrapper(getActivity(), R.style.TripsPage));
        View view = themedInflater.inflate(R.layout.activity_trips, container, false);


        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_trip);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        collapsingToolbarLayout = view.findViewById(R.id.CollapsingToolbarLayout);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        locationCityName = view.findViewById(R.id.location_city_name);
        filtersLayout = view.findViewById(R.id.filters_layout);

        trips = new ArrayList<>();
        adapter = new TripAdapter(getContext(),trips);
        recyclerView.setAdapter(adapter);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                float progress = (float) Math.abs(verticalOffset) / totalScrollRange;  // Progress of scroll (0 to 1)
                float scrollPercentage = (float) Math.abs(verticalOffset) / totalScrollRange;


                locationCityName.setAlpha(1 - progress);
                filtersLayout.setAlpha(1 - progress);
                // Check if the toolbar is fully collapsed
                if (scrollPercentage > 0.8) {
                    // Fully collapsed - show the element
                    locationCityName.setVisibility(View.GONE);
                    filtersLayout.setVisibility(View.GONE);
                } else if (scrollPercentage < 0.2){
                    // Expanded or in transition - hide the element
//                    locationCityName.setVisibility(View.VISIBLE);
//                    filtersLayout.setVisibility(View.VISIBLE);

                }
            }
        });

        fetchTripsFromFirestore(); // Call method to fetch trips

        return view;
    }

    private void fetchTripsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("readyTrips") // Collection name in Firestore
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Map Firestore document data to ReadyTrips object
                            ReadyTrips trip = document.toObject(ReadyTrips.class);
                            trips.add(trip); // Add the trip to the list
                        }
                        adapter.notifyDataSetChanged(); // Notify adapter about data changes
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        requireView().setOnApplyWindowInsetsListener((v, insets) -> {
//            int statusBarHeight = WindowInsetsCompat.toWindowInsetsCompat(insets).getInsets(WindowInsetsCompat.Type.statusBars()).top;
//            collapsingToolbarLayout.setPadding(0,statusBarHeight + 4 ,0,0);
//            return insets;
//        });

        view.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                // Apply insets for status bar
                int statusBarHeight = WindowInsetsCompat.toWindowInsetsCompat(insets).getInsets(WindowInsetsCompat.Type.statusBars()).top;

                appBarLayout.setPadding(
                        0,
                        statusBarHeight+4,
                        0,
                        0
                );
                                // Don't consume the insets
                return insets;
            }
        });
        // Request insets
        view.requestApplyInsets();



    }
}