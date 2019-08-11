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

import com.bumptech.glide.Glide;
import com.hkim00.moves.HomeActivity;
import com.hkim00.moves.MoveDetailsActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.TripActivity;
import com.hkim00.moves.models.Move;

import org.parceler.Parcels;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final Context context;
    private final List<Move> moves;

    public HistoryAdapter(Context context, List<Move> moves) {
        this.context = context;
        this.moves = moves;
    }

    @Override
    public int getItemViewType(int position) {
        if (moves.get(position).moveType.equals("food")) {
            return 1;
        } else if (moves.get(position).moveType.equals("event")) {
            return 2;
        } else return 3;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_move, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
            ivMoveImage = itemView.findViewById(R.id.ivMoveImg);
            tvDetail1 = itemView.findViewById(R.id.tvDetail1);
            tvDetail2 = itemView.findViewById(R.id.tvDetail2);
            itemView.setOnClickListener(this);
        }

        public void bind(Move move) {
            tvTitle.setText(move.name);
            tvDetail1.setText(move.distanceFromLocation(context) + "mi  •");
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

            if (move.photo != null && move.moveType.equals("food")) {
                String maxWidth = "100";
                String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + maxWidth + "&photoreference=" + move.photo + "&key=" + context.getString(R.string.api_key);

                Glide.with(context)
                        .load(photoUrl)
                        .into(ivMoveImage);
            } else if (move.photo != null) {
                Glide.with(context)
                        .load(move.photo)
                        .into(ivMoveImage);
            } else {
            ivMoveImage.setImageResource(R.drawable.placeholder);
            }
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

            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
    }
}
