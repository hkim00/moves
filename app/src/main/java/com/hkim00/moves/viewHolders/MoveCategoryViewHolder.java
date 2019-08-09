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
import com.hkim00.moves.models.MoveCategory;

import java.util.List;

public class MoveCategoryViewHolder extends RecyclerView.ViewHolder {

    private TextView tvPreference;
    private RecyclerView rvMoves;
    private MoveAdapter adapter;

    private Context context;


    public MoveCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        tvPreference = itemView.findViewById(R.id.tvPreference);
        rvMoves = itemView.findViewById(R.id.rvMoves);
    }

    public void bind(Context context, MoveCategory moveCategory) {
        this.context = context;

        tvPreference.setText(moveCategory.category);

        if (moveCategory != null) {
            setupRecyclerView(moveCategory.moves);
        }
    }

    private void setupRecyclerView(List<Move> moves) {
        rvMoves.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MoveAdapter(context, moves);
        rvMoves.setAdapter(adapter);
    }
}
