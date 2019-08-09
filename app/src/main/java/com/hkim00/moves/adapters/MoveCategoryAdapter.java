package com.hkim00.moves.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.models.Cuisine;
import com.hkim00.moves.models.MoveCategory;
import com.hkim00.moves.viewHolders.MoveCategoryViewHolder;

import java.util.List;

public class MoveCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MoveCategory> moveCategories;

    public MoveCategoryAdapter(Context context, List<MoveCategory> moveCategories) {
        this.context = context;
        this.moveCategories = moveCategories;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_preference, parent, false);
        return new MoveCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MoveCategoryViewHolder viewHolder = (MoveCategoryViewHolder) holder;
        viewHolder.bind(context, moveCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return moveCategories.size();
    }
}
