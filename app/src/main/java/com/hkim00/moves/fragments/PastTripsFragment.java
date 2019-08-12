package com.hkim00.moves.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.adapters.TripAdapter;
import com.hkim00.moves.models.Trip;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class PastTripsFragment extends Fragment {
    public final static String TAG = "PastTripsFragment";
    public static final int LOCATION_REQUEST_CODE = 20;

    private static ArrayList<Trip> upcomingTrips;
    private RecyclerView rvUpcomingTrips;
    private TripAdapter tripAdapter;
    private TextView tvNoTrips;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_past_trips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvUpcomingTrips = view.findViewById(R.id.rvUpcomingTrips);
        tvNoTrips = view.findViewById(R.id.tvNoTrips);

        SetUpRecycler();
        getNextTrip();
    }

    private void SetUpRecycler() {
        upcomingTrips = new ArrayList<>();

        rvUpcomingTrips.setLayoutManager(new LinearLayoutManager(getContext()));

        tripAdapter = new TripAdapter(getContext(), upcomingTrips);
        rvUpcomingTrips.setAdapter(tripAdapter);
    }

    public void getNextTrip() {
        ParseQuery<ParseObject> tripQuery = ParseQuery.getQuery("Trip");
        tripQuery.whereEqualTo("owner", ParseUser.getCurrentUser());
        tripQuery.orderByDescending("createdAt");
        tripQuery.findInBackground((objects, e) -> {
            if (e == null) {
                List<Trip> trips = new ArrayList<>();

                if (objects.size() == 0) {
                    tvNoTrips.setVisibility(View.VISIBLE);
                } else {
                    tvNoTrips.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < objects.size(); i++) {
                        Trip trip = Trip.fromParseObject(objects.get(i));
                        trips.add(trip);
                    }
                    upcomingTrips.clear();
                    upcomingTrips.addAll(trips);
                    tripAdapter.notifyDataSetChanged();
                }
            } else {
                Log.e(TAG, "Error finding upcoming trips.");
                e.printStackTrace();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == LOCATION_REQUEST_CODE) {
            getNextTrip();
        }
    }
}
