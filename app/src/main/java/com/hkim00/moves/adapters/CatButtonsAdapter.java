package com.hkim00.moves.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkim00.moves.R;
import com.hkim00.moves.models.CategoryButton;
import com.hkim00.moves.util.MoveCategoriesHelper;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CatButtonsAdapter extends RecyclerView.Adapter<CatButtonsAdapter.ViewHolder>{
    private Context context;
    private List<CategoryButton> catButtons;

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
            if (position != RecyclerView.NO_POSITION) {
                CategoryButton categoryButton = catButtons.get(position);
                categoryButton.isPref = !categoryButton.isPref;
                v.setBackgroundColor(v.getResources().getColor((categoryButton.isPref ? R.color.quantum_vanillagreen900: R.color.white)));
                tvCuisine.setTextColor(v.getResources().getColor((categoryButton.isPref ? R.color.white : R.color.black)));
            }
        }

        public void bind(CategoryButton categoryButton) {
            itemView.setBackgroundColor(context.getResources().getColor((categoryButton.isPref ? R.color.quantum_vanillagreen900 : R.color.white)));
            tvCuisine.setTextColor(context.getResources().getColor((categoryButton.isPref ? R.color.white : R.color.black)));
            tvCuisine.setText(categoryButton.type);
            getCategoryImage();
        }

        private void getCategoryImage() {
            if (tvCuisine.getText() == "Mexican") {
                ivCuisine.setImageResource(R.drawable.mexican);
            }
            if (tvCuisine.getText() == "Italian") {
                ivCuisine.setImageResource(R.drawable.italian);
            }
            if (tvCuisine.getText() == "Thai") {
                ivCuisine.setImageResource(R.drawable.thai);
            }
            if (tvCuisine.getText() == "Chinese") {
                ivCuisine.setImageResource(R.drawable.chinese);
            }
            if (tvCuisine.getText() == "Vegan") {
                ivCuisine.setImageResource(R.drawable.vegan);
            }
            if (tvCuisine.getText() == "Indian") {
                ivCuisine.setImageResource(R.drawable.indian);
            }
            if (tvCuisine.getText() == "American") {
                ivCuisine.setImageResource(R.drawable.american);
            }
            if (tvCuisine.getText() == "Greek") {
                ivCuisine.setImageResource(R.drawable.greek);
            }
            if (tvCuisine.getText() == "French") {
                ivCuisine.setImageResource(R.drawable.french);
            }
            if (tvCuisine.getText() == "Japanese") {
                ivCuisine.setImageResource(R.drawable.japanese);
            }
            if (tvCuisine.getText() == "African") {
                ivCuisine.setImageResource(R.drawable.african);
            }
            if (tvCuisine.getText() == "Halal") {
                ivCuisine.setImageResource(R.drawable.halal);
            }
            if (tvCuisine.getText() == "German") {
                ivCuisine.setImageResource(R.drawable.german);
            }
            if (tvCuisine.getText() == "Korean") {
                ivCuisine.setImageResource(R.drawable.korean);
            }
            if (tvCuisine.getText() == "Lebanese") {
                ivCuisine.setImageResource(R.drawable.lebanese);
            }
            if (tvCuisine.getText() == "Ethiopian") {
                ivCuisine.setImageResource(R.drawable.ethiopian);
            }
            if (tvCuisine.getText() == "Turkish") {
                ivCuisine.setImageResource(R.drawable.turkish);
            }
            if (tvCuisine.getText() == "Caribbean") {
                ivCuisine.setImageResource(R.drawable.caribbean);
            }
            if (tvCuisine.getText() == "Music") {
                ivCuisine.setImageResource(R.drawable.music);
            }
            if (tvCuisine.getText() == "Sports") {
                ivCuisine.setImageResource(R.drawable.sports);
            }
            if (tvCuisine.getText() == "Dance") {
                ivCuisine.setImageResource(R.drawable.dance);
            }
            if (tvCuisine.getText() == "Broadway") {
                ivCuisine.setImageResource(R.drawable.broadway);
            }
            if (tvCuisine.getText() == "Theater") {
                ivCuisine.setImageResource(R.drawable.theater);
            }
            if (tvCuisine.getText() == "Opera") {
                ivCuisine.setImageResource(R.drawable.opera);
            }
        }

    }
}