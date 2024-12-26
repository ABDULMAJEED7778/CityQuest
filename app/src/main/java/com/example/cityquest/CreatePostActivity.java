package com.example.cityquest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.adapter.NearByCityAdapter;
import com.example.cityquest.adapter.PlaceAutocompleteAdapter;
import com.example.cityquest.adapter.UploadedPostPhotosAdapter;
import com.example.cityquest.utils.FirebaseUtils;
import com.example.cityquest.utils.PlayerManager;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private UploadedPostPhotosAdapter adapter;
    private AutoCompleteTextView searchET;
    private RecyclerView citiesRecyclerView;
    private PlaceAutocompleteAdapter suggestionAdapter;
    private PlacesClient placesClient;
    private LinearLayout seclectedCityLL;
    private TextView selectedCityTV;
    private EditText postTitleEd, postDescriptionEd;
    private Button sharePostBtn;
    private String placeId;

    private String username = "";

    Uri selectedVideo;
    private PlayerView playerView;


    ArrayList<Uri> selectedPhotos;
    private boolean isvideo = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchET = findViewById(R.id.search_input_createPost);
        toolbar = findViewById(R.id.toolbar_createPost);
        postTitleEd = findViewById(R.id.post_title_ed);
        postDescriptionEd = findViewById(R.id.post_description_ed);
        sharePostBtn = findViewById(R.id.share_post_btn);
        recyclerView = findViewById(R.id.photos_recyclerView_createPost);
        citiesRecyclerView = findViewById(R.id.cities_recyclerview_createPost);
        playerView = findViewById(R.id.videoView_createPost);
        citiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        seclectedCityLL = findViewById(R.id.selected_city_linearLayout);
        selectedCityTV = findViewById(R.id.selected_city_tv);

        username = getIntent().getStringExtra("username");


        //TODO make sure to intialze places client only once
        String apiKey = getString(R.string.google_maps_api_key);
        Places.initializeWithNewPlacesApiEnabled(this, apiKey);
        placesClient = Places.createClient(this);

        // Initialize Autocomplete Adapter
        suggestionAdapter = new PlaceAutocompleteAdapter(this, new ArrayList<>());
        citiesRecyclerView.setAdapter(suggestionAdapter);

        suggestionAdapter.setOnItemClickListener(position -> {
            AutocompletePrediction selectedPrediction = suggestionAdapter.getItemAt(position);
            if (selectedPrediction != null) {
                citiesRecyclerView.setVisibility(View.GONE);
                placeId = selectedPrediction.getPlaceId();
                String cityName = selectedPrediction.getPrimaryText(null).toString();
                searchET.setText("");
                selectedCityTV.setText(cityName);
                seclectedCityLL.setVisibility(View.VISIBLE);
                searchET.setEnabled(false);


            }
        });

        isvideo = getIntent().getBooleanExtra("isVideo", false);

        if(!isvideo){
            selectedPhotos= getIntent().getParcelableArrayListExtra("selectedPhotos");

            if (selectedPhotos != null && !selectedPhotos.isEmpty()) {
                adapter = new UploadedPostPhotosAdapter(this,selectedPhotos);
                recyclerView.setAdapter(adapter);
                playerView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show();
            }
        }else{
            selectedVideo =  getIntent().getParcelableExtra("selectedVideo");

            if (selectedVideo != null) {
                // Attach Player only; don't play yet
                ExoPlayer player = PlayerManager.getInstance(this);
                playerView.setPlayer(player);

                // Set media item but don't start playback here
                MediaItem mediaItem = MediaItem.fromUri(selectedVideo);
                player.setMediaItem(mediaItem);
                player.prepare();
                playerView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "No video selected", Toast.LENGTH_SHORT).show();
            }


        }


        sharePostBtn.setOnClickListener(v->{

            uploadPostToFireStore(isvideo);

        });


        seclectedCityLL.setOnClickListener(v->{
            seclectedCityLL.setVisibility(View.GONE);
            searchET.setEnabled(true);

            selectedCityTV.setText("");
            suggestionAdapter.updateData(new ArrayList<>());
            citiesRecyclerView.setVisibility(View.GONE);
        });
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action required here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    // Fetch predictions after the user has typed at least 3 characters
                    fetchPlacePredictions(s.toString());
                } else {
                    // Clear the predictions if input is too short

                    suggestionAdapter.updateData(new ArrayList<>());
                    citiesRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action required here
            }
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow_icon); // Replace with your arrow drawable
        }

        postDescriptionEd.addTextChangedListener(new TextWatcher() {
            private String previousText; // Store the previous text

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString(); // Save the current text
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used
            }

            @Override
            public void afterTextChanged(Editable s) {
                int lineCount = postDescriptionEd.getLineCount();
                if (lineCount > 4) {
                    // Revert to the previous text
                    postDescriptionEd.removeTextChangedListener(this); // Temporarily remove listener to avoid recursion
                    postDescriptionEd.setText(previousText);

                    // Ensure the cursor is within bounds
                    if (previousText.length() > 0) {
                        postDescriptionEd.setSelection(previousText.length());
                    } else {
                        postDescriptionEd.setSelection(0); // Set to the start if text is empty
                    }

                    postDescriptionEd.addTextChangedListener(this); // Re-add listener
                }
            }
        });


        postDescriptionEd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If Enter is pressed, calculate the current line count
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    int lineCount = ((EditText) v).getLineCount();
                    if (lineCount >= 4) {
                        return true; // Block the Enter key if lines exceed 5
                    }
                }
                return false;
            }
        });





    }

    private void uploadPostToFireStore(boolean isvideo) {

        // Get the input from the EditTexts and the selected city
        String title = postTitleEd.getText().toString().trim();
        String description = postDescriptionEd.getText().toString().trim();
        String city = selectedCityTV.getText().toString().trim();

        // Flag to check if there are any validation errors
        boolean isValid = true;

        // Validate Title Field
        if (title.isEmpty()) {

            isValid = false; // Set validation to false if title is empty
        } else {
            postTitleEd.setError(null); // Clear any previous error
        }

        // Validate City Field
        if (city.isEmpty()) {

            isValid = false; // Set validation to false if city is empty
        }

        // Check if everything is valid
        if (isValid) {
            // Proceed with sharing the post if all required fields are filled
            String userId = FirebaseUtils.getCurrentUser().getUid();
            String postId = FirebaseUtils.getPostsCollection().document().getId();

            Map<String, Object> postMap = new HashMap<>();
            postMap.put("userName", username);
            postMap.put("userId", userId);
            postMap.put("postId", postId);
            postMap.put("datePosted", FieldValue.serverTimestamp()); // Use server timestamp for creation date
            postMap.put("title", title);
            postMap.put("description", description);

            if(isvideo){
                postMap.put("videoUrl", selectedVideo);
                postMap.put("photoUrls", null);
            }else {
                postMap.put("videoUrl", null);
                postMap.put("photoUrls", selectedPhotos);
            }

            postMap.put("placeId",placeId);
            postMap.put("views", 0);
            postMap.put("likes", 0);
            postMap.put("comments", 0);
            postMap.put("userProfilePicture", "");


            FirebaseUtils.getPostsCollection().document(postId)
                    .set(postMap)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Post added to global posts collection successfully");

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("postIds", FieldValue.arrayUnion(postId));
                        updates.put("postCount", FieldValue.increment(1));
                        // Add the post ID to the user's document in the "users" collection
                        FirebaseUtils.getUserDocument(userId)
                                .update(updates) // Add post ID to the user's postIds array
                                .addOnSuccessListener(aVoid2 -> Log.d("Firestore", "Post ID added to user's document"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error adding post ID to user's document", e));

                        // Upload media to Firebase Storage
                        uploadMediaToStorage(postId,isvideo);

                        Toast.makeText(this, "Post shared successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error adding post to global posts collection", e));


            // Add your logic for sharing the post, e.g., saving it to the database or posting to a server.
        } else {
            // Display message or handle any other logic if validation fails
            Toast.makeText(this, "Please fill in the required fields", Toast.LENGTH_SHORT).show();
            searchET.setError("City is required");
            postTitleEd.setError("Title is required");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //finish(); // Close the activity when the back arrow is clicked
        Toast.makeText(this, "discared??", Toast.LENGTH_SHORT).show();
        return true;
    }
    private void fetchPlacePredictions(String query) {
        // Build the autocomplete request
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setTypesFilter(Arrays.asList("locality"))
                .setQuery(query)
                .build();

        // Make the request to fetch predictions
        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {
                    List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                    // Update adapter with the new list of predictions
                    suggestionAdapter.updateData(predictions);
                    citiesRecyclerView.setVisibility(View.VISIBLE);
                    Log.e("PlaceError", "pridictions fetched = "+predictions.toString());
                })
                .addOnFailureListener(exception -> {
                    Log.e("PlaceError", "Error fetching place predictions: " + exception.getMessage());
                });



    }

    private void uploadMediaToStorage(String postId,boolean isVideo) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference postFolderRef = storage.getReference().child("posts/" + postId);

        List<String> uploadedPhotoUrls = new ArrayList<>();



        if(!isVideo){
            Log.e("selectedPhotos",selectedPhotos.toString());
        for (Uri mediaUri : selectedPhotos) {
            String fileName = System.currentTimeMillis() + "_" + mediaUri.getLastPathSegment();
            StorageReference fileRef = postFolderRef.child( "photos/" + fileName);

            fileRef.putFile(mediaUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                            .addOnSuccessListener(downloadUrl -> {

                                uploadedPhotoUrls.add(downloadUrl.toString());

                                // Update Firestore with uploaded photo URLs
                                if (uploadedPhotoUrls.size() == selectedPhotos.size()) {
                                    FirebaseUtils.getPostsCollection().document(postId)
                                            .update("photoUrls", uploadedPhotoUrls)
                                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Photo URLs updated successfully"))
                                            .addOnFailureListener(e -> Log.e("Firestore", "Error updating photo URLs", e));
                                }
                            }))
                    .addOnFailureListener(e -> Log.e("FirebaseStorage", "Error uploading media", e));
        }
        }else {


            String fileName = System.currentTimeMillis() + "_" + selectedVideo.getLastPathSegment();
            StorageReference fileRef = postFolderRef.child(fileName);

            fileRef.putFile(selectedVideo)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                                    .addOnSuccessListener(downloadUrl -> {
                                        FirebaseUtils.getPostsCollection().document(postId)
                                                .update("videoUrl", downloadUrl.toString());
                                            }))
                            .addOnFailureListener(e -> Log.e("FirebaseStorage", "Error uploading media", e));



        }

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void releasePlayer() {
        PlayerManager.releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }
}