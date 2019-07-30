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
import com.hkim00.moves.models.CategoryButton;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;

import org.parceler.Parcels;

import java.util.ArrayList;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_move, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveAdapter.ViewHolder holder, int position) {
        Move move = moves.get(position);
        holder.bind(move);
        holder.move = move;
    }

    @Override
    public int getItemCount() {
        return moves.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;
        public Move move;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        public void bind(Move move) {
            tvTitle.setText(move.getName());
        }

        @Override
        public void onClick(View v) {
            goToMoveDetails();
        }

        private void goToMoveDetails() {
            Intent intent = new Intent(context, MoveDetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("move", Parcels.wrap(move));
            context.startActivity(intent);
        }
    }
}
