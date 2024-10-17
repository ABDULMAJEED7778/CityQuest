package com.example.cityquest;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.adapter.DaysDetailsAdapter;
import com.example.cityquest.adapter.EditDaysDetailsAdapter;
import com.example.cityquest.model.TripDay;

import java.util.List;

public class EditTripActivity extends AppCompatActivity {

    private RecyclerView editDaysRecyclerView;
    private EditDaysDetailsAdapter editDaysDetailsAdapter;
    private List<TripDay> tripDays;
    private int clickedDayPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_trip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout_edit_trip_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            Log.i(systemBars.top + " " ,"dsfsdfas");
            return insets;
        });

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color_light));


        // Retrieve the passed data
        clickedDayPosition = getIntent().getIntExtra("clicked_day_position", -1);
        tripDays = getIntent().getParcelableArrayListExtra("trip_days");

        // Initialize RecyclerView
        editDaysRecyclerView = findViewById(R.id.edit_days_recycler_view);
        editDaysRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the adapter and expand only the clicked day
        editDaysDetailsAdapter = new EditDaysDetailsAdapter(this, tripDays);
        editDaysRecyclerView.setAdapter(editDaysDetailsAdapter);

        // Expand the clicked day
        if (clickedDayPosition != -1) {
            editDaysDetailsAdapter.expandDay(clickedDayPosition);

        }
    }
}
