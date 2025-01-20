package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.model.Trips;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class DateRangeFragment extends Fragment {

    private Button nextBtn, backBtn, resetBtn;
    private EditText dateRangeET;
    private DateRangeCalendarView calendar;
    private ReadyTrips trip;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_range, container, false);
        initializeViews(view);
        setupCalendar();
        setListeners();
        return view;
    }

    private void initializeViews(View view) {
        dateRangeET = view.findViewById(R.id.date_range_et);
        calendar = view.findViewById(R.id.calendar_date_picker);
        nextBtn = view.findViewById(R.id.nextBtn_dr);
        resetBtn = view.findViewById(R.id.reset_dateRange_btn);
        backBtn = view.findViewById(R.id.backBtn_dr);

        // Get the trip object passed as an argument
        if (getArguments() != null && getArguments().containsKey("trip")) {
            trip = getArguments().getParcelable("trip");
        }

        if (trip == null) {
            trip = new ReadyTrips();
        }

        calendar.setFonts(Objects.requireNonNull(ResourcesCompat.getFont(requireContext(), R.font.poppins_medium)));
    }

    private void setupCalendar() {
        Calendar today = Calendar.getInstance();
        String todayFormattedDate = DATE_FORMAT.format(today.getTime());

        Calendar startDateSelectable = Calendar.getInstance();
        Calendar endDateSelectable = Calendar.getInstance();
        endDateSelectable.add(Calendar.YEAR, 2);

        calendar.setSelectableDateRange(startDateSelectable, endDateSelectable);
        calendar.setCurrentMonth(today);
        calendar.setSelectedDateRange(today, today);

        dateRangeET.setText(todayFormattedDate + " -> ");
        trip.setStartDate(DATE_FORMAT.format(startDateSelectable.getTime()));
        trip.setEndDate(DATE_FORMAT.format(endDateSelectable.getTime()));
    }

    private void setListeners() {
        resetBtn.setOnClickListener(v -> {
            calendar.resetAllSelectedViews();
            trip.setStartDate("");
            trip.setEndDate("");
            dateRangeET.setText("");

            Calendar today = Calendar.getInstance();
            String todayFormattedDate = DATE_FORMAT.format(today.getTime());
            Calendar endDate = (Calendar) today.clone();
            endDate.add(Calendar.DAY_OF_MONTH, 5);
            String endFormattedDate = DATE_FORMAT.format(endDate.getTime());

            dateRangeET.setHint(todayFormattedDate + " -> " + endFormattedDate);
            resetBtn.setVisibility(View.INVISIBLE);
        });

        nextBtn.setOnClickListener(v -> {
            if (dateRangeET.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please select a date range", Toast.LENGTH_SHORT).show();
                return;
            }

//            Intent intent = new Intent(requireContext(), CompanionsActivity.class);
//            intent.putExtra("trip", trip);
//            startActivity(intent);
//            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            CompanionsFragment companionsFragment = new CompanionsFragment();
            Bundle args = new Bundle();
            args.putParcelable("trip", trip);
            companionsFragment.setArguments(args);
            ((MainActivity) requireActivity()).switchToNextFragment(companionsFragment);

        });

        backBtn.setOnClickListener(v -> requireActivity().onBackPressed());

        calendar.setCalendarListener(new CalendarListener() {
            @Override
            public void onFirstDateSelected(@NonNull Calendar startDate) {
                String formattedDate = DATE_FORMAT.format(startDate.getTime());
                dateRangeET.setText(formattedDate + " -> ");
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                String startFormattedDate = DATE_FORMAT.format(startDate.getTime());
                String endFormattedDate = DATE_FORMAT.format(endDate.getTime());
                dateRangeET.setText(startFormattedDate + " -> " + endFormattedDate);
                resetBtn.setVisibility(View.VISIBLE);
            }
        });
    }
}
