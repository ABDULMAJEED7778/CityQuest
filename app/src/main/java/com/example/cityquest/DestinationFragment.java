package com.example.cityquest;

import static com.example.cityquest.Database.AppDatabase.databaseWriteExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.cityquest.Database.AppDatabase;
import com.example.cityquest.adapter.PlaceAutocompleteAdapter;
import com.example.cityquest.adapter.PopularCityAdapter;
import com.example.cityquest.apiCalls.VolleyCallback;
import com.example.cityquest.model.City;
import com.example.cityquest.model.ItineraryPlace;
import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.model.TripDay;
import com.example.cityquest.model.Trips;
import com.example.cityquest.model.User;
import com.example.cityquest.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DestinationFragment extends Fragment {

    private Button skipBtn;
    private AutoCompleteTextView searchET;
    private RecyclerView searchRecyclerView;
    private LottieAnimationView loadingAnim;

    private PopularCityAdapter popularCityAdapter;
    RecyclerView recyclerViewCityPopular;
    private PlacesClient placesClient;
    private List<City> cities;
    private ChipGroup chipGroup;
    private PlaceAutocompleteAdapter suggestionAdapter;
    AppDatabase database;

    public DestinationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Safely initialize the database
        database = AppDatabase.getDatabase(requireContext().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);
        initializeViews(view);
        initializePlacesClient();
        initializeRecyclerViews();
        setListeners();
        fetchCitiesFromFirestore();

        FirebaseUser currentUser = FirebaseUtils.getCurrentUser();
        if (currentUser != null) {
            setUserInfo(currentUser.getUid());
        }


        skipBtn.setOnClickListener(v->{

            ReadyTrips trip = new ReadyTrips(UUID.randomUUID().toString(), "Trip to Bangalore", "https://images.pexels.com/photos/1036861/pexels-photo-1036861.jpeg", 4.5f,
                    "Bangalore", "India", "Cloudy", 25.0f, "City & Nature", "Solo",
                    "2024-04-18", "2024-04-20", "Embark on a journey through the Cultural Delights of Bangalore, where vibrant traditions meet modern innovation. Explore bustling markets, savor aromatic spices, and immerse yourself in the rich heritage of India's Garden City."
            ,Arrays.asList("Great food", "Nightlife and Entertainment", "Shopping Experience"),"ChIJbU60yXAWrjsR4E9-UejD3_g");




            trip.setSynced(false);

            List<TripDay> itinerary = new ArrayList<>();

            // Example: Adding an itinerary with five places each day
            itinerary.add(new TripDay(1, Arrays.asList(
                    new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "An iconic arch monument in Mumbai."),
                    new ItineraryPlace("Mahatma Gandhi Park", "ChIJUXbUGHsWrjsRmUwSArERj1E", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "A famous promenade along the coast."),
                    new ItineraryPlace("Lalbagh", "ChIJHdPykcEVrjsRIr4v35kLEY4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "Ancient rock-cut temples on an island.")
//
            )));
            itinerary.add(new TripDay(2, Arrays.asList(

                    new ItineraryPlace("Lalbagh", "ChIJHdPykcEVrjsRIr4v35kLEY4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "Ancient rock-cut temples on an island."),
                    new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "An iconic arch monument in Mumbai."),
                    new ItineraryPlace("Mahatma Gandhi Park", "ChIJUXbUGHsWrjsRmUwSArERj1E", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "A famous promenade along the coast.")
//
            )));
            itinerary.add(new TripDay(3, Arrays.asList(
                    new ItineraryPlace("Mahatma Gandhi Park", "ChIJUXbUGHsWrjsRmUwSArERj1E", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "A famous promenade along the coast."),
                    new ItineraryPlace("Bengaluru Palace", "ChIJN1ZKKUkWrjsRzxIVM363-LE", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "An iconic arch monument in Mumbai."),

                    new ItineraryPlace("Lalbagh", "ChIJHdPykcEVrjsRIr4v35kLEY4", "https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg", "Ancient rock-cut temples on an island.")
//
            )));




            Log.e("TripId","before db and fire "+trip.getTripId());

                databaseWriteExecutor.execute(() -> {
                    database.readyTripsDao().insertTrip(trip);
                });
                saveTripToFirebase(trip);


            Intent intent = new Intent(getActivity(), ReadyPlanDetailsActivity.class);
            intent.putExtra("trip", trip);  // Pass the trip object
            intent.putParcelableArrayListExtra("itinerary", (ArrayList<? extends Parcelable>) itinerary);

            startActivity(intent);

        });



        return view;
    }


    private void initializeViews(View view) {
        searchRecyclerView = view.findViewById(R.id.recyclerView_search_suggestion);
        loadingAnim = view.findViewById(R.id.loading_anim_dest_page);
        recyclerViewCityPopular = view.findViewById(R.id.recyclerView_city_popular);

        searchET = view.findViewById(R.id.search_input);
        //mapBtn = view.findViewById(R.id.mapBtn_dest);

        skipBtn = view.findViewById(R.id.skipBtn_dest);
        //logOutBtn = view.findViewById(R.id.logoutBtn_dest);
        chipGroup = view.findViewById(R.id.chipGroup_filters);
        Chip defaultChip = view.findViewById(R.id.chip_historical); // Default chip
        defaultChip.setChecked(true);
    }

    private void initializePlacesClient() {
        String apiKey = BuildConfig.MAPS_API_KEY;
        Places.initializeWithNewPlacesApiEnabled(requireContext(), apiKey);
        placesClient = Places.createClient(requireContext());
    }

    private void initializeRecyclerViews() {
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        suggestionAdapter = new PlaceAutocompleteAdapter(requireContext(), new ArrayList<>());
        searchRecyclerView.setAdapter(suggestionAdapter);
        cities = new ArrayList<>();
        popularCityAdapter = new PopularCityAdapter(cities);
        recyclerViewCityPopular.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewCityPopular.setAdapter(popularCityAdapter);
    }

    private void setListeners() {
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                Chip defaultChip = group.findViewById(R.id.chip_historical);
                defaultChip.setChecked(true);
                Toast.makeText(requireContext(), "At least one chip must be selected", Toast.LENGTH_SHORT).show();
                return;
            }
            filterCitiesByType(checkedIds);
        });

        //mapBtn.setOnClickListener(v -> startActivity(new Intent(requireContext(), MapActivity.class)));


//        logOutBtn.setOnClickListener(v -> {
//            FirebaseUtils.signOut();
//            startActivity(new Intent(requireContext(), SignInActivity.class));
//            requireActivity().finish();
//        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    fetchPlacePredictions(s.toString());
                } else {
                    suggestionAdapter.updateData(new ArrayList<>());
                    searchRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        suggestionAdapter.setOnItemClickListener(position -> {
            AutocompletePrediction selectedPrediction = suggestionAdapter.getItemAt(position);
            if (selectedPrediction != null) {
                String placeId = selectedPrediction.getPlaceId();
                String cityName = selectedPrediction.getPrimaryText(null).toString();
                DestinationDetailsBottomSheet bottomSheet = DestinationDetailsBottomSheet.newInstance(placeId, cityName,this);
                bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
            }
        });
    }
    public void navigateToDateFragment(ReadyTrips trip) {

        DateRangeFragment dateFragment = new DateRangeFragment();
        Bundle args = new Bundle();
        args.putParcelable("trip", trip);
        dateFragment.setArguments(args);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, dateFragment)
                .addToBackStack(null)
                .commit();

    }


    private void fetchCitiesFromFirestore() {
        recyclerViewCityPopular.setVisibility(View.GONE);
        loadingAnim.setVisibility(View.VISIBLE);
        FirebaseUtils.getCitiesCollection()
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cities.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            cities.add(document.toObject(City.class));
                        }
                        filterCitiesByType(chipGroup.getCheckedChipIds());
                        popularCityAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("DestinationFragment", "Error fetching cities: ", task.getException());
                    }
                    recyclerViewCityPopular.setVisibility(View.VISIBLE);
                    loadingAnim.setVisibility(View.GONE);
                });
    }

    private void setUserInfo(String userId) {
        DocumentReference userRef = FirebaseUtils.getUserDocument(userId);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                User user = task.getResult().toObject(User.class);
                if (user != null) {
                    // Handle user info if needed
                }
            }
        });
    }

    private void fetchPlacePredictions(String query) {
        placesClient.findAutocompletePredictions(
                        FindAutocompletePredictionsRequest.builder()
                                .setTypesFilter(Arrays.asList("locality"))
                                .setQuery(query)
                                .build())
                .addOnSuccessListener(response -> {
                    suggestionAdapter.updateData(response.getAutocompletePredictions());
                    searchRecyclerView.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> Log.e("DestinationFragment", "Error fetching predictions: ", e));
    }

    private void filterCitiesByType(List<Integer> checkedIds) {
        List<String> selectedTypes = new ArrayList<>();
        for (int chipId : checkedIds) {
            switch (chipId) {
                case R.id.chip_historical:
                    selectedTypes.add("Historical");
                    break;
                case R.id.chip_Cultural:
                    selectedTypes.add("Cultural");
                    break;
                case R.id.chip_Modern:
                    selectedTypes.add("Modern");
                    break;
                case R.id.chip_coastal:
                    selectedTypes.add("Coastal");
                    break;
            }
        }
        filterCities(selectedTypes);
    }

    private void filterCities(List<String> selectedTypes) {
        List<City> filteredCities = new ArrayList<>();
        for (City city : cities) {
            if (selectedTypes.isEmpty() || selectedTypes.contains(city.getCityType())) {
                filteredCities.add(city);
            }
        }
        popularCityAdapter.updateCityList(filteredCities);
        popularCityAdapter.notifyDataSetChanged();
    }

    public void saveTripToFirebase(ReadyTrips trip) {


        // Save to Firestore
        FirebaseUtils.getTripsCollection(FirebaseUtils.getCurrentUser().getUid())
                .add(trip) // Add the trip to Firestore
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.e("Firebase", "Trip is added to Firestore", task.getException());
                            trip.setSynced(true);
                            databaseWriteExecutor.execute(() -> {
                                database.readyTripsDao().updateTrip(trip); // Update synced status
                            });
                            Log.e("TripId","after db and fire "+trip.getTripId());


                        } else {
                            Log.e("Firebase", "Error saving trip", task.getException());
                        }
                    }
                });
    }
}
