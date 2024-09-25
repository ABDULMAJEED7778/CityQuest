package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    //change from skipBtn to exploreBtn
    Button exploreBtn;
    TextView signupTv;
    EditText emailET, passwordET;
    Button signInBtn;
    FirebaseAuth mAuth;

    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.emailInput_SignIn);
        passwordET = findViewById(R.id.passwordInput_SignIn);

        signInBtn = findViewById(R.id.signInButton);



        exploreBtn = findViewById(R.id.explore_around_btn);

        signupTv = findViewById(R.id.signUpTXT);

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
                                    Toast.makeText(SignInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignInActivity.this, DestinationActivity.class);
                                    startActivity(intent);
                                    finish();  // Finish the login activity
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(SignInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(SignInActivity.this, DestinationActivity.class);
            startActivity(intent);
            finish();  // Finish the login activity
        }
    }
}