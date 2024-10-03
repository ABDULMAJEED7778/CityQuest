package com.example.cityquest;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_trips);

        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.activity_trips, container, false);

        // Set up padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_trip);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        trips = new ArrayList<>();
        adapter = new TripAdapter(getContext(),trips);
        recyclerView.setAdapter(adapter);

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
}