package com.hkim00.moves.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkim00.moves.R;
import com.hkim00.moves.models.CategoryButton;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CatButtonsAdapter extends RecyclerView.Adapter<CatButtonsAdapter.ViewHolder>{
    private Context context;
    private List<CategoryButton> catButtons;
    public List<String> foodPrefList;

    public CatButtonsAdapter(Context context, List<CategoryButton> catButtons) {
        this.context = context;
        this.catButtons = catButtons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cat_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryButton categoryButton = catButtons.get(position);
        holder.bind(categoryButton);
    }

    @Override
    public int getItemCount() {
        return catButtons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvCuisine;
        private ImageView ivCuisine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCuisine = itemView.findViewById(R.id.cuisine_tv);
            ivCuisine = itemView.findViewById(R.id.cuisine_iv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            foodPrefList = new ArrayList<>();
            if (position != RecyclerView.NO_POSITION) {
                CategoryButton categoryButton = catButtons.get(position);
                categoryButton.isPref = !categoryButton.isPref;
            }
        }

        public void bind(CategoryButton categoryButton) {
            // TODO: image database? Looks like Google Places uses reviewer images as official listing photo
            tvCuisine.setText(categoryButton.type);
        }
    }
}