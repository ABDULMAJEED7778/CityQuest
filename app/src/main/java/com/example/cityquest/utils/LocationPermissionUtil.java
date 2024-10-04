package com.example.cityquest.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationPermissionUtil {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    public interface LocationPermissionCallback {
        void onPermissionGranted();
        void onPermissionDenied();
    }

    public static void requestLocationPermission(Activity activity, LocationPermissionCallback callback) {
        // Check if the permission is already granted
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionGranted(); // Permission already granted
        } else {
            // Request the permission
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public static void handleRequestPermissionsResult(int requestCode, int[] grantResults, LocationPermissionCallback callback) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callback.onPermissionGranted(); // Permission granted
            } else {
                callback.onPermissionDenied(); // Permission denied
            }
        }
    }
}

