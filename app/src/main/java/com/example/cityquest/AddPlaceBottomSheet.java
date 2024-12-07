package com.example.cityquest;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddPlaceBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_container, container, false);

        // Find the parent LinearLayout
        LinearLayout parentLayout = (LinearLayout) view;

        // Create a new TextView programmatically
        TextView textView = new TextView(getContext());
        textView.setText("Add a Place");
        textView.setTextSize(18);
        textView.setPadding(16, 16, 16, 8); // Adjust padding as needed
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                120
        ));
        textView.setTextAppearance(R.style.bottomSheetTextAppearance);
        textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary_color_light));
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.background_color));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER);
        textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.add_to_my_trip_button));

        // Add the TextView to the top of the layout
        parentLayout.addView(textView, 0); // Add at index 0 to place it at the top
        view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.search_buttom_sheet_background));




        // Dynamically add the existing Fragment to this Bottom Sheet
        ExploreFragment addPlaceFragment = new ExploreFragment(); // Existing Fragment
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_sheet_container, addPlaceFragment)
                .commit();

        return view;
    }
}
