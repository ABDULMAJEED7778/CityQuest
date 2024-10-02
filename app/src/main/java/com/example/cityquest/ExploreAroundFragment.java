package com.example.cityquest;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.cityquest.adapter.CityAdapter;
import com.example.cityquest.adapter.CityPagerAdapter;
import com.example.cityquest.adapter.TrendingCityAdapter;
import com.example.cityquest.adapter.WeekendTripAdapter;
import com.example.cityquest.model.City;
import com.example.cityquest.utils.FirebaseUtils;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExploreAroundFragment extends Fragment {

    private CityPagerAdapter cityPagerAdapter;
    LinearLayout exploreLayout; // Replace with your actual ID

    private RecyclerView trendingCitiesRecyclerView, weekendTripRecyclerView, citiesRecyclerView;
    private TrendingCityAdapter cityAdapter;
    private WeekendTripAdapter weekendTripAdapter;

    private List<City> cityList;
    Timer timer;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_explore, container, false);


        exploreLayout = view.findViewById(R.id.search_bar_layout); // Replace with your actual ID
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shrink_animation);
        final Animation releaseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.release_animation);




        // Initialize the city list
        cityList = new ArrayList<>();

        // Set up trendingCities RecyclerView
        trendingCitiesRecyclerView = view.findViewById(R.id.recycler_view_city); // Your RecyclerView ID
        trendingCitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cityAdapter = new TrendingCityAdapter(getContext(),cityList);

        //this is used to achieve horizontal paging as in viewPager
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        trendingCitiesRecyclerView.setAdapter(cityAdapter);

        citiesRecyclerView = view.findViewById(R.id.recyclerView_CitiesPhotos_Explore); // Your RecyclerView ID
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        citiesRecyclerView.setLayoutManager(layoutManager);
        cityPagerAdapter = new CityPagerAdapter(getContext(),cityList);
        snapHelper.attachToRecyclerView(citiesRecyclerView);
        citiesRecyclerView.setAdapter(cityPagerAdapter);

        // Set up trendingCities RecyclerView
        weekendTripRecyclerView = view.findViewById(R.id.recycler_view_weekendTrip); // Your RecyclerView ID
        weekendTripRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        weekendTripAdapter = new WeekendTripAdapter(getContext(),cityList);
        weekendTripRecyclerView.setAdapter(weekendTripAdapter);

        // automatic scrolling of cities
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(() -> {
                    int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();

                    if (lastVisibleItemPosition < cityAdapter.getItemCount() - 1) {
                        // Scroll to the next item
                        layoutManager.smoothScrollToPosition(citiesRecyclerView, new RecyclerView.State(), lastVisibleItemPosition + 1);
                    } else {
                        // If the last item is reached, scroll back to the first item
                        layoutManager.smoothScrollToPosition(citiesRecyclerView, new RecyclerView.State(), 0);
                    }
                });
            }
        }, 0, 4000);

        // Fetch cities data from Firestore
        fetchCitiesFromFirestore();






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

    private void fetchCitiesFromFirestore() {
        FirebaseUtils.getCitiesCollection()
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            City city = document.toObject(City.class);  // Convert document to City object
                            cityList.add(city);
                        }

                        // Notify RecyclerView adapter about data changes
                        cityAdapter.notifyDataSetChanged();
                        weekendTripAdapter.notifyDataSetChanged();
                        cityPagerAdapter.notifyDataSetChanged();

//                        // Now initialize the ViewPager adapter after data is fetched
//                        cityPagerAdapter = new CityPagerAdapter(requireActivity(), cityList);
//                        viewPager.setAdapter(cityPagerAdapter);

                    } else {
                        Log.d("CityActivity", "Error getting documents: ", task.getException());
                    }
                });
    }


}


