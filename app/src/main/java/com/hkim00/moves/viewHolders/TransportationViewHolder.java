package com.hkim00.moves.viewHolders;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hkim00.moves.R;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.UserLocation;
import com.lyft.deeplink.RideTypeEnum;
import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.networking.ApiConfig;


public class TransportationViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
    private Context context;
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private LyftButton lyftButton;
    private Move mapMove;


    public TransportationViewHolder(@NonNull View itemView) {
        super(itemView);

        lyftButton = itemView.findViewById(R.id.lyft_button);
    }

    public void bind(Context context, Move move) {
        this.context = context;
        mapMove = move;

        if (move.lat != null && move.lng != null) {
            addMapFragment();
            setupLyftButton();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setMapToolbarEnabled(true);

        LatLng moveLatLng = new LatLng(mapMove.lat, mapMove.lng);
        map.addMarker(new MarkerOptions().position(moveLatLng).title(mapMove.name));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(moveLatLng, 15)); // second argument controls how zoomed in map is
    }

    private void addMapFragment() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commit();
    }


    private void setupLyftButton() {
        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId(context.getString(R.string.client_id_lyft))
                .setClientToken("...")
                .build();
        lyftButton.setApiConfig(apiConfig);
        UserLocation currLocation = UserLocation.getCurrentLocation(context);

        if (currLocation.lat == null || currLocation.lng == null) {
            return;
        }

        RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                .setPickupLocation(Double.valueOf(currLocation.lat), Double.valueOf(currLocation.lng))
                .setDropoffLocation(mapMove.lat, mapMove.lng);
        rideParamsBuilder.setRideTypeEnum(RideTypeEnum.STANDARD);

        lyftButton.setRideParams(rideParamsBuilder.build());
        lyftButton.load();
    }
}
