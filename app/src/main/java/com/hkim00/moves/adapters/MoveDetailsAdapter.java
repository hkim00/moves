package com.hkim00.moves.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;

import java.util.List;

public class MoveDetailsAdapter extends RecyclerView.Adapter<MoveDetailsAdapter.ViewHolder> {
    private Context context;
    private List<Move> moves;


    public MoveDetailsAdapter(Context context, List<Move> moves) {
        this.context = context;
        this.moves = moves;
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
        holder.bind(moves.get(0));
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
        private RatingBar ratingBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCuisine = itemView.findViewById(R.id.tvCuisine);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }


        public void bind(Move move) {
            tvName.setText(move.name);

            if (move.lat != null && move.lng != null) {
                tvDistance.setText(move.distanceFromLocation(context) + " mi");

                if (move.moveType.equals("food")) {
                    setFoodView(move);
                }
            }
        }

        private void setFoodView(Move move) {
            tvCuisine.setText(move.cuisine);

            Restaurant restaurant = (Restaurant) move;


            String price = "";
            if (restaurant.price_level < 0) {
                price = "N/A";
            } else {
                for (int i = 0; i < restaurant.price_level; i++) {
                    price += '$';
                }
            }
            tvPrice.setText(price);

            if (restaurant.rating < 0) {
                ratingBar.setVisibility(View.INVISIBLE);
            } else {
                float moveRate = ((Restaurant) move).rating.floatValue();
                ratingBar.setRating(moveRate = moveRate > 0 ? moveRate / 2.0f : moveRate);
            }
        }

    }
}
