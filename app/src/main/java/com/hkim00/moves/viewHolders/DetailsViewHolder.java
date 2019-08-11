package com.hkim00.moves.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;

public class DetailsViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    private TextView tvName, tvDistance, tvPrice, tvCuisine, tvTime;
    private RatingBar ratingBar;
    private ImageView ivTime;


    public DetailsViewHolder(@NonNull View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tvName);
        tvDistance = itemView.findViewById(R.id.tvDistance);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvCuisine = itemView.findViewById(R.id.tvCuisine);
        tvTime = itemView.findViewById(R.id.tvTime);
        ratingBar = itemView.findViewById(R.id.ratingBar);
        ivTime = itemView.findViewById(R.id.ivTime);
    }


    public void bind(Context context, Move move) {
        this.context = context;

        tvName.setText(move.name);
        tvCuisine.setText(move.subCategory);

        ratingBar.setVisibility((move.moveType.equals("food")) ? View.VISIBLE : View.INVISIBLE);
        ivTime.setVisibility((move.moveType.equals("food")) ? View.GONE : View.VISIBLE);
        tvTime.setVisibility((move.moveType.equals("food")) ? View.GONE : View.VISIBLE);

        if (move.didCheckHTTPDetails) {
            tvDistance.setText(move.distanceFromLocation(context) + " mi");

            if (move.moveType.equals("food")) {
                setFoodView(move);
            } else {
                setEventView(move);
            }
        }
    }

    private void setFoodView(Move move) {
        Restaurant restaurant = (Restaurant) move;

        if (restaurant.price_level < 0) {
            tvPrice.setText("N/A");
        } else {
            String price = "";
            for (int i = 0; i < restaurant.price_level; i++) {
                price += '$';
            }
            tvPrice.setText(price);
        }

        if (restaurant.rating < 0) {
            ratingBar.setVisibility(View.INVISIBLE);
        } else {
            float moveRate = ((Restaurant) move).rating.floatValue();
            ratingBar.setRating(moveRate > 0 ? moveRate / 2.0f : moveRate);
        }
    }


    private void setEventView(Move move) {
        Event event = (Event) move;

        tvPrice.setText(event.priceRange);
        tvTime.setText(event.startDate + "  •  " + event.startTime);
        tvCuisine.setText(event.genre);
    }

}
