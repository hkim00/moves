package com.hkim00.moves.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.models.Cuisine;
import com.hkim00.moves.viewHolders.ChooseMoveViewHolder;
import com.hkim00.moves.viewHolders.MovesViewHolder;

import java.util.List;

public class MoveResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Cuisine> moveResults;

    public MoveResultAdapter(Context context, List<Cuisine> moveResults) {
        this.context = context;
        this.moveResults = moveResults;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_preference, parent, false);
        return new MovesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MovesViewHolder viewHolder = (MovesViewHolder) holder;
        viewHolder.bind(context, moveResults.get(position));
    }

    @Override
    public int getItemCount() {
        return moveResults.size();
    }
}
