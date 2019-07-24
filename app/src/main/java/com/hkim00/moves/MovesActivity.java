package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MovesActivity extends AppCompatActivity {


    MoveAdapter adapterRestaurants;
    MoveAdapter adapterEvents;
    List<Move> restaurants, events;

    RecyclerView rvMoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moves);

        rvMoves = findViewById(R.id.rvMoves);

        restaurants = new ArrayList<>();
        events = new ArrayList<>();

        adapterRestaurants = new MoveAdapter(getApplicationContext(), restaurants);
        adapterEvents = new MoveAdapter(getApplicationContext(), events);
        rvMoves.setLayoutManager(new LinearLayoutManager(this));
//       rvMoves.setAdapter(adapterRestaurants);
//

        restaurants.addAll((List<Move>) Parcels.unwrap(getIntent().getParcelableExtra("moveRestaurant")));
//        adapterRestaurants.notifyDataSetChanged();

        rvMoves.setAdapter(adapterEvents);
        events.addAll((List<Move>) Parcels.unwrap(getIntent().getParcelableExtra("moveEvent")));

        //restaurants.addAll((List<Restaurant>) Parcels.unwrap(getIntent().getParcelableExtra("movesRestaurants")));
//        adapterRestaurants.notifyDataSetChanged();

        rvMoves.setAdapter(adapterEvents);
        //events.addAll((List<Event>) Parcels.unwrap(getIntent().getParcelableExtra("movesEvents")));

        //adapterEvents.notifyDataSetChanged();

    }
}
