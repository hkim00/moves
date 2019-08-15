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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hkim00.moves.HomeActivity;
import com.hkim00.moves.MoveDetailsActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.TripActivity;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.MovePhoto;

import org.parceler.Parcels;

import java.util.List;

public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.ViewHolder>{

    private final Context context;
    private final List<Move> moves;

    private boolean isTrip;
    private boolean isHome;

    public MoveAdapter(Context context, List<Move> moves) {
        this.context = context;
        this.moves = moves;

        this.isTrip = context instanceof TripActivity;
        this.isHome = context instanceof HomeActivity;
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

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate((isHome) ? R.layout.item_home_move : R.layout.item_move, parent, false);
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
        private TextView tvDetail1;
        public Move move;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivMoveImage = itemView.findViewById(R.id.ivMoveImg);
            tvDetail1 = itemView.findViewById(R.id.tvDetail1);
            itemView.setOnClickListener(this);
        }

        public void bind(Move move) {
            tvTitle.setText(move.name);

            String distanceFromMove = move.distanceFromLocation(context);
            tvDetail1.setText(distanceFromMove.equals("") ? "" : distanceFromMove + "mi");

            if (move.moveType.equals("food")) {
                String price = "";
                if (move.price_level > 0) {
                    for (int i = 0; i < move.price_level; i++) {
                        price += '$';
                    }
                }

                tvDetail1.append(price.equals("") ? "" : "  •  " + price);

                if (move.movePhotos != null) {
                    if (move.movePhotos.size() > 0) {
                        MovePhoto movePhoto = move.movePhotos.get(0);

                        Glide.with(context)
                                .load(movePhoto.getPhotoURL(context))
                                .into(ivMoveImage);
                    } else {
                        ivMoveImage.setImageResource(R.drawable.placeholder);
                    }
                } else {
                    if (move.photo != null) {
                        Glide.with(context)
                                .load(move.photo)
                                .into(ivMoveImage);
                    } else {
                        ivMoveImage.setImageResource(R.drawable.placeholder);
                    }
                }
            } else {
                if (move.genre != null) {
                    tvDetail1.append((move.genre.equals("")) ? "" : "  •  " + move.genre);
                }

                if (move.photo != null) {
                    Glide.with(context)
                            .load(move.photo)
                            .into(ivMoveImage);
                }  else {
                    ivMoveImage.setImageResource(R.drawable.placeholder);
                }
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
            intent.putExtra("isTrip", isTrip);
            context.startActivity(intent);

            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
    }
}