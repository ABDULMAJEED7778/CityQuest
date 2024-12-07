package com.example.cityquest.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.AddPlaceBottomSheet;
import com.example.cityquest.ExploreFragment;
import com.example.cityquest.R;
import com.example.cityquest.model.TripDay;
import com.example.cityquest.utils.ItemMoveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditDaysDetailsAdapter extends RecyclerView.Adapter<EditDaysDetailsAdapter.EditDaysViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private Context context;
    private List<TripDay> days;
    private List<Boolean> expandedStates; // Track expanded/collapsed states for each day
    private ItemTouchHelper daysItemTouchHelper;
    private ItemTouchHelper placesItemTouchHelper;
    private FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;




    public EditDaysDetailsAdapter (Context context, List<TripDay> days,  FragmentManager fragmentManager,  LayoutInflater layoutInflater) {
        this.context = context;
        this.days = days;
        this.fragmentManager = fragmentManager;
        this.layoutInflater = layoutInflater;

        // Initialize expanded state to false for all days
        this.expandedStates = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            expandedStates.add(false); // By default, all items are collapsed
        }
    }
    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.daysItemTouchHelper = itemTouchHelper;
    }

        @NonNull
    @Override
    public EditDaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_page_day_card_item, parent, false);
        return new EditDaysViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditDaysViewHolder holder, int position) {
        TripDay day = days.get(position);
        holder.dayNumber.setText("Day " + (position + 1));

        // Set up the PlacesAdapter for the nested RecyclerView
        EditPlacesAdapter editPlacesAdapter = new EditPlacesAdapter(context, day.getPlaces());
        holder.editPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.editPlacesRecyclerView.setAdapter(editPlacesAdapter);

        ItemTouchHelper.Callback callback = new ItemMoveCallback(editPlacesAdapter);
        placesItemTouchHelper = new ItemTouchHelper(callback);
        placesItemTouchHelper.attachToRecyclerView(holder.editPlacesRecyclerView);
        editPlacesAdapter.setItemTouchHelper(placesItemTouchHelper);


        // Check if the current day is expanded or collapsed
        boolean isExpanded = expandedStates.get(position);
        holder.placesLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        // Change the arrow direction based on the expanded state
        holder.toggleButton.setImageResource(isExpanded ? R.drawable.down_arrow_icon_vector : R.drawable.right_arrow_icon_vector);

        // Handle clicks to expand/collapse the card
        holder.dayNameLinearLayout.setOnClickListener(v -> toggleDayExpansion(holder, position));
        holder.toggleButton.setOnClickListener(v -> toggleDayExpansion(holder, position));

        // Delete button listener
        holder.daysDeleteBtn.setOnClickListener(v -> showDeleteConfirmationDialog(position));

        holder.daysReorderBtn.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    daysItemTouchHelper.startDrag(holder); // Start drag on button touch
                    return true; // Indicate that the event is consumed
                case MotionEvent.ACTION_UP:
                    // Call performClick() when the button is released
                    return true; // Indicate that the event is consumed
                case MotionEvent.ACTION_CANCEL:
                    // Handle cancel event
                    return true;
            }
            return false;
        });

        holder.addPlaceBtn.setOnClickListener(v -> {
            AddPlaceBottomSheet bottomSheet = new AddPlaceBottomSheet();
            bottomSheet.show(fragmentManager, "AddPlaceBottomSheet");
        });

        List<String> notes = day.getNotes();
        if (notes == null) {
            notes = new ArrayList<>();
            day.setNotes(notes); // Ensure the TripDay object has a non-null notes list
        }


        // Clear and populate the notes layout
        holder.notesLayout.removeAllViews();
        for (String note : day.getNotes()) {
            TextView noteView = new TextView(context);
            noteView.setText(note);
            noteView.setPadding(8, 8, 8, 8);
            noteView.setTextSize(TypedValue.COMPLEX_UNIT_SP , 16);
            noteView.setTextAppearance(R.style.bottomNavTextAppearanceInactive);
            noteView.setTextColor(ContextCompat.getColor(context,R.color.primary_color));
            noteView.setBackground(ContextCompat.getDrawable(context,R.color.transparent));
            holder.notesLayout.addView(noteView);

        }

        holder.addNoteBtn.setOnClickListener(v -> showAddNoteDialog(day, holder.notesLayout));


    }

    private void showAddNoteDialog(TripDay day, LinearLayout notesLayout) {
        // Inflate the custom layout
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null);

        // Initialize the dialog elements
        EditText noteText = dialogView.findViewById(R.id.note_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        // Set dialog background
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_background));

        // Cancel button action
        dialogView.findViewById(R.id.cancel_button).setOnClickListener(v -> dialog.dismiss());

        // Add button action
        dialogView.findViewById(R.id.Add_button).setOnClickListener(v -> {
            String note = noteText.getText().toString().trim();
            if (note.length() > 100) {
                noteText.setError("Note cannot exceed 100 characters");
            } else if (!note.isEmpty()) {
                // Add note to the TripDay
                day.getNotes().add(note);

                // Add note dynamically to the UI
                TextView noteView = new TextView(context);
                noteView.setText(note);
                noteView.setPadding(8, 8, 8, 8);
                noteView.setBackgroundResource(R.drawable.popup_menu_background);
                notesLayout.addView(noteView);

                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();

        // Convert dp to pixels
        int widthInDp = 350; // Replace with your desired dp value
        int widthInPixels = (int) (widthInDp * context.getResources().getDisplayMetrics().density);

        // Set dialog width
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }



    // Toggle the expansion of a specific day card
    private void toggleDayExpansion(EditDaysViewHolder holder, int position) {
        boolean currentlyExpanded = expandedStates.get(position);

        // Toggle the expanded state
        expandedStates.set(position, !currentlyExpanded);

        // Animate the layout changes
        TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);

        // Notify the adapter to refresh the item
        notifyItemChanged(position);
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Day");
        builder.setMessage("Are you sure you want to delete this day?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Remove the day and notify the adapter
            days.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, days.size());
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }


    // Method to programmatically expand a specific day (called when a tab is selected)
    public void expandDay (int dayPosition) {
        // Collapse all other days
        for (int i = 0; i < expandedStates.size(); i++) {
            expandedStates.set(i, i == dayPosition); // Only expand the selected day
        }




        // Notify the adapter to update all items
        notifyDataSetChanged();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(days, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(days, i, i - 1);
            }
        }

        // Update day numbers after moving
        updateDayNumbers();

        notifyItemMoved(fromPosition, toPosition);
        notifyItemChanged(fromPosition); // Notify change at the original position
        notifyItemChanged(toPosition); // Notify change at the new position
    }

    @Override
    public void onRowSelected(RecyclerView.ViewHolder myViewHolder) {
        // Optionally, apply a visual effect when the row is selected for drag
    }

    @Override
    public void onRowClear(RecyclerView.ViewHolder myViewHolder) {
        // Optionally, clear any visual effects after drag is complete
    }
    // Method to update day numbers based on the current list position
    private void updateDayNumbers() {
        for (int i = 0; i < days.size(); i++) {
            TripDay day = days.get(i);
            day.setDayNumber(i + 1); // Update the day number (1-based index)
        }
    }


    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class EditDaysViewHolder extends RecyclerView.ViewHolder {

        TextView dayNumber;
        RecyclerView editPlacesRecyclerView;
        ImageButton toggleButton;
        LinearLayout dayNameLinearLayout;
        ImageButton daysDeleteBtn;
        ImageButton daysReorderBtn;
        LinearLayout placesLayout;
        Button addPlaceBtn;
        Button addNoteBtn;
        LinearLayout notesLayout;

        public EditDaysViewHolder(@NonNull View itemView) {
            super(itemView);
            dayNumber = itemView.findViewById(R.id.day_name);
            editPlacesRecyclerView = itemView.findViewById(R.id.edit_places_recycler_view);
            toggleButton = itemView.findViewById(R.id.toggle_button);
            dayNameLinearLayout = itemView.findViewById(R.id.day_name_linear_layout);
            daysDeleteBtn = itemView.findViewById(R.id.days_delete_button);
            daysReorderBtn = itemView.findViewById(R.id.days_reorder_button);
            placesLayout = itemView.findViewById(R.id.places_layout);
            addPlaceBtn = itemView.findViewById(R.id.add_place_button);
            addNoteBtn = itemView.findViewById(R.id.add_note_button);
            notesLayout = itemView.findViewById(R.id.notes_layout);
        }

    }

    public void updateDays(List<TripDay> newDays) {
        // Update the current list of days
        this.days = newDays;

        // Reset the expanded states
        expandedStates.clear();
        for (int i = 0; i < newDays.size(); i++) {
            expandedStates.add(false);
        }

        // Notify the adapter that the data has changed
        notifyDataSetChanged();
    }
}
