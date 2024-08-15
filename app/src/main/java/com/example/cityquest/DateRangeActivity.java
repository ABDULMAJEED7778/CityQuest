package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateRangeActivity extends AppCompatActivity {


    Button nextBtn,backBtn,resetBtn;
    EditText dateRangET;
    DateRangeCalendarView calendar;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_date_range);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        dateRangET = findViewById(R.id.date_range_et);
        calendar = findViewById(R.id.calendar);
        nextBtn = findViewById(R.id.nextBtn_dr);
        resetBtn = findViewById(R.id.reset_dateRange_btn);
        Calendar today = Calendar.getInstance();
        String todayFormattedDate = DATE_FORMAT.format(today.getTime());
        nextBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DateRangeActivity.this,CompanionsActivity.class);
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
                dateRangET.setText("");
                Calendar endDate = (Calendar) today.clone();
                endDate.add(Calendar.DAY_OF_MONTH, 5);
                String endFormattedDate = DATE_FORMAT.format(endDate.getTime());

                // Update the EditText hint with the new date range
                dateRangET.setHint(todayFormattedDate + " -> " + endFormattedDate);



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
            }
        });
        // Set selectable date range
        Calendar startDateSelectable = Calendar.getInstance();
        Calendar endDateSelectable = Calendar.getInstance();
        endDateSelectable.add(Calendar.YEAR, 2); // Example: Set end date to 2 years from now
        calendar.setSelectableDateRange(startDateSelectable, endDateSelectable);

        // Set current date as default

        dateRangET.setText(todayFormattedDate+" -> ");
        calendar.setCurrentMonth(today);
        calendar.setSelectedDateRange(today, today);
    }
}

//https://github.com/ArchitShah248/CalendarDateRangePicker  8/2 -> 8/16 no past dates allowed select todays date by default