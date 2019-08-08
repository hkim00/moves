package com.hkim00.moves.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.MoveDetailsActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.TripActivity;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;

import org.parceler.Parcels;

import java.util.List;

public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.ViewHolder>{

    private final Context context;
    private final List<Move> moves;
    private boolean isTrip;

    public MoveAdapter(Context context, List<Move> moves) {
        this.context = context;
        this.moves = moves;

        this.isTrip = (context instanceof TripActivity) ? true : false;
    }

    @Override
    public int getItemViewType(int position) {
        if (moves.get(position).moveType == "food") {
            return 1;
        } else if (moves.get(position).moveType == "event") {
            return 2;
        } else return 3;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_move, parent, false);
        view.setElevation(5);
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
        private ImageView ivMoveImage;
        private ConstraintLayout clMove;
        private TextView tvDetail1;
        private TextView tvDetail2;
        public Move move;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clMove = itemView.findViewById(R.id.clMove);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivMoveImage = itemView.findViewById(R.id.ivMoveImage);
            tvDetail1 = itemView.findViewById(R.id.tvDetail1);
            tvDetail2 = itemView.findViewById(R.id.tvDetail2);
            itemView.setOnClickListener(this);
        }

        public void bind(Move move) {
            tvTitle.setText(move.name);
            tvDetail1.setText(move.distanceFromLocation(context) + "mi   •");
            if (move.moveType.equals("food")) {

                String price = "";
                if (move.price_level < 0) {
                    price = "Unknown";
                } else {
                    for (int i = 0; i < move.price_level; i++) {
                        price += '$';
                    }
                }
                tvDetail2.setText(price);
            } else {
                tvDetail2.setText(move.genre);

            }
        }

        public void setFoodView(Move move){
            Restaurant restaurant = (Restaurant) move;
            tvDetail2.setText(restaurant.price_level);
        }

        public void setEventView(Move move){
            Event event = (Event) move;
            tvDetail2.setText(event.genre);
        }

        @Override
        public void onClick(View v) {
            goToMoveDetails();
        }

        private void goToMoveDetails() {
            Intent intent = new Intent(context, MoveDetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("move", Parcels.wrap(move));
            intent.putExtra("isTrip", isTrip);
            context.startActivity(intent);

            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
    }
}