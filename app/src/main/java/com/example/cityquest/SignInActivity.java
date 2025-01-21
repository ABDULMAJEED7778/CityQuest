package com.example.cityquest;

import static com.example.cityquest.Database.AppDatabase.databaseWriteExecutor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import com.example.cityquest.Database.AppDatabase;
import com.example.cityquest.Database.ReadyTripsDao;
import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.model.User;
import com.example.cityquest.utils.FirebaseUtils;
import com.example.cityquest.utils.FirestoreCityUploader;
import com.example.cityquest.utils.FirestoreTripUploader;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    //change from skipBtn to exploreBtn
    Button exploreBtn;
    Button upload;
    TextView signupTv;
    EditText emailET, passwordET;
    Button signInBtn;
    FirebaseAuth mAuth;

  //  String origin = getIntent().getStringExtra("origin");
   // String tripId = getIntent().getStringExtra("tripId");


    AlertDialog progressDialog;

    private static final int REQ_ONE_TAP = 2;  // Unique request code
    private static final String TAG = "GoogleOneTap";
    private boolean showOneTapUI = true; // Control One Tap UI visibility

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);



        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.emailInput_SignIn);
        passwordET = findViewById(R.id.passwordInput_SignIn);

        signInBtn = findViewById(R.id.signInButton);


        oneTapClient = Identity.getSignInClient(this);

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId("getString(R.string.default_web_client_id") // Your OAuth 2.0 client ID
                        .setFilterByAuthorizedAccounts(false) // Show all accounts
                        .build())
                .build();

        // Trigger the sign-in when the user clicks a button
        findViewById(R.id.buttonGoogleSignIn).setOnClickListener(v -> beginSignIn());


        exploreBtn = findViewById(R.id.explore_around_btn);
        upload = findViewById(R.id.upload_to_firestore_btn);

        signupTv = findViewById(R.id.signInTXT);

        upload.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                FirestoreTripUploader tripUploader = new FirestoreTripUploader();
//                tripUploader.generateAndStoreTrips();

                FirestoreCityUploader cityUploader = new FirestoreCityUploader();
                cityUploader.uploadCities();

                //TODO use this to save the trip locally to the database
//                // Assume you have a ReadyTrips object
//                ReadyTrips newTrip = new ReadyTrips("1", "to Paris", "photo_url", 4.5f,
//                        "Paris", "France", "Sunny", 25.0f, "Adventure", "Friends",
//                        "2024-10-10", "2024-10-15", "A wonderful trip to Paris");
//
//// Get the ReadyTripsDao from the AppDatabase
//                AppDatabase db = AppDatabase.getDatabase(SignInActivity.this);
//                ReadyTripsDao readyTripsDao = db.readyTripsDao();
//
//// Insert the trip into the database using ExecutorService
//                AppDatabase.databaseWriteExecutor.execute(() -> {
//                    readyTripsDao.insertTrip(newTrip);
//                });


            }
        });

        exploreBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        signupTv.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignInActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignInActivity.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show custom progress dialog
                progressDialog = new AlertDialog.Builder(SignInActivity.this)
                        .setView(R.layout.loading_progress_dialog)
                        .setCancelable(false)
                        .create();
                progressDialog.show();

                // Firebase Authentication for Login
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Hide the progress dialog
                                progressDialog.dismiss();


                                if (task.isSuccessful()) {


                                    FirebaseUser currentUser = FirebaseUtils.getCurrentUser();
                                    if (currentUser != null) {
                                        String userId = currentUser.getUid();

                                        // Check if user data exists in Firestore
                                        FirebaseUtils.getUsersCollection().document(userId)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful() && task.getResult() != null) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                // User exists, retrieve the user data
                                                                User user = document.toObject(User.class);

                                                                // Save the user data in Room
                                                                if (user != null) {
                                                                    AppDatabase database = AppDatabase.getDatabase(SignInActivity.this);
                                                                    databaseWriteExecutor.execute(() -> {
                                                                        database.userDao().insertUser(user);
                                                                    });
                                                                }

                                                                // Proceed to next activity
                                                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                Toast.makeText(SignInActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(SignInActivity.this, "Failed to retrieve user", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }




                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Log.e("signact", "Login failed: " + errorMessage);
                                    Toast.makeText(SignInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    private void beginSignIn() {
        Log.e("logingoogle","hiiiii");
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        startIntentSenderForResult(result.getPendingIntent().getIntentSender(), REQ_ONE_TAP, null, 0, 0, 0);
                    } catch (Exception e) {
                        Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(this, e -> {
                    Log.d(TAG, "One Tap sign-in failed: " + e.getLocalizedMessage());
                    Toast.makeText(this, "One Tap sign-in failed", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
                    Log.d(TAG, "Got ID token.");
                    authenticateWithFirebase(idToken);
                }
            } catch (ApiException e) {
                Log.e(TAG, "One Tap sign-in failed.", e);
            }
        }
    }

    private void authenticateWithFirebase(String idToken) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {

        }
    }

    private void saveTripToFirestore(String tripId) {//TODO use it in sign in pop up
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the ReadyTrips document
        DocumentReference readyTripRef = db.collection("ReadyTrips").document(tripId);

        // Reference to the user's mytrips collection
        DocumentReference userTripRef = db.collection("users").document(userId).collection("mytrips").document(tripId);

        // Fetch the trip data from ReadyTrips collection
        readyTripRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the trip data as a Map or custom object
                        Map<String, Object> tripData = documentSnapshot.getData();

                        // Save the trip data to the user's mytrips subcollection
                        userTripRef.set(tripData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Trip saved successfully!", Toast.LENGTH_SHORT).show();
                                    Log.d("TripAdapter", "Trip saved successfully to Firestore");
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to save trip.", Toast.LENGTH_SHORT).show();
                                    Log.e("TripAdapter", "Error saving trip: ", e);
                                });
                    } else {
                        Toast.makeText(this, "Trip not found!", Toast.LENGTH_SHORT).show();
                        Log.e("TripAdapter", "Trip with ID " + tripId + " does not exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch trip.", Toast.LENGTH_SHORT).show();
                    Log.e("TripAdapter", "Error fetching trip: ", e);
                });
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}