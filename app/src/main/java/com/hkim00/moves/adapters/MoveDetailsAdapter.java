package com.hkim00.moves.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.models.Move;
import com.parse.ParseUser;

import java.util.List;

public class MoveDetailsAdapter extends RecyclerView.Adapter<MoveDetailsAdapter.ViewHolder> {
    private Context context;
    private Move move;


    public MoveDetailsAdapter(Context context, Move move) {
        this.context = context;
        this.move = move;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_move_details, parent, false);
        return new MoveDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(move);
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvDistance;
        private TextView tvPrice;
        private TextView tvCuisine;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCuisine = itemView.findViewById(R.id.tvCuisine);

        }


        public void bind(Move move) {
            tvName.setText(move.name);
            tvDistance.setText(move.distanceFromLocation(context) + " mi");
            tvCuisine.setText(move.cuisine);
        }

    }
}
