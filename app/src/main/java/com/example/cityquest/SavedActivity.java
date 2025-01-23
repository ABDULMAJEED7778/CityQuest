package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.cityquest.adapter.MySavedTripsAdapter;
import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SavedActivity extends Fragment implements SignInDialogFragment.SignInDialogListener,MySavedTripsAdapter.EmptyStateListener{

    private RecyclerView recyclerView;
    private MySavedTripsAdapter adapter;
    private LottieAnimationView loadingAnim;
    private List<ReadyTrips> trips;
    private LinearLayout noTripsLayout;
    private SwipeRefreshLayout swipeRefreshLayout;





    AlertDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_saved, container, false);




        // Check if the user is signed in
        if (FirebaseUtils.getCurrentUser() == null) {
            showSignInDialog();
        } else {
            initializeRecyclerView(view);
            fetchTripsFromFirestore();
        }




        return view;

    }

    private void initializeRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.my_saved_trips_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.secondary_color, R.color.background_color);


        loadingAnim = view.findViewById(R.id.loading_anim_savedTrips_page);
        noTripsLayout = view.findViewById(R.id.no_savedTrips_yet);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        trips = new ArrayList<>();
        adapter = new MySavedTripsAdapter(getContext(), trips,this);
        recyclerView.setAdapter(adapter);

        // Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Simulate data refresh
            fetchTripsFromFirestore();
        });
    }

    private void showSignInDialog() {
        // Create and show the SignInDialogFragment
        SignInDialogFragment signInDialogFragment = new SignInDialogFragment(this);
        signInDialogFragment.show(getChildFragmentManager(), "SignInDialog");
    }


    private void fetchTripsFromFirestore() {
        trips.clear();
        noTripsLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        if (!swipeRefreshLayout.isRefreshing()) {
            loadingAnim.setVisibility(View.VISIBLE);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseUtils.getCurrentUser().getUid();

        db.collection("users").document(userId)
                .collection("mytrips")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ReadyTrips trip = document.toObject(ReadyTrips.class);
                            trips.add(trip);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                    loadingAnim.setVisibility(View.GONE);
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    toggleEmptyState();


                });
    }
    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onSignInSuccess() {
        // User successfully signed in
        Toast.makeText(getContext(), "Sign-in successful!", Toast.LENGTH_SHORT).show();
        initializeRecyclerView(getView());
        fetchTripsFromFirestore();
    }

    @Override
    public void onSignInFailure() {

        // User failed to sign in
        Toast.makeText(getContext(), "Sign-in failed. Please try again.", Toast.LENGTH_SHORT).show();

    }
    private void toggleEmptyState() {

        if (adapter.isListEmpty()) {
            noTripsLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noTripsLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEmptyStateChanged(boolean isEmpty) {
        toggleEmptyState();
    }
}
