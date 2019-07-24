package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

//import com.hkim00.moves.adapters.EventAdapter;
import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.adapters.ProfileAdapter;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MovesActivity extends AppCompatActivity {


    MoveAdapter moveAdapter;
   // ProfileAdapter adapterRestaurants;
    //EventAdapter adapterEvents;
    List<Move> moves;
    //List<Restaurant> restaurants;
    //List<Event> events;
    RecyclerView rvMoves;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moves);

        rvMoves = findViewById(R.id.rvMoves);

       // restaurants = new ArrayList<>();
       // events = new ArrayList<>();

        moveAdapter = new MoveAdapter(getApplicationContext(), moves);
        //moveAdapter = new EventAdapter(getApplicationContext(), events);

        rvMoves.setLayoutManager(new LinearLayoutManager(this));
        rvMoves.setAdapter(moveAdapter);

        moves.addAll((List<Restaurant>) Parcels.unwrap(getIntent().getParcelableExtra("movesRestaurants")));
        moveAdapter.notifyDataSetChanged();

       //rvMoves.setAdapter(moveAdapter);
        moves.addAll((List<Event>) Parcels.unwrap(getIntent().getParcelableExtra("movesEvents")));
        moveAdapter.notifyDataSetChanged();
    }
}
