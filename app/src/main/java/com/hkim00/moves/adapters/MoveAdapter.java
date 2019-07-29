package com.hkim00.moves.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.MoveDetailsActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;

import org.parceler.Parcels;

import java.util.List;

public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.ViewHolder>{

    private final Context context;
    private final List<Move> moves;

    public MoveAdapter(Context context, List<Move> moves) {
        this.context = context;
        this.moves = moves;
    }

    @Override
    public int getItemViewType(int position) {
        return moves.get(position).getMoveType();
    }

    @NonNull
    @Override
    public MoveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Move.RESTAURANT:
                view = LayoutInflater
                        .from(context)
                        .inflate(R.layout.item_move, parent, false);
                return new restaurantViewHolder(view);
            case Move.EVENT:
                view = LayoutInflater
                        .from(context)
                        .inflate(R.layout.item_move, parent, false);
                return new eventViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MoveAdapter.ViewHolder holder, int position) {
        Move move = moves.get(position);
        holder.bindType(move);
        holder.move = move;
    }

    @Override
    public int getItemCount() {
        return moves.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        public Move move;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(view -> goToMoveDetails(move.getMoveType()));
        }

        public abstract void bindType(Move move);

        private void goToMoveDetails(int moveType) {
            Intent intent = new Intent(context, MoveDetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (moveType == 1) {
                intent.putExtra("move", Parcels.wrap(move));
            }
            if (moveType == 2) {
                intent.putExtra("move", Parcels.wrap(move));
            }
            context.startActivity(intent);
        }
    }

    public class restaurantViewHolder extends ViewHolder {
        private final TextView tvTitle;
        public Restaurant restaurant;

        public restaurantViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        public void bindType(Move move){
            tvTitle.setText(((Restaurant) move).name);
        }
    }

    public class eventViewHolder extends ViewHolder {
        private final TextView tvTitle;

        public eventViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        public void bindType(Move move){
            tvTitle.setText(((Event) move).name);
        }
    }
}
