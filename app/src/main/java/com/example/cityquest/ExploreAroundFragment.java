package com.example.cityquest;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityquest.adapter.CityAdapter;
import com.example.cityquest.adapter.CityPagerAdapter;
import com.example.cityquest.adapter.TrendingCityAdapter;
import com.example.cityquest.adapter.WeekendTripAdapter;
import com.example.cityquest.model.City;
import com.example.cityquest.model.LocationViewModel;
import com.example.cityquest.utils.FirebaseUtils;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ExploreAroundFragment extends Fragment {

    private CityPagerAdapter cityPagerAdapter;
    Button searchButton;

    private RecyclerView trendingCitiesRecyclerView, weekendTripRecyclerView, citiesRecyclerView;
    private TrendingCityAdapter cityAdapter;
    private WeekendTripAdapter weekendTripAdapter;

    private TextView yourLocationCityTV;
    LocationViewModel locationViewModel;

    private List<City> cityList;
    Timer timer;



    private static final String ARG_CITY_NAME = "city_name";

    public static ExploreAroundFragment newInstance(String cityName) {
        ExploreAroundFragment fragment = new ExploreAroundFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CITY_NAME, cityName); // Put the city name in the arguments
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_explore, container, false);


        searchButton = view.findViewById(R.id.search_bar_btn);
        yourLocationCityTV = view.findViewById(R.id.location_city_name);

        final Animation shrinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shrink_animation);
        final Animation releaseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.release_animation);

        searchButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Start shrinking animation when the button is pressed
                    searchButton.startAnimation(shrinkAnimation);
                    return true; // Indicate that the touch event is handled

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Start releasing animation when the button is released
                    searchButton.startAnimation(releaseAnimation);

                    // Check if the touch is within the button bounds before navigating
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        // Navigate to the new activity
                        Intent intent = new Intent(getContext(), ChangeLocationCityActivity.class);
                        startActivity(intent);
                    }
                    return true;

                default:
                    return false; // For other events, do nothing
            }
        });

        // Retrieve the city name from arguments
        if (getArguments() != null) {
            String cityName = getArguments().getString(ARG_CITY_NAME);
            if(cityName.equals("city not selected")){

                setCityNameOfUserLocation();
            }else {
                yourLocationCityTV.setText(cityName);
            }
        }


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

        // Set up the timer for automatic scrolling
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Ensure the fragment is attached to the activity before accessing context
                if (isAdded()) {
                    // Your existing logic for scrolling goes here
                    if (layoutManager.findLastCompletelyVisibleItemPosition() < (cityAdapter.getItemCount() - 1)) {
                        layoutManager.smoothScrollToPosition(citiesRecyclerView, new RecyclerView.State(), layoutManager.findLastCompletelyVisibleItemPosition() + 1);
                    } else {
                        layoutManager.smoothScrollToPosition(citiesRecyclerView, new RecyclerView.State(), 0);
                    }
                } else {
                    // Cancel the timer if the fragment is not attached
                    cancel();
                }
            }
        }, 0, 3000);
        // Fetch cities data from Firestore
        fetchCitiesFromFirestore();






//






        return view;
    }

    private void setCityNameOfUserLocation() {

         locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);

        locationViewModel.getLocation().observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                String cityName = getCityName(location.getLatitude(), location.getLongitude());
                yourLocationCityTV.setText(cityName);
            }
        });



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
    // Helper method to check if the touch is inside the view bounds
    private boolean isTouchInsideView(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        return (event.getRawX() >= x && event.getRawX() <= (x + view.getWidth()) &&
                event.getRawY() >= y && event.getRawY() <= (y + view.getHeight()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel(); // Cancel the timer when the view is destroyed
            timer = null; // Avoid memory leaks
        }
    }
    private String getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        String cityName = null;

        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                cityName = addresses.get(0).getLocality(); // Get the city name

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName != null ? cityName : "Delhi";
    }




}


