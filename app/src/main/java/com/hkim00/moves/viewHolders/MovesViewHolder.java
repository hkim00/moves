package com.hkim00.moves.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.adapters.PhotoAdapter;
import com.hkim00.moves.models.Cuisine;
import com.hkim00.moves.models.Move;

import java.util.List;

public class MovesViewHolder extends RecyclerView.ViewHolder {

    private TextView tvPreference;
    private RecyclerView rvMoves;
    private MoveAdapter adapter;

    private Context context;
    private List<Move> moves;


    public MovesViewHolder(@NonNull View itemView) {
        super(itemView);

        rvMoves = itemView.findViewById(R.id.rvMoves);
    }

    public void bind(Context context, Cuisine cuisine) {
        this.context = context;

        if (cuisine != null) {
            setupRecyclerView(cuisine.results);
        }
    }

    private void setupRecyclerView(List<Move> moves) {
        rvMoves.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MoveAdapter(context, moves);
        rvMoves.setAdapter(adapter);
    }
}
