package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.cityquest.adapter.CityPagerAdapter;
import com.example.cityquest.model.City;

import java.util.ArrayList;
import java.util.List;

public class ExploreAroundFragment extends Fragment {
    private ViewPager2 viewPager;
    private CityPagerAdapter cityPagerAdapter;
    LinearLayout exploreLayout; // Replace with your actual ID

    // Load animations

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_explore, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        exploreLayout = view.findViewById(R.id.search_bar_layout); // Replace with your actual ID
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shrink_animation);
        final Animation releaseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.release_animation);



        // Sample data for cities
        List<City> cities = new ArrayList<>();
        cities.add(new City("Bengaluru", "https://images.pexels.com/photos/2529056/pexels-photo-2529056.jpeg", "Bengaluru, the Silicon Valley of India, is known for its beautiful parks, vibrant culture, and tech industry."));
        cities.add(new City("Mumbai", "https://images.pexels.com/photos/2529056/pexels-photo-2529056.jpeg", "Mumbai, the financial capital of India, is famous for its bustling streets, Bollywood film industry, and diverse food scene."));
        cities.add(new City("Delhi", "https://images.pexels.com/photos/2529056/pexels-photo-2529056.jpeg", "Delhi, the capital city of India, is known for its rich history, stunning architecture, and vibrant markets."));

        // Add more cities as needed

        cityPagerAdapter = new CityPagerAdapter(requireActivity(), cities);
        viewPager.setAdapter(cityPagerAdapter);

        exploreLayout.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Start the shrink animation when clicked
                    exploreLayout.startAnimation(shrinkAnimation);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Start the release animation when released
                    exploreLayout.startAnimation(releaseAnimation);

                    // Add code to navigate to the new activity
                    exploreLayout.postDelayed(() -> {
                        Intent intent = new Intent(getContext(), searchInExploreActivity.class); // Replace YourNewActivity with the target activity
                        startActivity(intent);
                    }, releaseAnimation.getDuration()); // Delay to wait for the animation to finish
                    break;
            }
            return true;
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState); // Pass both parameters

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}


