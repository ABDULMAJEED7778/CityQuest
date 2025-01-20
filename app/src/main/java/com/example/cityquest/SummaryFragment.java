package com.example.cityquest;

import static com.example.cityquest.Database.AppDatabase.databaseWriteExecutor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cityquest.Database.AppDatabase;
import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.model.Trips;
import com.example.cityquest.utils.FirebaseUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.UUID;

public class SummaryFragment extends Fragment {

    private Button nextBtn, backBtn;
    private TextView cityNameTV, CompanionTV, dateRangeTV;
    private FlexboxLayout flexboxLayout;
    private ReadyTrips trip;
    private SwitchCompat tripTypeSwitch;
    AppDatabase database = AppDatabase.getDatabase(getContext());




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        // Initialize views
        cityNameTV = view.findViewById(R.id.cityName_summary);
        CompanionTV = view.findViewById(R.id.companion_summary);
        dateRangeTV = view.findViewById(R.id.date_range_summary);
        flexboxLayout = view.findViewById(R.id.interest_flexbox_sum);
        nextBtn = view.findViewById(R.id.nextBtn_sum);
        backBtn = view.findViewById(R.id.backBtn_sum);
        tripTypeSwitch = view.findViewById(R.id.trip_type_switch);


        // Retrieve the trip object from arguments
        if (getArguments() != null) {
            trip = getArguments().getParcelable("trip");
        }
        if (trip == null) {
            trip = new ReadyTrips();
        }

        setTripSummaryData();

        nextBtn.setOnClickListener(v -> {

                //saveTripAndProceed


            boolean isSwitchChecked = tripTypeSwitch.isChecked();  // Check if the switch is on

            if (isSwitchChecked) {
                // If switch is on (Custom), navigate to ReadyTrips
                if (trip != null) {
                    trip.setTripId(UUID.randomUUID().toString());
                    trip.setName("Trip To "+trip.getCity());
                    trip.setSynced(false);



                    Log.e("TripId","before db and fire "+trip.getTripId());

                    databaseWriteExecutor.execute(() -> {
                        database.readyTripsDao().insertTrip(trip);
                    });
                    saveTripToFirebase(trip);
                }

                Intent intent = new Intent(getActivity(), ReadyPlanDetailsActivity.class);
                intent.putExtra("trip", trip);  // Pass the trip object
                startActivity(intent);

            } else {
                // If switch is off (Ready Trips), navigate to ReadyPlanDetailsActivity
                TripsFragment tripsFragment = new TripsFragment();
                Bundle args = new Bundle();
                args.putParcelable("trip", trip);
                tripsFragment.setArguments(args);
                ((MainActivity) requireActivity()).switchToNextFragment(tripsFragment);
            }

                }
        );

        backBtn.setOnClickListener(v -> navigateBack());

        populateInterests();

        return view;
    }

    private void setTripSummaryData() {
        String dateRange = trip.getStartDate() + " - " + trip.getEndDate();
        cityNameTV.setText(trip.getCity());
        CompanionTV.setText(trip.getCompanionType());
        dateRangeTV.setText(dateRange);
    }

    private void populateInterests() {
        for (String interest : trip.getActivities()) {
            TextView textView = new TextView(getContext());
            textView.setText(interest);
            textView.setBackgroundResource(R.drawable.chip_selected); // Set background drawable
            textView.setPadding(32, 16, 32, 16); // Set padding

            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16, 16, 16, 16); // Set margins

            textView.setTextAppearance(R.style.CustomChipTextAppearance);
            textView.setLayoutParams(layoutParams); // Set layout parameters

            flexboxLayout.addView(textView); // Add TextView to FlexboxLayout
        }
    }

    public void saveTripToFirebase(ReadyTrips trip) {


        // Save to Firestore
        FirebaseUtils.getTripsCollection(FirebaseUtils.getCurrentUser().getUid())
                .add(trip) // Add the trip to Firestore
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.e("Firebase", "Trip is added to Firestore", task.getException());
                            trip.setSynced(true);
                            databaseWriteExecutor.execute(() -> {
                                database.readyTripsDao().updateTrip(trip); // Update synced status
                            });
                            Log.e("TripId","after db and fire "+trip.getTripId());


                        } else {
                            Log.e("Firebase", "Error saving trip", task.getException());
                        }
                    }
                });
    }



    private void navigateBack() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    public static SummaryFragment newInstance(ReadyTrips trip) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putParcelable("trip", trip);
        fragment.setArguments(args);
        return fragment;
    }
}
