package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.model.Trips;
import com.google.android.flexbox.FlexboxLayout;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.List;

public class InterestsFragment extends Fragment {

    private Button nextBtn, backBtn;
    private FlexboxLayout chipGroup; // Use FlexboxLayout to manage dynamic buttons
    private ReadyTrips trip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interests, container, false);

        trip = requireArguments().getParcelable("trip");
        if (trip == null) {
            trip = new ReadyTrips();
        }

        nextBtn = view.findViewById(R.id.nextBtn_comp);
        nextBtn.setOnClickListener(v -> {
            List<String> selectedActivities = new ArrayList<>();
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                AppCompatButton button = (AppCompatButton) chipGroup.getChildAt(i);
                if (button.isSelected()) {
                    selectedActivities.add(button.getText().toString());
                }
            }
            trip.setActivities(selectedActivities); // Store selected activities
            Log.e("interests", trip.getActivities().toString());

//            Intent intent = new Intent(getActivity(), SummaryActivity.class);
//            intent.putExtra("trip", trip); // Pass the trip object
//            startActivity(intent);
//            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            SummaryFragment summaryFragment = new SummaryFragment();
            Bundle args = new Bundle();
            args.putParcelable("trip", trip);
            summaryFragment.setArguments(args);
            ((MainActivity) requireActivity()).switchToNextFragment(summaryFragment);
        });

        backBtn = view.findViewById(R.id.backBtn_comp);
        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        chipGroup = view.findViewById(R.id.chipGroupInterests); // FlexboxLayout in fragment_interests.xml
        String[] interests = {"Great food", "Art Galleries", "Hidden Gems", "Nightlife and Entertainment", "Outdoors", "Shopping Experience", "Royal heritage"};

        for (String interest : interests) {
            AppCompatButton button = createChipButton(interest);
            // Set margin
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8)); // Set the desired margins (left, top, right, bottom)
            button.setLayoutParams(params);

            chipGroup.addView(button);
        }

        return view;
    }

    // Method to create a styled AppCompatButton that acts like a chip
    private AppCompatButton createChipButton(String text) {
        AppCompatButton button = new AppCompatButton(requireContext());
        button.setText(text);
        button.setBackgroundResource(R.drawable.chip_background);  // Add a custom background
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_color));
        button.setPadding(dpToPx(12), dpToPx(0), dpToPx(12), dpToPx(0));  // Add padding to look like a chip
        button.setTextAppearance(requireContext(), R.style.CustomChipTextAppearance);

        // Set the initial state (not selected)
        button.setSelected(false);
        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0); // No drawable initially

        // Handle toggle selection
        button.setOnClickListener(v -> {
            button.setSelected(!button.isSelected());
            updateChipStyle(button);
        });

        return button;
    }

    // Method to update the style of the button based on its selected state
    private void updateChipStyle(AppCompatButton button) {
        if (button.isSelected()) {
            button.setBackgroundResource(R.drawable.chip_selected);  // Custom drawable for selected state
            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick_with_circle, 0, 0, 0); // Set tick mark drawable on the left
            button.setCompoundDrawablePadding(dpToPx(8));

        } else {
            button.setBackgroundResource(R.drawable.chip_background);  // Custom drawable for default state
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0); // Remove tick mark drawable

        }
    }

    // Convert dp to pixels
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
