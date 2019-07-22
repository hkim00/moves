package com.hkim00.moves;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.adapters.EventAdapter;
import com.hkim00.moves.adapters.ProfileAdapter;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Restaurant;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    EventAdapter adapterEvents;
    List<Event> events;
    RecyclerView rvMoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moves);

        rvMoves = findViewById(R.id.rvMoves);

        events = new ArrayList<>();

        adapterEvents = new EventAdapter(getApplicationContext(), events);
        rvMoves.setLayoutManager(new LinearLayoutManager(this));
        rvMoves.setAdapter(adapterEvents);
        events.addAll((List<Event>) Parcels.unwrap(getIntent().getParcelableExtra("movesEvents")));
        adapterEvents.notifyDataSetChanged();
    }
}
