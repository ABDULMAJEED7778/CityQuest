package com.example.cityquest;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.adapter.ExplorePlaceAdapter;
import com.example.cityquest.adapter.PlaceAutocompleteAdapter;
import com.example.cityquest.model.PlaceDetails;
import com.example.cityquest.utils.GoogleMapsAPIsUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExploreFragment extends Fragment {

    private AutoCompleteTextView searchET;

    private RecyclerView searchRecyclerView;

    private RecyclerView diningRecyclerView;
    private RecyclerView localEventsRecyclerView;
    private RecyclerView shoppingRecyclerView;
    private ImageButton diningExpandBtn;
    private ImageButton localEventsExpandBtn;
    private ImageButton shoppingExpandBtn;

    private ExplorePlaceAdapter explorePlaceAdapter;

    private PlacesClient placesClient;
    private PlaceAutocompleteAdapter suggestionAdapter;
    private LinearLayoutManager layoutManager;
    private GoogleMapsAPIsUtils googlePlacesUtils;
    private List<PlaceDetails> diningList = new ArrayList<>();
    private List<PlaceDetails> localEventsList = new ArrayList<>();
    private List<PlaceDetails> shoppingList = new ArrayList<>();

    private ProgressBar diningProgressBar;
    private ProgressBar localEventsProgressBar;
    private ProgressBar shoppingProgressBar;

    private double latitude = 37.7749; // Example latitude
    private double longitude = -122.4194; // Example longitude


    private ReadyPlanDetailsActivity.ExploreLoadCallback loadCallback; // Use the common callback interface

    public ExploreFragment() {
        super(R.layout.fragment_explore); // Reference to your fragment layout
    }

    public void setLoadCompleteListener(ReadyPlanDetailsActivity.ExploreLoadCallback callback) {
        this.loadCallback = callback; // Set the common callback
    }

    private void onDataLoaded() {
        if (loadCallback != null) {
            loadCallback.onExploreLoaded(); // Notify the loading completion
        }
        // Set adapters for all RecyclerViews after data is loaded
        setAdapterForRecyclerView(diningRecyclerView, diningList);
        setAdapterForRecyclerView(localEventsRecyclerView, localEventsList);
        setAdapterForRecyclerView(shoppingRecyclerView, shoppingList);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize GoogleMapsAPIsUtils with context and API key
        googlePlacesUtils = new GoogleMapsAPIsUtils(requireContext(), getString(R.string.google_maps_api_key));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        Log.e("ExploreFragment", "onCreateView: loaded");



        placesClient = Places.createClient(getContext());
        searchET = view.findViewById(R.id.search_input_explore_fragment);
        searchRecyclerView = view.findViewById(R.id.recyclerView_search_suggestion_explore_activity);
        layoutManager = new LinearLayoutManager(getContext());

        searchRecyclerView.setLayoutManager(layoutManager);

        // Initialize Autocomplete Adapter
        suggestionAdapter = new PlaceAutocompleteAdapter(getContext(), new ArrayList<>());
        searchRecyclerView.setAdapter(suggestionAdapter);




        // Example types to include in the search
         List<String> includedTypes = Arrays.asList("restaurant", "cafe");


        explorePlaceAdapter = new ExplorePlaceAdapter(getContext(), new ArrayList<>());  // Set empty data initially

        diningRecyclerView = view.findViewById(R.id.dining_re_recyclerView);
        diningRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        diningExpandBtn = view.findViewById(R.id.dining_recommendation_listing_btn);
        diningProgressBar = view.findViewById(R.id.progress_bar_dining);

        localEventsRecyclerView = view.findViewById(R.id.localEvents_fest_recyclerView); // Add local events RecyclerView
        localEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        localEventsExpandBtn = view.findViewById(R.id.localEvents_fest_listing_btn); // Local events expand button
        localEventsProgressBar = view.findViewById(R.id.progress_bar_local_events);

        shoppingRecyclerView = view.findViewById(R.id.shopping_recyclerView); // Add shopping RecyclerView
        shoppingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        shoppingExpandBtn = view.findViewById(R.id.shopping_listing_btn); // Shopping expand button
        shoppingProgressBar = view.findViewById(R.id.progress_bar_shopping);

        diningExpandBtn.setOnClickListener(v -> {//TODO fix the toggle and progress bar


            toggleRecyclerViewVisibility(diningRecyclerView, diningList,diningProgressBar);
            setAdapterForRecyclerView(diningRecyclerView, diningList);


        });
        localEventsExpandBtn.setOnClickListener(v -> {
            toggleRecyclerViewVisibility(localEventsRecyclerView, localEventsList,localEventsProgressBar);
            setAdapterForRecyclerView(localEventsRecyclerView, localEventsList);
        });
        shoppingExpandBtn.setOnClickListener(v -> {
            toggleRecyclerViewVisibility(shoppingRecyclerView, shoppingList,shoppingProgressBar);
            setAdapterForRecyclerView(shoppingRecyclerView, shoppingList);

        });

        setupSearchInputListener();
        loadData();

        return view;
    }

    private void loadData() {
         //Simulate data loading with a delay
        new android.os.Handler().postDelayed(() -> {


            onDataLoaded();
        }, 0); // Simulating a 2-second delay for data loading
    }

    private void toggleRecyclerViewVisibility(RecyclerView recyclerView, List<PlaceDetails> list,ProgressBar progressBar) {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            if (list.isEmpty()) {

                progressBar.setVisibility(View.VISIBLE); // Show the loading indicator

               Toast.makeText(getContext(), progressBar.getVisibility() == View.VISIBLE?"visible":"not visible", Toast.LENGTH_SHORT).show();
                loadData();
                // Fetch data for the respective section
                fetchPlaceData(list);
                progressBar.setVisibility(View.GONE); // Show the loading indicator

            }
        }
    }

    private void fetchPlaceData(List<PlaceDetails> list) {



        int searchRadius = 5000; // Search radius

        // Example types for different categories
        List<String> diningTypes = Arrays.asList("restaurant", "cafe");//TODO adjust the types we can also exclude some types
        List<String> eventsTypes = Arrays.asList("cultural_center", "event_venue");
        List<String> shoppingTypes = Arrays.asList("shopping_mall", "store");

        if (list == diningList) {
            fetchNearbyPlaces(latitude, longitude, searchRadius, diningTypes, list);
        } else if (list == localEventsList) {
            fetchNearbyPlaces(latitude, longitude, searchRadius, eventsTypes, list);
        } else if (list == shoppingList) {
            fetchNearbyPlaces(latitude, longitude, searchRadius, shoppingTypes, list);
        }
    }

    private void fetchNearbyPlaces(double latitude, double longitude, int searchRadius, List<String> types, List<PlaceDetails> list) {
        googlePlacesUtils.searchNearbyPlaces(latitude, longitude, searchRadius, types, new GoogleMapsAPIsUtils.NearbySearchCallback() {
            @Override
            public void onSuccess(List<PlaceDetails> places) {
                list.addAll(places);
                explorePlaceAdapter.notifyDataSetChanged(); // Update adapter after fetching data


                Log.e("diningList", "diningList: " + diningList.toString());
                Log.e("localEventsList", "localEventsList: " + localEventsList.toString());
                Log.e("shoppingList", "shoppingList: " + shoppingList.toString());
            }

            @Override
            public void onError(String errorMessage) {


                Log.e("ExploreFragment", "Error fetching nearby places: " + errorMessage);
                Toast.makeText(getContext(), "Error fetching nearby places", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapterForRecyclerView(RecyclerView recyclerView, List<PlaceDetails> list) {
        explorePlaceAdapter = new ExplorePlaceAdapter(getContext(), list);
        recyclerView.setAdapter(explorePlaceAdapter);
    }

    private void setupSearchInputListener() {
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    // Fetch predictions after typing 3 or more characters
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
                Log.e("PlaceId", placeId);

                // Fetch the new location (latitude and longitude) based on the placeId
                fetchPlaceDetails(placeId);

                // Collapse the RecyclerViews
                collapseRecyclerViews();

                // Optionally, update UI with the new selected place
                // You can also show a Toast or update a TextView to reflect the selected place
                searchET.setText("");
                searchRecyclerView.setVisibility(View.GONE);
                // Close the keyboard
                hideKeyboard();
            }
        });
    }
    private void hideKeyboard() {
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(searchET.getWindowToken(), 0);
            }
        }
        searchET.clearFocus();  // Clear focus from the search input
    }

    private void fetchPlaceDetails(String placeId) {
        // Build the Place Details Request
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, Arrays.asList(Place.Field.LAT_LNG));

        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {
                    // Extract latitude and longitude from the response
                    LatLng latLng = response.getPlace().getLatLng();
                    if (latLng != null) {
                        // Update the location values (latitude, longitude)
                         latitude = latLng.latitude;
                         longitude = latLng.longitude;

                        // Optionally, you can update a UI element to show the selected location
                        Log.e("New Location", "Latitude: " + latitude + ", Longitude: " + longitude);

                        // Update the list of places (if needed)
                        updatePlaceData();
                    }
                })
                .addOnFailureListener(exception -> {
                    Log.e("PlaceDetailsError", "Error fetching place details: " + exception.getMessage());
                    Toast.makeText(getContext(), "Error fetching place details", Toast.LENGTH_SHORT).show();
                });
    }
    private void collapseRecyclerViews() {
        // Collapse all RecyclerViews by setting them to GONE
        diningRecyclerView.setVisibility(View.GONE);
        localEventsRecyclerView.setVisibility(View.GONE);
        shoppingRecyclerView.setVisibility(View.GONE);
    }
    private void updatePlaceData() {
        // Clear existing data
        diningList.clear();
        localEventsList.clear();
        shoppingList.clear();

    }




    private void fetchPlacePredictions(String query) {
        // Build the autocomplete request
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setTypesFilter(Arrays.asList("locality"))
                .setQuery(query)
                .build();

        // Make the request to fetch predictions
        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {
                    List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                    // Update adapter with the new list of predictions
                    suggestionAdapter.updateData(predictions);
                    searchRecyclerView.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(exception -> {
                    Log.e("PlaceError", "Errorrrrrr fetching place predictions: " + exception.getMessage());
                });



    }
    private void showLoadingIndicator(RecyclerView recyclerView, ProgressBar progressBar) {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE); // Show the loading indicator
    }

    private void hideLoadingIndicator(RecyclerView recyclerView, ProgressBar progressBar) {
        recyclerView.setVisibility(View.VISIBLE); // Show the RecyclerView
        progressBar.setVisibility(View.GONE);    // Hide the loading indicator
    }


}
