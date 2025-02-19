package com.example.cityquest;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.example.cityquest.model.Trips;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class DateRangeActivity extends AppCompatActivity {


    Button nextBtn,backBtn,resetBtn;
    EditText dateRangET;
    DateRangeCalendarView calendar;
    Trips trip;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_date_range);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.secondary_color_dark));

        dateRangET = findViewById(R.id.date_range_et);
        calendar = findViewById(R.id.calendar_date_picker);



        calendar.setFonts(Objects.requireNonNull(ResourcesCompat.getFont(this, R.font.poppins_medium)));
        trip = getIntent().getParcelableExtra("trip");
        if (trip == null) {
            trip = new Trips();
        }

        nextBtn = findViewById(R.id.nextBtn_dr);
        resetBtn = findViewById(R.id.reset_dateRange_btn);
        Calendar today = Calendar.getInstance();
        String todayFormattedDate = DATE_FORMAT.format(today.getTime());
        nextBtn.setOnClickListener(v -> {

            if(dateRangET.getText().toString().isEmpty()){
                Toast.makeText(this, "Please select a date range", Toast.LENGTH_SHORT).show();
                return;
            }


            Intent intent = new Intent(DateRangeActivity.this,CompanionsActivity.class);
            intent.putExtra("trip", trip);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        backBtn = findViewById(R.id.backBtn_dr);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DateRangeActivity.this,DestinationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.resetAllSelectedViews();
                trip.setStartDate("");
                trip.setEndDate("");
                dateRangET.setText("");
                Calendar endDate = (Calendar) today.clone();
                endDate.add(Calendar.DAY_OF_MONTH, 5);
                String endFormattedDate = DATE_FORMAT.format(endDate.getTime());

                // Update the EditText hint with the new date range
                dateRangET.setHint(todayFormattedDate + " -> " + endFormattedDate);
                resetBtn.setVisibility(View.INVISIBLE);



            }
        });

        calendar.setCalendarListener(new CalendarListener() {


            @Override
            public void onFirstDateSelected(@NonNull Calendar startDate) {
                String formattedDate = DATE_FORMAT.format(startDate.getTime());
                dateRangET.setText(formattedDate+" -> ");

            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                String startFormattedDate = DATE_FORMAT.format(startDate.getTime());
                String endFormattedDate = DATE_FORMAT.format(endDate.getTime());
                dateRangET.setText(startFormattedDate+" -> "+endFormattedDate);
                resetBtn.setVisibility(View.VISIBLE);
            }
        });
        // Set selectable date range
        Calendar startDateSelectable = Calendar.getInstance();
        Calendar endDateSelectable = Calendar.getInstance();
        endDateSelectable.add(Calendar.YEAR, 2); // Example: Set end date to 2 years from now
        calendar.setSelectableDateRange(startDateSelectable, endDateSelectable);
        trip.setStartDate(DATE_FORMAT.format(startDateSelectable.getTime()));
        trip.setEndDate(DATE_FORMAT.format(endDateSelectable.getTime()));

        // Set current date as default

        dateRangET.setText(todayFormattedDate+" -> ");
        calendar.setCurrentMonth(today);
        calendar.setSelectedDateRange(today, today);
    }
}

//https://github.com/ArchitShah248/CalendarDateRangePicker  8/2 -> 8/16 no past dates allowed select todays date by default