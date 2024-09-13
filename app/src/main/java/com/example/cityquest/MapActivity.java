package com.example.cityquest;



import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

//import com.ola.mapsdk.model.OlaLatLng;
//import com.ola.mapsdk.model.OlaMarkerOptions;
//import com.ola.mapsdk.view.OlaMap;
//import com.ola.mapsdk.view.OlaMapView;
//
//import com.ola.mapsdk.interfaces.OlaMapCallback;
//import com.ola.mapsdk.camera.MapControlSettings;

public class MapActivity extends AppCompatActivity {

//    OlaMapView mapView;
//    OlaMarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize the map view
//        mapView = findViewById(R.id.mapView);
//
//
//        // Set up the map using the API key and callback
//        mapView.getMap(String.valueOf(R.string.api_key), new OlaMapCallback() {
//                @Override
//                public void onMapReady(OlaMap olaMap) {
//                    // Map is ready to use
//                }
//
//                @Override
//                public void onMapError(String error) {
//                    // Handle map error
//                }
//            },new MapControlSettings.Builder()
//                .setCompassEnabled(true)       // Enable compass
//                .setDoubleTapGesturesEnabled(true) // Enable double tap gestures
//                .setRotateGesturesEnabled(true) // Enable rotate gestures
//                .setScrollGesturesEnabled(true) // Enable scroll gestures
//                .setTiltGesturesEnabled(true)   // Enable tilt gestures
//                .setZoomGesturesEnabled(true)   // Enable zoom gestures
//                .build()
//        );
//         markerOptions = new OlaMarkerOptions.Builder()
//                 .setMarkerId("marker1")
//                .setPosition(new OlaLatLng(18.52145653681468, 73.93178277572254,18.52145653681468))
//                .setIsIconClickable(true)
//                .setIconRotation(0f)
//                .setIsAnimationEnable(true)
//                .setIsInfoWindowDismissOnClick(true)
//                .build();

    }





}
