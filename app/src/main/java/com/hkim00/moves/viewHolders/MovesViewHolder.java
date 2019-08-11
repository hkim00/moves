package com.hkim00.moves.viewHolders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hkim00.moves.MoveDetailsActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.adapters.ProfileAdapter;
import com.hkim00.moves.models.Move;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MovesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final static String TAG = "MovesViewHolder";
    private Context context;

    private TextView tvTitle;
    private ImageView ivMoveImage;
    private ConstraintLayout clMove;
    private TextView tvDetail1;
    private TextView tvDetail2;
    public Move move;

    public MovesViewHolder (@NonNull View itemView) {
        super(itemView);
        clMove = itemView.findViewById(R.id.clMove);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        ivMoveImage = itemView.findViewById(R.id.ivMoveImg);
        tvDetail1 = itemView.findViewById(R.id.tvDetail1);
        tvDetail2 = itemView.findViewById(R.id.tvDetail2);
        itemView.setOnClickListener(this);
    }

    public void bind(Context context, Move move) {
        this.context = context;
        this.move = move;

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
        if (move.photo != null) {
            String maxWidth = "100";
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + maxWidth + "&photoreference=" + move.photo + "&key=" + context.getString(R.string.api_key);

            Glide.with(context)
                    .load(photoUrl)
                    .into(ivMoveImage);
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