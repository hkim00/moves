package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hkim00.moves.adapters.ProfileAdapter;
import com.hkim00.moves.models.Restaurant;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MovesActivity extends AppCompatActivity {


    ProfileAdapter adapter;
    List<Restaurant> restaurants;
    RecyclerView rvMoves;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moves);

        rvMoves = findViewById(R.id.rvMoves);

        restaurants = new ArrayList<>();

        adapter = new ProfileAdapter(getApplicationContext(), restaurants);

        rvMoves.setLayoutManager(new LinearLayoutManager(this));
        rvMoves.setAdapter(adapter);

        restaurants.addAll((List<Restaurant>) Parcels.unwrap(getIntent().getParcelableExtra("moves")));
        adapter.notifyDataSetChanged();
    }
}
