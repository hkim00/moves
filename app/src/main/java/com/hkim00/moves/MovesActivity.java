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


    MoveAdapter adapter;
    List<Move> moves;
    RecyclerView rvMoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moves);

        rvMoves = findViewById(R.id.rvMoves);

        moves = new ArrayList<>();

        adapter = new MoveAdapter(getApplicationContext(), moves);
        rvMoves.setLayoutManager(new LinearLayoutManager(this));
        rvMoves.setAdapter(adapter);

        moves.addAll(Parcels.unwrap(getIntent().getParcelableExtra("moves")));
        adapter.notifyDataSetChanged();
    }
}
