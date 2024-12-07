package com.example.cityquest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

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

public class SignInDialogFragment extends DialogFragment {

    private static final String TAG = "GoogleOneTap";
    private static final int REQ_ONE_TAP = 2;
    private SignInDialogListener listener;

    private EditText emailET, passwordET;
    private Button signInBtn;
    private LinearLayout googleSignInBtn;
    private FirebaseAuth mAuth;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private ActivityResultLauncher<IntentSenderRequest> signInLauncher;
    private ProgressBar signInProgressBar;


    public interface SignInDialogListener {
        void onSignInSuccess();
        void onSignInFailure();
    }

    public SignInDialogFragment(SignInDialogListener listener) {
        this.listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_dialog, container, false);  // Use your dialog layout here
        getActivity().getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.dialog_background));

        view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.dialog_background));

        mAuth = FirebaseAuth.getInstance();

        emailET = view.findViewById(R.id.emailInput_SignIn);
        passwordET = view.findViewById(R.id.passwordInput_SignIn);
        signInBtn = view.findViewById(R.id.signInButton_dialog);
        googleSignInBtn = view.findViewById(R.id.buttonGoogleSignIn);
        signInProgressBar = view.findViewById(R.id.progress_bar_signin_button);

        oneTapClient = Identity.getSignInClient(requireContext());

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id)) // Your OAuth 2.0 client ID
                        .setFilterByAuthorizedAccounts(false) // Show all accounts
                        .build())
                .build();

        googleSignInBtn.setOnClickListener(v -> beginSignIn());

        signInBtn.setOnClickListener(v -> signInWithEmail());

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialog);


        signInLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
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
        });
    }


    private void signInWithEmail() {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Please enter your email.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter your password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading spinner
        signInBtn.setEnabled(false);
        signInProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                            listener.onSignInSuccess();  // Notify listener of success
                            dismiss();  // Close the dialog
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Log.e(TAG, "Login failed: " + errorMessage);
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            listener.onSignInFailure();  // Notify listener of failure
                        }
                        signInProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void beginSignIn() {
        if (getActivity() == null) return;
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(getActivity(), result -> {
                    try {
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                        signInLauncher.launch(intentSenderRequest);
                    } catch (Exception e) {
                        Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(getActivity(), e -> {
                    Log.d(TAG, "One Tap sign-in failed: " + e.getLocalizedMessage());
                    Toast.makeText(getContext(), "One Tap sign-in failed", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        listener.onSignInSuccess();  // Notify listener of success
                        dismiss();  // Close the dialog
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        listener.onSignInFailure();  // Notify listener of failure
                    }
                });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}