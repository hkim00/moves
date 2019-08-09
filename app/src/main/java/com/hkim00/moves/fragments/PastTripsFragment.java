package com.hkim00.moves.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.OrientationHelper;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    ArrayList<Trip> upcomingTrips;
    private static ComponentContext componentContext;
    private static RecyclerBinder recyclerBinder;
    private static Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_past_trips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getNextTrip();

        SetUpRecycler();
    }

    private View SetUpRecycler() {
        upcomingTrips = new ArrayList<>();


        componentContext = new ComponentContext(getContext());

        recyclerBinder =  new RecyclerBinder.Builder()
                .layoutInfo(new LinearLayoutInfo(getContext(), OrientationHelper.VERTICAL, false))
                .build(componentContext);

        Recycler recycler = Recycler.create(componentContext)
                .binder(recyclerBinder)
                .paddingDip(YogaEdge.LEFT, 10)
                .paddingDip(YogaEdge.RIGHT, 10)
                .build();

        final Component component = Column.create(componentContext)
                .child(Text.create(componentContext)
                        .text("Upcoming Trips")
                        .textStyle(3) //bold italic
                        .textSizeDip(24)
                        .textAlignment(Layout.Alignment.ALIGN_CENTER)
                        .paddingDip(YogaEdge.BOTTOM, 10)
                        .paddingDip(YogaEdge.TOP, 15))
                .child(recycler)
                .build();


        return LithoView.create(componentContext, component);
    }



    private void getNextTrip() {
        ParseQuery<ParseObject> tripQuery = ParseQuery.getQuery("Trip");
        tripQuery.whereEqualTo("owner", ParseUser.getCurrentUser());
        tripQuery.orderByDescending("createdAt");
        tripQuery.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> objects, ParseException e) {
               List<Trip> trips = new ArrayList<>();

               if (objects.size() == 0) {
                   Trip trip = Trip.fromParseObject(objects.get);
                   Intent intent = new Intent(getContext(), TripActivity.class);
                   intent.putExtra("trip", Parcels.wrap(trip));
                   startActivity(intent);
                   getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
               }else {
                    for (int i = 0; i <objects.size(); i++) {
                       Trip trip = Trip.fromParseObject(objects.get(i));
                       trips.add(trip);

                   }
                    upcomingTrips.addAll(trips);
               }

            }

        });
    }

    private void addContents(List<Trip> trips) {
        for (int i = 0; i < trips.size(); i++) {
            Component component = Trip.create(componentContext).trip(trips.get(i)).build();
            recyclerBinder.appendItem(component);
        }
    }
}
