package com.hkim00.moves.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hkim00.moves.R;
import com.hkim00.moves.models.Restaurant;
import com.parse.ParseFile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context context;
    private List<Restaurant> restaurants;

    public ProfileAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_move, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.restaurant = restaurant;

        holder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivMoveImage;

        private Restaurant restaurant;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMoveImage = itemView.findViewById(R.id.ivMoveImage);

            ivMoveImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //goToMovetDetails();
                }
            });
        }


        public void bind(Restaurant restaurant) {
            //TODO get image from API
           // ParseFile moveImage = restaurant.getParseFile("image");
            //   Glide.with(context).load(moveImage.getUrl()).into(ivMoveImage);
            }
        }

        private void goToMoveDetails() {
            //TODO create activity with move details
            Intent intent = new Intent(context, MoveDetailsActivity.class);

           // intent.putExtra("postId", restaurant.getObjectId()); --> not sure if line is necessary yet

            context.startActivity(intent);
        }
    }
}