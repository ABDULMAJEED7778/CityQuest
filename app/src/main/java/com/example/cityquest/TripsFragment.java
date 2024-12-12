package com.example.cityquest;


import static android.view.Gravity.START;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.cityquest.adapter.TripAdapter;
import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.utils.FirebaseUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TripsFragment extends Fragment implements TripAdapter.OnFavoriteTripActionListener{

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private List<ReadyTrips> trips; // Use ReadyTrips for your trips
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Button locationCityName;
    private LinearLayout filtersLayout;
    private MenuItem filterMenuItem;
    private LottieAnimationView loadingAnim;



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
        loadingAnim = view.findViewById(R.id.loading_anim_trips_page);


        trips = new ArrayList<>();
        adapter = new TripAdapter(getContext(),trips,this);
        recyclerView.setAdapter(adapter);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                float progress = (float) Math.abs(verticalOffset) / totalScrollRange;  // Progress of scroll (0 to 1)
                float scrollPercentage = (float) Math.abs(verticalOffset) / totalScrollRange;


                locationCityName.setAlpha(1 - progress);
                filtersLayout.setAlpha(1 - progress);
                locationCityName.setVisibility(View.GONE);
                // Check if the toolbar is fully collapsed
                if(scrollPercentage > 0.9) {
                    if (filterMenuItem != null) {
                        filterMenuItem.setVisible(true);
                        requireActivity().invalidateOptionsMenu();
                        Log.e("iugiug","jgkgkj");
                    }
                }
                if (scrollPercentage > 0.8) {
                    // Fully collapsed - show the element
                    filtersLayout.setVisibility(View.GONE);
                } else if (scrollPercentage < 0.2){
                    // Expanded or in transition - hide the element
                    // Expanded or in transition - hide the menu item
                    if (filterMenuItem != null) {
                        filterMenuItem.setVisible(false);
                        requireActivity().invalidateOptionsMenu();
                    }
                    locationCityName.setVisibility(View.VISIBLE);
                    filtersLayout.setVisibility(View.VISIBLE);

                }
            }
        });

        fetchTripsFromFirestore(); // Call method to fetch trips




        return view;
    }

    private void fetchTripsFromFirestore() {
        recyclerView.setVisibility(View.GONE);
        loadingAnim.setVisibility(View.VISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("readyTrips") // Collection name in Firestore
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        trips.clear(); // Clear the existing list to avoid duplicates
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Map Firestore document data to ReadyTrips object
                            ReadyTrips trip = document.toObject(ReadyTrips.class);
                            trips.add(trip); // Add the trip to the list
                        }
                        adapter.notifyDataSetChanged(); // Notify adapter about data changes

                        // Fetch saved trips after trips have been loaded
                        if(FirebaseUtils.getCurrentUser() != null)
                            fetchSavedTrips();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }

                    // Hide loading animation and show RecyclerView
                    loadingAnim.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                });
    }
    private void fetchSavedTrips() {
        String userId = FirebaseUtils.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).collection("mytrips")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Set<String> savedTripIds = new HashSet<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            savedTripIds.add(document.getId());
                        }
                        adapter.updateSavedTrips(savedTripIds);
                    } else {
                        Log.w("Firestore", "Error getting saved trips.", task.getException());
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
                WindowInsetsCompat insetsCompat = WindowInsetsCompat.toWindowInsetsCompat(insets);
                int statusBarHeight = insetsCompat.getInsets(WindowInsetsCompat.Type.statusBars()).top;

                appBarLayout.setPadding(
                        0,
                        statusBarHeight,
                        0,
                        0
                );

                // Adjust CollapsingToolbarLayout to handle insets without interfering with the title
                collapsingToolbarLayout.setPadding(0, 1, 0, 0);
                collapsingToolbarLayout.setMinimumHeight(statusBarHeight);
                                // Don't consume the insets
                return insets;
            }
        });
        // Request insets
        view.requestApplyInsets();


        MenuHost menuHost = requireActivity();

        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.tool_bar_trips_activity, menu); // Inflate the menu
                filterMenuItem = menu.findItem(R.id.filter); // Reference to the "edit" menu item
                filterMenuItem.setVisible(false); // Initially hide the menu item
                Log.i("oflsjdflihlsad",filterMenuItem+"erewrf");
            }

            @Override
            public void onPrepareMenu(@NonNull Menu menu) {
                // If you need to update the menu dynamically before it's shown, you can do it here
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                // Handle menu item clicks here if needed
                return false;
            }
        }, getViewLifecycleOwner());

    }


    @Override
    public boolean isUserSignedIn() {
        return FirebaseUtils.getCurrentUser() != null;
    }

    @Override
    public void showSignInDialog(OnSignInSuccessListener listener) {
        // Show the sign-in dialog using the fragment manager of the parent fragment
        SignInDialogFragment dialogFragment = new SignInDialogFragment(new SignInDialogFragment.SignInDialogListener() {
            @Override
            public void onSignInSuccess() {
                listener.onSignInSuccess();
            }

            @Override
            public void onSignInFailure() {

            }
        });
        // Show the dialog using the parent fragment manager
        dialogFragment.show(getParentFragmentManager(), "SignInDialog");
    }


    @Override
    public void saveTripToFirestore(ReadyTrips trip) {


        String userId = FirebaseUtils.getCurrentUser().getUid();
        DocumentReference tripRef = FirebaseUtils.getMyTripsDocument(userId, trip.getTripId());

        tripRef.set(trip)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Trip saved successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to save trip.", Toast.LENGTH_SHORT).show();
                    Log.e("MyActivity", "Error saving trip: ", e);
                });
    }

    @Override
    public void removeTripFromFirestore(ReadyTrips trip) {
        String userId = FirebaseUtils.getCurrentUser().getUid();
        DocumentReference tripRef = FirebaseUtils.getMyTripsDocument(userId, trip.getTripId());

        tripRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Trip removed successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to remove trip.", Toast.LENGTH_SHORT).show();
                    Log.e("TripsFragment", "Error removing trip: ", e);
                });
    }


}