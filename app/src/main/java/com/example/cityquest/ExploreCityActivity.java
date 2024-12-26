package com.example.cityquest;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
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
import com.example.cityquest.model.TravelStory;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExploreCityActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ImageSliderAdapter imageSliderAdapter;
    private List<String> imageUrls;

    private TabLayout mainTabLayout;
    private NestedScrollView nestedScrollView;
    private View overviewSection,thingsToDoSection,nearbySection,storiesSection;


    ThingsToDoAdapter thingsToDoAdapter ,natureAdapter, foodAdapter, cultureAdapter;
    List<ItineraryPlace> places;
    private TabLayout thingsToDoTabLayout;
    private RecyclerView TopAttractionsRecyclerView,natureRecyclerView, foodRecyclerView, cultureRecyclerView;

    private NearByCityAdapter nearByCityAdapter;
    private List<City> cities;
    private RecyclerView nearbyCityRecyclerView;

    private TravelStoryAdapter travelStoryAdapter;
    private List<TravelStory> travelStories;
    private RecyclerView travelStoryRecyclerView;




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


//        // Finding the VideoView by its ID//TODO use for community post item
//        VideoView videoView = findViewById(R.id.videoView);
//
//        // Creating a Uri object to refer to the resource from the videoUrl
//        Uri uri = Uri.parse(videoUrl);
//
//        // Setting the video URI to the VideoView
//        videoView.setVideoURI(uri);
//
////        // Creating an object of MediaController class
////        MediaController mediaController = new MediaController(this);
////
////        // Setting the anchor view for the MediaController
////        mediaController.setAnchorView(videoView);
////
////        // Associating the MediaController with the VideoView
////        videoView.setMediaController(mediaController);
//
//        // Starting the video playback
//        videoView.start();

        topAppBar = findViewById(R.id.topAppBar);
        // Initialize the ViewPager2 and TabLayout
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        thingsToDoTabLayout = findViewById(R.id.tab_layout_things_to_do);

        mainTabLayout = findViewById(R.id.tab_layout_for_cityExplore);
        nestedScrollView = findViewById(R.id.nested_scrollView_explore_city);
        overviewSection = findViewById(R.id.section_overview);
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

        cities = Arrays.asList(
                new City("Bangalore", "India", "Modern", "1", "https://images.pexels.com/photos/4428276/pexels-photo-4428276.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "4.8", "A vibrant city known for its technology industry and parks."),
                new City("Delhi", "India", "Historical", "2", "https://images.pexels.com/photos/4428291/pexels-photo-4428291.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "4.5", "The capital city of India, known for its rich history and culture."),
                new City("Mumbai", "India", "Modern", "3", "https://images.pexels.com/photos/4428274/pexels-photo-4428274.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "4.7", "A bustling metropolis known for Bollywood and the Gateway of India."),
                new City("Chennai", "India", "Coastal", "4", "https://images.pexels.com/photos/4428272/pexels-photo-4428272.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "4.6", "A coastal city famous for its cultural heritage and temples."),
                new City("Hyderabad", "India", "Historical", "5", "https://images.pexels.com/photos/1604287/pexels-photo-1604287.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "4.4", "Known for its iconic Charminar and rich history.")


        );
        nearByCityAdapter = new NearByCityAdapter(this,cities);
        nearbyCityRecyclerView.setAdapter(nearByCityAdapter);



                List<ItineraryPlace> naturePlaces = Arrays.asList(
                new ItineraryPlace("Nature Park 1", "PlaceID1", "https://images.pexels.com/photos/1659438/pexels-photo-1659438.jpeg", "Description 1"),
                new ItineraryPlace("Nature Park 2", "PlaceID2", "https://images.pexels.com/photos/147411/italy-mountains-dawn-daybreak-147411.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 2"),
                new ItineraryPlace("Nature Park 2", "PlaceID2", "https://images.pexels.com/photos/247599/pexels-photo-247599.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 2"),
                new ItineraryPlace("Nature Park 2", "PlaceID2", "https://images.pexels.com/photos/158063/bellingrath-gardens-alabama-landscape-scenic-158063.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 2")
        );

        List<ItineraryPlace> foodPlaces = Arrays.asList(
                new ItineraryPlace("Food Place 1", "PlaceID3", "https://images.pexels.com/photos/70497/pexels-photo-70497.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 3"),
                new ItineraryPlace("Food Place 2", "PlaceID4", "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 4"),
                new ItineraryPlace("Food Place 2", "PlaceID4", "https://images.pexels.com/photos/70497/pexels-photo-70497.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 4"),
                new ItineraryPlace("Food Place 2", "PlaceID4", "https://images.pexels.com/photos/769289/pexels-photo-769289.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 4")
        );

        List<ItineraryPlace> culturePlaces = Arrays.asList(
                new ItineraryPlace("Culture Spot 1", "PlaceID5", "https://images.pexels.com/photos/974320/pexels-photo-974320.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 5"),
                new ItineraryPlace("Culture Spot 2", "PlaceID6", "https://images.pexels.com/photos/161251/senso-ji-temple-japan-kyoto-landmark-161251.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 6"),
                new ItineraryPlace("Culture Spot 2", "PlaceID6", "https://images.pexels.com/photos/3185480/pexels-photo-3185480.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 6"),
                new ItineraryPlace("Culture Spot 2", "PlaceID6", "https://images.pexels.com/photos/937465/pexels-photo-937465.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Description 6")
        );
        places = new ArrayList<>();
        places.add(new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/4428276/pexels-photo-4428276.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "An iconic arch monument in Mumbai."));
        places.add(new ItineraryPlace("Mahatma Gandhi Park", "ChIJUXbUGHsWrjsRmUwSArERj1E", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "A famous promenade along the coast."));
        places.add(new ItineraryPlace("Lalbagh", "ChIJHdPykcEVrjsRIr4v35kLEY4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "Ancient rock-cut temples on an island."));
        places.add(new ItineraryPlace("Shri Gavi", "ChIJh6gk5PUVrjsRT9dhFc5-nrI", "https://images.pexels.com/photos/4428276/pexels-photo-4428276.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "A vibrant street market."));
        places.add(new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "An iconic arch monument in Mumbai."));

        uploadTravelStories();
        travelStoryAdapter = new TravelStoryAdapter(this, travelStories);
        travelStoryRecyclerView.setAdapter(travelStoryAdapter);


        thingsToDoAdapter = new ThingsToDoAdapter(this,places);
        TopAttractionsRecyclerView.setAdapter(thingsToDoAdapter);
        // Set adapters for each RecyclerView
        natureAdapter = new ThingsToDoAdapter(this, naturePlaces);
        foodAdapter = new ThingsToDoAdapter(this, foodPlaces);
        cultureAdapter = new ThingsToDoAdapter(this, culturePlaces);

        natureRecyclerView.setAdapter(natureAdapter);
        foodRecyclerView.setAdapter(foodAdapter);
        cultureRecyclerView.setAdapter(cultureAdapter);
        imageUrls = Arrays.asList(
                "https://images.pexels.com/photos/974320/pexels-photo-974320.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "https://images.pexels.com/photos/161251/senso-ji-temple-japan-kyoto-landmark-161251.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
               "https://images.pexels.com/photos/3185480/pexels-photo-3185480.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "https://images.pexels.com/photos/937465/pexels-photo-937465.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        );
        // Set up the ImageSliderAdapter
        imageSliderAdapter = new ImageSliderAdapter(this, imageUrls);

// Set the adapter to the ViewPager2
        viewPager.setAdapter(imageSliderAdapter);

// Attach TabLayoutMediator to sync dots with the ViewPager2 pages
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Some implementation (if needed, e.g., set tab text)
        }).attach();


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
                        break;
                    case 1:
                        // Show Food RecyclerView
                        TopAttractionsRecyclerView.setVisibility(View.GONE);
                        natureRecyclerView.setVisibility(View.VISIBLE);
                        foodRecyclerView.setVisibility(View.GONE);
                        cultureRecyclerView.setVisibility(View.GONE);
                        break;
                    case 2:
                        // Show Culture RecyclerView
                        TopAttractionsRecyclerView.setVisibility(View.GONE);
                        natureRecyclerView.setVisibility(View.GONE);
                        foodRecyclerView.setVisibility(View.VISIBLE);
                        cultureRecyclerView.setVisibility(View.GONE);
                        break;
                    case 3:
                        // Show Culture RecyclerView
                        TopAttractionsRecyclerView.setVisibility(View.GONE);
                        natureRecyclerView.setVisibility(View.GONE);
                        foodRecyclerView.setVisibility(View.GONE);
                        cultureRecyclerView.setVisibility(View.VISIBLE);
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

    //TODO video playback
//    @Override
//    protected void onPause() {
//        super.onPause();
//        VideoView videoView = findViewById(R.id.videoView);
//        videoView.pause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        VideoView videoView = findViewById(R.id.videoView);
//        videoView.start();
//    }
}

