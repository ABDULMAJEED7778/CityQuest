package com.example.cityquest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.tabs.TabLayout;

public class AddedToMyTripsFragment extends Fragment {

    private TextView savedTextView;
    private LottieAnimationView tickAnimation;
    public AddedToMyTripsFragment() {
        super(R.layout.fragment_add_to_trips); // Reference to your fragment layout
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_to_trips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedTextView = view.findViewById(R.id.saved_text_view);
        tickAnimation = view.findViewById(R.id.tickAnimation);

        Animation popUpAnim = AnimationUtils.loadAnimation(getContext(), R.anim.popup_text);

        // Play animation once
        tickAnimation.playAnimation();

        // Add an animation listener to stop at the last frame
        tickAnimation.addAnimatorUpdateListener(animation -> {
            if (tickAnimation.getProgress() == 1f) {
                tickAnimation.pauseAnimation();
            }
        });

        // Delay for 3 seconds (3000 milliseconds)
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                savedTextView.setVisibility(View.VISIBLE); // Make it visible after delay
                savedTextView.startAnimation(popUpAnim);

            }
        }, 1500); // Delay in milliseconds
    }

}
