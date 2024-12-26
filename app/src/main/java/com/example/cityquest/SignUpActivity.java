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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cityquest.Database.AppDatabase;
import com.example.cityquest.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    EditText emailET, passwordET, userNameET;
    Button signUpBtn;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    AlertDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        emailET = findViewById(R.id.emailInput_signUp);
        passwordET = findViewById(R.id.passwordInput_signUp);
        userNameET = findViewById(R.id.name_input_signUp);
        signUpBtn = findViewById(R.id.signUpButton);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email,password,userName;
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                userName = userNameET.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUpActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(SignUpActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Show custom progress dialog
                progressDialog = new AlertDialog.Builder(SignUpActivity.this)
                        .setView(R.layout.loading_progress_dialog)
                        .setCancelable(false)
                        .create();
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {



                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    String userId = mAuth.getCurrentUser().getUid();
                                    User user = new User(userId,userName, email,0,0,0,"");
                                    db.collection("users").document(userId)
                                            .set(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        AppDatabase database = AppDatabase.getDatabase(SignUpActivity.this);
                                                        databaseWriteExecutor.execute(() -> {
                                                            database.userDao().insertUser(user);
                                                        });

                                                        Toast.makeText(SignUpActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, "Failed to save user info", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    String errorMessage = task.getException().getMessage();

                                    Log.e("signUpError", "Errorrr: " + errorMessage);
                                    Toast.makeText(SignUpActivity.this, "Errorrrr: " + errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });





    }
}