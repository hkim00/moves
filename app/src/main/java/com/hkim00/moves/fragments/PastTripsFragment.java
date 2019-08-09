package com.hkim00.moves.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.widget.LinearLayoutInfo;
import com.facebook.litho.widget.Recycler;
import com.facebook.litho.widget.RecyclerBinder;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaEdge;
import com.hkim00.moves.R;
import com.hkim00.moves.TripActivity;
import com.hkim00.moves.adapters.TripAdapter;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Trip;
import com.hkim00.moves.specs.MoveItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class PastTripsFragment extends Fragment {
    public final static String TAG = "PastTripsFragment";

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



    private void getNextTrip() {
        ParseQuery<ParseObject> tripQuery = ParseQuery.getQuery("Trip");
        tripQuery.whereEqualTo("owner", ParseUser.getCurrentUser());
        tripQuery.orderByDescending("createdAt");
        tripQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
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
                }
                else {
                    Log.e(TAG, "Error finding upcoming trips.");
                    e.printStackTrace();
                }

            }

        });
    }
}