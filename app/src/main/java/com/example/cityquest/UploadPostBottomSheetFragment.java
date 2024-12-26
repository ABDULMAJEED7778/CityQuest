package com.example.cityquest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.cityquest.model.Trips;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class UploadPostBottomSheetFragment extends BottomSheetDialogFragment {


    private static final int PICK_IMAGES_REQUEST = 1;
    private static final String ARG_USER_NAME = "user_name";

    private static final int MAX_IMAGES = 5;
    private ActivityResultLauncher<PickVisualMediaRequest> pickPhotosLauncher;
    private ActivityResultLauncher<PickVisualMediaRequest> pickVideoLauncher;

    private String username;
    private LinearLayout photoUpload,videoUpload;


    public static UploadPostBottomSheetFragment newInstance(String username) {
        UploadPostBottomSheetFragment fragment = new UploadPostBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, username);

        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_post_bottom_sheet, container, false);

        Bundle args = getArguments();
        if (args != null) {

           username = args.getString(ARG_USER_NAME);
           Log.d("username","in bundle :"+username);


        }
        return view;
    }

    @NonNull
    @Override
    public int getTheme() {
        return R.style.MyBottomSheetDialogTheme; // Use the defined theme here
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Close the bottom sheet when X icon is clicked
        ImageView closeIcon = view.findViewById(R.id.close_icon);
        closeIcon.setOnClickListener(v -> dismiss());

        // Handle click on Photo Upload item
        photoUpload = view.findViewById(R.id.upload_photos_linear_layout);
        photoUpload.setOnClickListener(v -> {
            pickPhotos();
        });
        videoUpload = view.findViewById(R.id.upload_video_linear_layout);
        videoUpload.setOnClickListener(v -> {
            //upload one video
            pickVideo();
        });

        // Initialize ActivityResultLauncher to pick multiple photos

        pickPhotosLauncher =
                registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5),uris->{


                    if (uris != null && !uris.isEmpty()) {
                        // Send the URIs to CreatePostActivity
                        Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                        intent.putExtra("username", username);
                        Log.d("username","putExtra :"+username);

                        intent.putParcelableArrayListExtra("selectedPhotos", new ArrayList<>(uris)); // Send the URIs
                        startActivity(intent);
                        dismiss();  // Close the bottom sheet after selection
                    } else {
                        Toast.makeText(getContext(), "No images selected", Toast.LENGTH_SHORT).show();
                    }
                });

        // Initialize ActivityResultLauncher for videos
        pickVideoLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("selectedVideo", uri); // Send the URI
                intent.putExtra("isVideo", true);
                startActivity(intent);
                dismiss(); // Close the bottom sheet after selection
            } else {
                Toast.makeText(getContext(), "No video selected", Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void pickPhotos() {
        pickPhotosLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }

    private void pickVideo() {
        pickVideoLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE).build());
    }




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // Prevent the bottom sheet from being expanded
        dialog.getBehavior().setDraggable(false);
        return dialog;
    }


}
