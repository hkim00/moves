package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hkim00.moves.adapters.EventAdapter;
import com.hkim00.moves.adapters.ProfileAdapter;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Restaurant;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {


    ProfileAdapter adapterRestaurants;
    EventAdapter adapterEvents;
    List<Restaurant> restaurants;
    List<Event> events;
    RecyclerView rvMoves;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moves);

        rvMoves = findViewById(R.id.rvMoves);

        restaurants = new ArrayList<>();

        adapterRestaurants = new ProfileAdapter(getApplicationContext(), restaurants);
        rvMoves.setLayoutManager(new LinearLayoutManager(this));
        rvMoves.setAdapter(adapterRestaurants);
        restaurants.addAll((List<Restaurant>) Parcels.unwrap(getIntent().getParcelableExtra("movesRestaurants")));
        adapterRestaurants.notifyDataSetChanged();
    }
}
