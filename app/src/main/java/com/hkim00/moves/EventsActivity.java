package com.hkim00.moves;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.models.Move;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    MoveAdapter adapterEvents;
    List<Move> events;
    RecyclerView rvMoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moves);

        rvMoves = findViewById(R.id.rvMoves);

        events = new ArrayList<>();

        adapterEvents = new MoveAdapter(getApplicationContext(), events);
        rvMoves.setLayoutManager(new LinearLayoutManager(this));
        rvMoves.setAdapter(adapterEvents);

        events.addAll(Parcels.unwrap(getIntent().getParcelableExtra("movesEvents")));
        adapterEvents.notifyDataSetChanged();
    }
}
