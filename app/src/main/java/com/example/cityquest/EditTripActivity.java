package com.example.cityquest;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.adapter.DaysDetailsAdapter;
import com.example.cityquest.adapter.EditDaysDetailsAdapter;
import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.TripDay;
import com.example.cityquest.utils.ItemMoveCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditTripActivity extends AppCompatActivity {

    private RecyclerView editDaysRecyclerView;
    private EditDaysDetailsAdapter editDaysDetailsAdapter;
    private List<TripDay> tripDays;
    private List<TripDay> originalTripDays;
    private int clickedDayPosition;
    private ItemTouchHelper daysItemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_trip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout_edit_trip_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color_light));

        // Set up the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar); // Make sure you have a Toolbar with this ID in your layout
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Retrieve the passed data
        clickedDayPosition = getIntent().getIntExtra("clicked_day_position", -1);
        tripDays = getIntent().getParcelableArrayListExtra("trip_days");

        originalTripDays = deepCopyTripDays(tripDays);

        // Initialize RecyclerView
        editDaysRecyclerView = findViewById(R.id.edit_days_recycler_view);
        editDaysRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Set up the adapter and expand only the clicked day
        editDaysDetailsAdapter = new EditDaysDetailsAdapter(this, tripDays,getSupportFragmentManager(), getLayoutInflater());
        editDaysRecyclerView.setAdapter(editDaysDetailsAdapter);

        ItemTouchHelper.Callback callback = new ItemMoveCallback(editDaysDetailsAdapter);
        daysItemTouchHelper = new ItemTouchHelper(callback);
        daysItemTouchHelper.attachToRecyclerView(editDaysRecyclerView);
        editDaysDetailsAdapter.setItemTouchHelper(daysItemTouchHelper);


        // Expand the clicked day
        if (clickedDayPosition != -1) {
            editDaysDetailsAdapter.expandDay(clickedDayPosition);

        }
    }

    // Inflate the menu with the Save button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_edit_trip_page, menu);
        return true;
    }

    // Handle the menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            if (hasChanges()) {
                promptForTripName();
            } else {
                // Show a message indicating no changes
                Toast.makeText(this, "No changes made to save", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Check if the tripDays list has been modified compared to the original state
    private boolean hasChanges() {
        if (tripDays.size() != originalTripDays.size()) {
            return true;
        }
        for (int i = 0; i < tripDays.size(); i++) {
            if (!tripDays.get(i).equals(originalTripDays.get(i))) {
                return true;
            }
        }
        return false;
    }

    // Deep copy of the tripDays list to store the original state
    private List<TripDay> deepCopyTripDays(List<TripDay> original) {
        List<TripDay> copy = new ArrayList<>();
        for (TripDay day : original) {
            copy.add(new TripDay(day)); // Assuming TripDay has a copy constructor
        }
        return copy;
    }

    // Prompt the user to enter a name for the customized trip
    private void promptForTripName() {
        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_save_trip_edit, null);

        // Find the EditText in the custom layout
        EditText editTripName = dialogView.findViewById(R.id.edit_trip_name);
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);


        // Set up the dialog buttons
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.dialog_background));
        dialogView.findViewById(R.id.cancel_button).setOnClickListener(v -> dialog.dismiss());

        dialogView.findViewById(R.id.save_button).setOnClickListener(v -> {
            String tripName = editTripName.getText().toString();
            if (!tripName.isEmpty()) {
                // Save the trip with the given name
                saveCustomizedTrip(tripName);
                dialog.dismiss();
            } else {
                // Show a message to enter a name
                Toast.makeText(this, "Please enter a trip name", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        dialog.show();

        // Convert dp to pixels
        int widthInDp = 350; // Replace with your desired dp value
        int widthInPixels = (int) (widthInDp * getResources().getDisplayMetrics().density);

    // Set dialog width in dp (converted to pixels)
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT); // Width in dp (converted to pixels)
        }
    }

    // Save the customized trip to Firestore
    private void saveCustomizedTrip(String tripName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        // Prepare the trip data
        Map<String, Object> tripData = new HashMap<>();
        tripData.put("trip_name", tripName);
        tripData.put("days", getDaysAsMapList());
        Log.i("days" , getDaysAsMapList() + "");

        // Save the trip under user_trips collection in Firestore
        db.collection("users")
                .document(userId)
                .collection("trips")
                .add(tripData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(EditTripActivity.this, "Trip saved successfully!", Toast.LENGTH_SHORT).show();

                    // Create an Intent to send back the updated trip
                    Intent resultIntent = new Intent();
                    resultIntent.putParcelableArrayListExtra("updated_trip_days", (ArrayList<TripDay>) tripDays);

                    // Set the result as OK and pass back the updated trip
                    setResult(RESULT_OK, resultIntent);

                    // Close the activity
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditTripActivity.this, "Failed to save trip", Toast.LENGTH_SHORT).show();
                });
    }

    // Convert the list of TripDay objects into a format that Firestore can store
    private List<Map<String, Object>> getDaysAsMapList() {
        List<Map<String, Object>> dayList = new ArrayList<>();
        for (TripDay day : tripDays) {
            Map<String, Object> dayMap = new HashMap<>();
            dayMap.put("day_number", day.getDayNumber());

            // Convert ItineraryPlace objects into maps
            List<Map<String, Object>> placesList = new ArrayList<>();
            for (ItineraryPlace place : day.getPlaces()) {
                Map<String, Object> placeMap = new HashMap<>();
                placeMap.put("place_name", place.getPlaceName());
                placeMap.put("place_id", place.getPlaceId());
                placeMap.put("photo_url", place.getPhotoUrl());
                placeMap.put("overview", place.getOverview());
                placesList.add(placeMap);
            }
            dayMap.put("places", placesList);

            dayList.add(dayMap);
        }
        return dayList;
    }

}
