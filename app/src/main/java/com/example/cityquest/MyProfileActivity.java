package com.example.cityquest;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cityquest.utils.FirebaseUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

public class MyProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView getStartedTV;
    FloatingActionButton uploadPostFAB;
    TextView usernameTextView, postCountTV, followingsCountTV, followersCountTV;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar_my_profile);
        getStartedTV = findViewById(R.id.get_started_to_share_my_profile);
        uploadPostFAB = findViewById(R.id.upload_post_fab_my_profile);
        usernameTextView = findViewById(R.id.profile_name_my_profile);
        postCountTV = findViewById(R.id.posts_count_my_profile);
        followingsCountTV = findViewById(R.id.following_count_my_profile);
        followersCountTV = findViewById(R.id.followers_count_my_profile);

        getUserDataFromFireStore();
        uploadPostFAB.setOnClickListener(v -> {

            UploadPostBottomSheetFragment bottomSheetFragment = UploadPostBottomSheetFragment.newInstance(username);

            bottomSheetFragment.show(getSupportFragmentManager(), "UploadPostBottomSheetFragment");
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow_icon); // Replace with your arrow drawable
        }
        getStartedTV.setOnClickListener(v -> {

        });


    }

    private void getUserDataFromFireStore() {

        String userId = FirebaseUtils.getCurrentUser().getUid();
        Log.d("userId", userId);
        // Retrieve user data
        FirebaseUtils.getUserData(userId, task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Extract data
                     username = document.getString("userName");


                    // Populate TextViews
                    usernameTextView.setText(username != null ? username : "N/A");

                } else {
                    Toast.makeText(this, "User document does not exist.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Firestore", "Error fetching user data", task.getException());
                Toast.makeText(this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close the activity when the back arrow is clicked
        return true;
    }




}