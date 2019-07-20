package com.hkim00.moves;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private static final String TAG = "LocationActivity";
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    PlacesClient placesClient;

    TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        tvLocation = findViewById(R.id.tvLocation);

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        placesClient = Places.createClient(this);


        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            placesClient.findCurrentPlace(request).addOnSuccessListener(findCurrentPlaceResponse -> {

                if (findCurrentPlaceResponse.getPlaceLikelihoods().size() > 0) {
                    PlaceLikelihood highestLikely = findCurrentPlaceResponse.getPlaceLikelihoods().get(0);

                    for (PlaceLikelihood placeLikelihood : findCurrentPlaceResponse.getPlaceLikelihoods()) {
                        PlaceLikelihood currentPlace = placeLikelihood;
                        if (currentPlace.getLikelihood() > highestLikely.getLikelihood()) {
                            highestLikely = currentPlace;
                        }

                        Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                        tvLocation.append(String.format("Place '%s' has likelihood: %f\n",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                    }

                    tvLocation.append(String.format("\nHighest likely is: %s with %s", highestLikely.getPlace().getName(), highestLikely.getLikelihood()));
                }
            }).addOnFailureListener(e -> {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    e.printStackTrace();
                }
            });
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            getLocationPermission();
        }
    }


    private void getLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            Toast.makeText(this, "Ask for permission", Toast.LENGTH_LONG).show();


        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Toast.makeText(this, "requesting now!", Toast.LENGTH_LONG).show();
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager .PERMISSION_GRANTED) {
                    Toast.makeText(this, "got permissions!", Toast.LENGTH_LONG).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(this, "got denied :(", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
