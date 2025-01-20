package com.example.cityquest;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cityquest.adapter.ImageSliderAdapter;
import com.example.cityquest.adapter.NearByCityAdapter;
import com.example.cityquest.adapter.ThingsToDoAdapter;
import com.example.cityquest.adapter.TravelStoryAdapter;
import com.example.cityquest.adapter.WeekendTripAdapter;
import com.example.cityquest.model.City;
import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.LocationViewModel;
import com.example.cityquest.model.PlaceDetails;
import com.example.cityquest.model.TravelStory;

import com.example.cityquest.utils.FirebaseUtils;
import com.example.cityquest.utils.GoogleMapsAPIsUtils;
import com.example.cityquest.utils.LocationPermissionUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.FindPlaceFromTextRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExploreCityActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private ViewPager2 viewPager;
    private GoogleMapsAPIsUtils googlePlacesUtils;
    private TabLayout tabLayout;
    private ImageSliderAdapter imageSliderAdapter;
    private List<String> imageUrls= new ArrayList<>();

    private TabLayout mainTabLayout;
    private NestedScrollView nestedScrollView;
    private View thingsToDoSection,nearbySection,storiesSection;
    private TextView overviewSection;
    private List<PlaceDetails> nearByCitiesList = new ArrayList<>();

    double cityLat, cityLng;

    ThingsToDoAdapter thingsToDoAdapter ,natureAdapter, foodAdapter, cultureAdapter;
    List<ItineraryPlace> places;
    private TabLayout thingsToDoTabLayout;
    private RecyclerView TopAttractionsRecyclerView,natureRecyclerView, foodRecyclerView, cultureRecyclerView;


    private boolean thingsToDoDataFetched = false;
    private boolean natureDataFetched = false;
    private boolean foodDataFetched = false;
    private boolean cultureDataFetched = false;

    private NearByCityAdapter nearByCityAdapter;

    private RecyclerView nearbyCityRecyclerView;

    private TravelStoryAdapter travelStoryAdapter;
    private List<TravelStory> travelStories;
    private RecyclerView travelStoryRecyclerView;
    private Timer timer;

    String cityName,cityId,cityDescription,cityOverview;
    TextView cityNameTV, cityDescriptionTV;

    List<PlaceDetails> thingsToDoPlaces,naturePlaces, foodPlaces, culturePlaces;





    private PlacesClient placesClient;




   // String videoUrl = "https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_explore_city);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);




        googlePlacesUtils = new GoogleMapsAPIsUtils(this, BuildConfig.MAPS_API_KEY);



        placesClient = Places.createClient(this);


         cityName = getIntent().getStringExtra("CITY_NAME");
         cityId = getIntent().getStringExtra("CITY_ID");
         cityDescription = getIntent().getStringExtra("CITY_DESCRIPTION");
         cityOverview = getIntent().getStringExtra("CITY_OVERVIEW");

         thingsToDoPlaces = new ArrayList<>();
         naturePlaces = new ArrayList<>();
         foodPlaces = new ArrayList<>();
         culturePlaces = new ArrayList<>();






        topAppBar = findViewById(R.id.topAppBar);
        // Initialize the ViewPager2 and TabLayout
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        thingsToDoTabLayout = findViewById(R.id.tab_layout_things_to_do);

        overviewSection = findViewById(R.id.overview_section_tv);
        cityNameTV = findViewById(R.id.cityName);
        cityDescriptionTV = findViewById(R.id.city_popular_for_txt);
        cityNameTV.setText(cityName);
        cityDescriptionTV.setText(cityDescription);
        overviewSection.setText(cityOverview);

        mainTabLayout = findViewById(R.id.tab_layout_for_cityExplore);
        nestedScrollView = findViewById(R.id.nested_scrollView_explore_city);

        thingsToDoSection = findViewById(R.id.things_to_do_section);
        nearbySection = findViewById(R.id.section_nearby);
        storiesSection = findViewById(R.id.section_stories);

        TopAttractionsRecyclerView = findViewById(R.id.recyclerView_TopAttractions);
        natureRecyclerView = findViewById(R.id.recyclerView_Nature);
        foodRecyclerView = findViewById(R.id.recyclerView_Food);
        cultureRecyclerView = findViewById(R.id.recyclerView_Culture);
        nearbyCityRecyclerView = findViewById(R.id.recycler_view_nearbyDest);
        travelStoryRecyclerView = findViewById(R.id.recycler_view_stories);
        nearbyCityRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        // Set GridLayoutManager with 2 columns
        TopAttractionsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        natureRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        foodRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        cultureRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        travelStoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

//        cities = Arrays.asList(
//                new City("Bangalore", "India", "Modern", "1", "https://images.pexels.com/photos/4428276/pexels-photo-4428276.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "4.8", "A vibrant city known for its technology industry and parks."),
//                new City("Delhi", "India", "Historical", "2", "https://images.pexels.com/photos/4428291/pexels-photo-4428291.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "4.5", "The capital city of India, known for its rich history and culture."),
//                new City("Mumbai", "India", "Modern", "3", "https://images.pexels.com/photos/4428274/pexels-photo-4428274.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "4.7", "A bustling metropolis known for Bollywood and the Gateway of India."),
//                new City("Chennai", "India", "Coastal", "4", "https://images.pexels.com/photos/4428272/pexels-photo-4428272.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "4.6", "A coastal city famous for its cultural heritage and temples."),
//                new City("Hyderabad", "India", "Historical", "5", "https://images.pexels.com/photos/1604287/pexels-photo-1604287.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "4.4", "Known for its iconic Charminar and rich history.")
//
//
//        );
        nearByCityAdapter = new NearByCityAdapter(this,nearByCitiesList);
        fetchNearByCities();
        nearbyCityRecyclerView.setAdapter(nearByCityAdapter);



//                List<ItineraryPlace> naturePlaces = Arrays.asList(
//                new ItineraryPlace("Nature Park 1", "PlaceID1", "https://images.pexels.com/photos/1659438/pexels-photo-1659438.jpeg", "Description 1"),
//                new ItineraryPlace("Nature Park 2", "PlaceID2", "https://images.pexels.com/photos/147411/italy-mountains-dawn-daybreak-147411.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 2"),
//                new ItineraryPlace("Nature Park 2", "PlaceID2", "https://images.pexels.com/photos/247599/pexels-photo-247599.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 2"),
//                new ItineraryPlace("Nature Park 2", "PlaceID2", "https://images.pexels.com/photos/158063/bellingrath-gardens-alabama-landscape-scenic-158063.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 2")
//        );

//        List<ItineraryPlace> foodPlaces = Arrays.asList(
//                new ItineraryPlace("Food Place 1", "PlaceID3", "https://images.pexels.com/photos/70497/pexels-photo-70497.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 3"),
//                new ItineraryPlace("Food Place 2", "PlaceID4", "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 4"),
//                new ItineraryPlace("Food Place 2", "PlaceID4", "https://images.pexels.com/photos/70497/pexels-photo-70497.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 4"),
//                new ItineraryPlace("Food Place 2", "PlaceID4", "https://images.pexels.com/photos/769289/pexels-photo-769289.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 4")
//        );

//        List<ItineraryPlace> culturePlaces = Arrays.asList(
//                new ItineraryPlace("Culture Spot 1", "PlaceID5", "https://images.pexels.com/photos/974320/pexels-photo-974320.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 5"),
//                new ItineraryPlace("Culture Spot 2", "PlaceID6", "https://images.pexels.com/photos/161251/senso-ji-temple-japan-kyoto-landmark-161251.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 6"),
//                new ItineraryPlace("Culture Spot 2", "PlaceID6", "https://images.pexels.com/photos/3185480/pexels-photo-3185480.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 6"),
//                new ItineraryPlace("Culture Spot 2", "PlaceID6", "https://images.pexels.com/photos/937465/pexels-photo-937465.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 6")
//        );
        places = new ArrayList<>();
        places.add(new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/4428276/pexels-photo-4428276.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "An iconic arch monument in Mumbai."));
        places.add(new ItineraryPlace("Mahatma Gandhi Park", "ChIJUXbUGHsWrjsRmUwSArERj1E", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "A famous promenade along the coast."));
        places.add(new ItineraryPlace("Lalbagh", "ChIJHdPykcEVrjsRIr4v35kLEY4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "Ancient rock-cut temples on an island."));
        places.add(new ItineraryPlace("Shri Gavi", "ChIJh6gk5PUVrjsRT9dhFc5-nrI", "https://images.pexels.com/photos/4428276/pexels-photo-4428276.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "A vibrant street market."));
        places.add(new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "An iconic arch monument in Mumbai."));

//uploadTravelStories();
        getPostsByPlaceId(cityId);
        travelStoryAdapter = new TravelStoryAdapter(this, travelStories);
        travelStoryRecyclerView.setAdapter(travelStoryAdapter);


//        thingsToDoAdapter = new ThingsToDoAdapter(this,places);
//        TopAttractionsRecyclerView.setAdapter(thingsToDoAdapter);
        // Set adapters for each RecyclerView
//        natureAdapter = new ThingsToDoAdapter(this, naturePlaces);
//        foodAdapter = new ThingsToDoAdapter(this, foodPlaces);
//        cultureAdapter = new ThingsToDoAdapter(this, culturePlaces);

//        natureRecyclerView.setAdapter(natureAdapter);
//        foodRecyclerView.setAdapter(foodAdapter);
//        cultureRecyclerView.setAdapter(cultureAdapter);

        fetchPlacePhotos(cityId);
        // Set up the ImageSliderAdapter
        imageSliderAdapter = new ImageSliderAdapter(this, imageUrls);

// Set the adapter to the ViewPager2
        viewPager.setAdapter(imageSliderAdapter);

// Attach TabLayoutMediator to sync dots with the ViewPager2 pages
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Some implementation (if needed, e.g., set tab text)
        }).attach();

        // Set up automatic scrolling
        startAutoScroll();


        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int scrollToY = 0;

                switch (tab.getPosition()) {
                    case 0: // Overview
                        scrollToY = overviewSection.getTop();
                        break;
                    case 1: // Things to do
                        scrollToY = thingsToDoSection.getTop();
                        break;
                    case 2: // Nearby
                        scrollToY = nearbySection.getTop();
                        break;
                    case 3: // Travel Stories
                        scrollToY = storiesSection.getTop();
                        break;
                }

                // Scroll to the target section
                nestedScrollView.smoothScrollTo(0, scrollToY);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Handle tab selection
        thingsToDoTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        // Show Nature RecyclerView
                        TopAttractionsRecyclerView.setVisibility(View.VISIBLE);
                        natureRecyclerView.setVisibility(View.GONE);
                        foodRecyclerView.setVisibility(View.GONE);
                        cultureRecyclerView.setVisibility(View.GONE);
                        if (!thingsToDoDataFetched) {
                            fetchPlaces("thingsToDo");
                        }
                        break;
                    case 1:
                        // Show Food RecyclerView
                        TopAttractionsRecyclerView.setVisibility(View.GONE);
                        natureRecyclerView.setVisibility(View.VISIBLE);
                        foodRecyclerView.setVisibility(View.GONE);
                        cultureRecyclerView.setVisibility(View.GONE);

                        // Check if Nature data has been fetched, if not, fetch it
                        if (!natureDataFetched) {
                            fetchPlaces("nature");
                        }

                        break;
                    case 2:
                        // Show Culture RecyclerView
                        TopAttractionsRecyclerView.setVisibility(View.GONE);
                        natureRecyclerView.setVisibility(View.GONE);
                        foodRecyclerView.setVisibility(View.VISIBLE);
                        cultureRecyclerView.setVisibility(View.GONE);

                        if (!foodDataFetched) {
                            fetchPlaces("food");
                        }


                        break;
                    case 3:
                        // Show Culture RecyclerView
                        TopAttractionsRecyclerView.setVisibility(View.GONE);
                        natureRecyclerView.setVisibility(View.GONE);
                        foodRecyclerView.setVisibility(View.GONE);
                        cultureRecyclerView.setVisibility(View.VISIBLE);

                        if (!cultureDataFetched) {
                            fetchPlaces("culture");
                        }

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Optional: Handle if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Optional: Handle if needed
            }
        });




        // Set up the back button behavior
        topAppBar.setNavigationOnClickListener(v -> {
            // Use OnBackPressedDispatcher instead of the deprecated onBackPressed()
            OnBackPressedDispatcherOwner backPressedDispatcherOwner = (OnBackPressedDispatcherOwner) this;
            backPressedDispatcherOwner.getOnBackPressedDispatcher().onBackPressed();
        });

        // Set up the menu item click listeners
        topAppBar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.share:
                    // Handle share button press
                    return true;
                case R.id.favorite:
                    // Handle favorite button press
                    return true;
                default:
                    return false;
            }
        });


    }
    // Method to fetch places for different categories
    private void fetchPlaces(String category) {

        int radius = 50000; // Example: 50 km radius
        List<String> cityTypes = new ArrayList<>();

        // Define types based on the category
        switch (category) {
            case "thingsToDo":
                cityTypes.add("amusement_park");
                cityTypes.add("movie_theater");
                cityTypes.add("zoo");
                break;

            case "nature":
                cityTypes.add("beach");
                cityTypes.add("natural_feature");
                cityTypes.add("hiking_area");
                cityTypes.add("garden");
                break;

            case "food":
                cityTypes.add("restaurant");
                cityTypes.add("cafe");
                cityTypes.add("fast_food_restaurant");
                cityTypes.add("juice_shop");
                break;

            case "culture":
                cityTypes.add("cultural_landmark");
                cityTypes.add("historical_place");
                cityTypes.add("mosque");
                cityTypes.add("church");
                cityTypes.add("hindu_temple");
                break;
        }

        // Perform the search only if not already fetched
        googlePlacesUtils.searchNearbyPlaces(cityLat, cityLng, radius, cityTypes, 3, new GoogleMapsAPIsUtils.NearbySearchCallback() {
            @Override
            public void onSuccess(List<PlaceDetails> places) {
                // Based on the category, update the appropriate list and set visibility
                switch (category) {
                    case "thingsToDo":
                        thingsToDoPlaces = places;
                        thingsToDoDataFetched = true;
                        // Update RecyclerView with fetched places
                        updateRecyclerView(TopAttractionsRecyclerView, thingsToDoPlaces);
                    case "nature":
                        naturePlaces = places;
                        natureDataFetched = true;
                        // Update RecyclerView with fetched places
                        updateRecyclerView(natureRecyclerView, naturePlaces);
                        break;

                    case "food":
                        foodPlaces = places;
                        foodDataFetched = true;
                        // Update RecyclerView with fetched places
                        updateRecyclerView(foodRecyclerView, foodPlaces);
                        break;

                    case "culture":
                        culturePlaces = places;
                        cultureDataFetched = true;
                        // Update RecyclerView with fetched places
                        updateRecyclerView(cultureRecyclerView, culturePlaces);
                        break;
                }
                Log.e("PlacesAPI", "Error fetching places" +places.toString() );
            }

            @Override
            public void onError(String errorMessage) {

                // Handle failure (e.g., show a Toast or retry the request)
                Log.e("PlacesAPI", "category is "+category + errorMessage);
            }

        });
    }

    // Method to update RecyclerView with the fetched places
    private void updateRecyclerView(RecyclerView recyclerView, List<PlaceDetails> places) {
        // You can use an adapter to bind the data to the RecyclerView
        ThingsToDoAdapter placeAdapter = new ThingsToDoAdapter(this,places);
        recyclerView.setAdapter(placeAdapter);



    }


    private void fetchNearByCities() {

       // getPopularCities();

        FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(cityId, Arrays.asList(Place.Field.LAT_LNG));

        placesClient.fetchPlace(placeRequest).addOnSuccessListener(placeResponse -> {
            LatLng cityLatLng = placeResponse.getPlace().getLatLng();
            if (cityLatLng != null) {
                // The city coordinates are retrieved
                 cityLat = cityLatLng.latitude;
                 cityLng = cityLatLng.longitude;


                int radius = 50000; // Example: 50 km radius

                List<String> cityTypes = Arrays.asList("locality");
                googlePlacesUtils.searchNearbyPlaces(cityLat, cityLng, radius, cityTypes, 3,new GoogleMapsAPIsUtils.NearbySearchCallback() {
                    @Override
                    public void onSuccess(List<PlaceDetails> places) {
                        nearByCitiesList.addAll(places);
                        nearByCityAdapter.notifyDataSetChanged(); // Update adapter after fetching data


                        Log.e("diningList", "diningList: " + nearByCitiesList.toString());

                    }



                    @Override
                    public void onError(String errorMessage) {

                        getPopularCities();

                        Log.e("ExploreFragment", "Error fetching nearby places: " + errorMessage);
                        Toast.makeText(ExploreCityActivity.this, "Error fetching nearby places", Toast.LENGTH_SHORT).show();
                    }
                });




            }
        }).addOnFailureListener(e -> {
            Log.e("Error", "Error fetching city coordinates", e);
        });

    }


        private void getPopularCities() {
        // Create a list of predefined popular cities
        List<PlaceDetails> popularCities = Arrays.asList(
                new PlaceDetails("1", "City 1", 4.5, "Address 1", true),
                new PlaceDetails("2", "City 2", 4.2, "Address 2", false),
                new PlaceDetails("3", "City 3", 4.8, "Address 3", true)

        );
            nearByCitiesList.addAll(popularCities);
            nearByCityAdapter.notifyDataSetChanged(); // Update adapter after fetching data


            // Now cityList contains popular cities
    }

    private void startAutoScroll() {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    int currentItem = viewPager.getCurrentItem();
                    int itemCount = imageSliderAdapter.getItemCount();

                    // Move to the next item or loop back to the first item
                    if (currentItem < itemCount - 1) {
                        viewPager.setCurrentItem(currentItem + 1, true);
                    } else {
                        viewPager.setCurrentItem(0, true);
                    }
                });
            }
        }, 0, 2000); // Delay and period in milliseconds
    }


    private void uploadTravelStories() {
        travelStories = Arrays.asList(
                // Photo-based posts
                new TravelStory(
                        "John Doe",
                        "2023-12-01",
                        "Goa Beach Adventure",
                        "Exploring the serene beaches of Goa.",
                        Arrays.asList("https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg"),
                        null, // No video URL provided
                        "Goa, India",
                        1200, // Views
                        350, // Likes
                        45,  // Comments
                        "https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg" // User profile picture
                ),
                new TravelStory(
                        "Alice Smith",
                        "2023-11-28",
                        "Sunset at Palolem Beach",
                        "A breathtaking sunset view at Palolem Beach.",
                        Arrays.asList("https://images.pexels.com/photos/460672/pexels-photo-460672.jpeg"),
                        null, // No video URL provided
                        "Goa, India",
                        900, // Views
                        250, // Likes
                        30,  // Comments
                        "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg" // User profile picture
                ),
                new TravelStory(
                        "David Brown",
                        "2023-11-15",
                        "Historical Fort Aguada",
                        "Exploring the ancient Fort Aguada.",
                        Arrays.asList("https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg"),
                        null, // No video URL provided
                        "Goa, India",
                        800, // Views
                        200, // Likes
                        25,  // Comments
                        "https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg" // User profile picture
                ),
                new TravelStory(
                        "Emily Davis",
                        "2023-11-10",
                        "Nightlife in Goa",
                        "Experiencing the vibrant nightlife.",
                        Arrays.asList("https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg"),
                        null, // No video URL provided
                        "Goa, India",
                        1000, // Views
                        300, // Likes
                        35,  // Comments
                        "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg" // User profile picture
                ),
                new TravelStory(
                        "Michael Lee",
                        "2023-11-05",
                        "Goan Cuisine",
                        "Tasting the delicious seafood in Goa.",
                        Arrays.asList("https://images.pexels.com/photos/4676439/pexels-photo-4676439.jpeg"),
                        null, // No video URL provided
                        "Goa, India",
                        750, // Views
                        220, // Likes
                        20,  // Comments
                        "https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg" // User profile picture
                ),

                // Video-based posts
                new TravelStory(
                        "Sophia Wilson",
                        "2023-11-01",
                        "Watersports Adventure",
                        "Thrilling water sports activities in Goa.",
                        null, // No photo URL provided
                        "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        "Goa, India",
                        950, // Views
                        280, // Likes
                        40,  // Comments
                        "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg" // User profile picture
                ),
                new TravelStory(
                        "Chris Taylor",
                        "2023-10-28",
                        "Parasailing Experience",
                        "Soaring through the skies of Goa.",
                        null, // No photo URL provided
                        "https://samplelib.com/lib/preview/mp4/sample-5s.mp4",
                        "Goa, India",
                        1100, // Views
                        400, // Likes
                        50,  // Comments
                        "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg" // User profile picture
                ),
                new TravelStory(
                        "Laura Green",
                        "2023-10-20",
                        "Scuba Diving Adventure",
                        "Exploring the underwater world.",
                        null, // No photo URL provided
                        "https://www.appsloveworld.com/wp-content/uploads/2018/10/640.mp4",
                        "Goa, India",
                        1300, // Views
                        500, // Likes
                        60,  // Comments
                        "https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg" // User profile picture
                ),
                new TravelStory(
                        "James White",
                        "2023-10-15",
                        "Goa's Carnival Festivities",
                        "Enjoying the vibrant carnival celebrations.",
                        null, // No photo URL provided
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4",
                        "Goa, India",
                        1500, // Views
                        550, // Likes
                        70,  // Comments
                        "https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg" // User profile picture
                ),
                new TravelStory(
                        "Megan Brown",
                        "2023-10-10",
                        "Jungle Trekking",
                        "Venturing into Goa's lush jungles.",
                        null, // No photo URL provided
                        "https://www.w3schools.com/html/mov_bbb.mp4",
                        "Goa, India",
                        1400, // Views
                        530, // Likes
                        65,  // Comments
                        "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg" // User profile picture
                )
        );


    }


    // Method to retrieve posts with the same placeId as the parameter
    private void getPostsByPlaceId(String placeId) {
        // Retrieve posts from Firestore where placeId matches the parameter
        travelStories = new ArrayList<>();

        FirebaseUtils.getPostsCollection()
                .whereEqualTo("placeId", placeId) // Filter by placeId
                .get() // Perform the query
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();


                        // Loop through the query results and create TravelStory objects
                        for (DocumentSnapshot document : querySnapshot) {
                            TravelStory travelStory = document.toObject(TravelStory.class);
                            if (travelStory != null) {
                                travelStories.add(travelStory); // Add the TravelStory to the list
                            }
                        }


                        travelStoryAdapter.notifyDataSetChanged();




                    } else {
                        // If the query fails, show an error message
                        Log.e("PostActivity", "Error fetching posts: " + task.getException());
                    }
                });
    }

    private void fetchPlacePhotos(String placeId) {

                imageUrls = Arrays.asList(
                "https://images.pexels.com/photos/974320/pexels-photo-974320.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "https://images.pexels.com/photos/161251/senso-ji-temple-japan-kyoto-landmark-161251.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
               "https://images.pexels.com/photos/3185480/pexels-photo-3185480.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "https://images.pexels.com/photos/937465/pexels-photo-937465.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        );
//
//        // Request DISPLAY_NAME, PHOTO_METADATAS, and EDITORIAL_SUMMARY fields
//        final List<Place.Field> fields = Arrays.asList(
//                Place.Field.NAME,
//                Place.Field.PHOTO_METADATAS
//        );
//
//        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);
//
//        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
//            final Place place = response.getPlace();
//            // Get the city name (DISPLAY_NAME)
//            String displayName = place.getName();
//            if (displayName != null) {
//                // Instead of Fragment's TextView, access the Activity's TextView
//
//                cityNameTV.setText(displayName);
//            }
//
//            // Get the photo metadata
//            final List<PhotoMetadata> metadataList = place.getPhotoMetadatas();
//            if (metadataList == null || metadataList.isEmpty()) {
//                Log.w("PlaceActivity", "No photo metadata available.");
//                return;
//            }
//
//            // Loop through each photo metadata and request the photo URI
//            for (PhotoMetadata photoMetadata : metadataList) {
//                // Create a FetchResolvedPhotoUriRequest for each photo
//                FetchResolvedPhotoUriRequest photoRequest = FetchResolvedPhotoUriRequest.builder(photoMetadata)
//                        .setMaxWidth(500) // Adjust width and height as needed
//                        .setMaxHeight(300)
//                        .build();
//
//                // Request the photo URI
//                placesClient.fetchResolvedPhotoUri(photoRequest).addOnSuccessListener(fetchResolvedPhotoUriResponse -> {
//                    Uri uri = fetchResolvedPhotoUriResponse.getUri();
//                    String photographer = photoMetadata.getAttributions(); // Get photographer attribution if available
//
//                    // Pass the Uri and photographer name to the adapter
//                    imageSliderAdapter.addPhoto(uri);
//                }).addOnFailureListener(exception -> {
//                    if (exception instanceof ApiException) {
//                        Log.e("PlaceActivity", "Error fetching photo: " + exception.getMessage());
//                    }
//                });
//            }
//        }).addOnFailureListener(exception -> {
//            if (exception instanceof ApiException) {
//                Log.e("PlaceActivity", "Error fetching place details: " + ((ApiException) exception).getStatusCode());
//            }
//        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the timer to avoid memory leaks
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}

