//package com.example.cityquest;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.cityquest.adapter.TripAdapter;
//import com.example.cityquest.model.ReadyTrips;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TripsActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private TripAdapter adapter;
//    private List<ReadyTrips> trips;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_trips);  // Set the layout
//
//        // Initialize RecyclerView
//        recyclerView = findViewById(R.id.recycler_view_trip);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//
//        // Initialize the list of trips and the adapter
//        trips = new ArrayList<>();
//        adapter = new TripAdapter(this, trips,this);
//        recyclerView.setAdapter(adapter);
//
//        // Fetch trips from Firestore
//        fetchTripsFromFirestore();
//    }
//
//    private void fetchTripsFromFirestore() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("readyTrips")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            ReadyTrips trip = document.toObject(ReadyTrips.class);
//                            trips.add(trip);
//                        }
//                        adapter.notifyDataSetChanged();  // Notify the adapter that data has changed
//                    } else {
//                        Log.w("Firestore", "Error getting documents.", task.getException());
//                    }
//                });
//    }
//}
