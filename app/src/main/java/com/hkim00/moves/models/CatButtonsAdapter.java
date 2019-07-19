package com.hkim00.moves.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
<<<<<<< HEAD
import android.content.Intent;
=======
>>>>>>> fa906d6926bfc110d5116ed10261cc63fd3473ea
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

<<<<<<< HEAD
import com.hkim00.moves.Intro2Activity;
import com.hkim00.moves.R;

import org.parceler.Parcels;
import org.w3c.dom.Comment;

=======
import com.hkim00.moves.R;
import com.parse.ParseUser;

import java.util.ArrayList;
>>>>>>> fa906d6926bfc110d5116ed10261cc63fd3473ea
import java.util.List;

public class CatButtonsAdapter extends RecyclerView.Adapter<CatButtonsAdapter.ViewHolder>{
    private Context context;
    private List<CategoryButton> catButtons;
<<<<<<< HEAD
=======
    public List<String> foodPrefList;
>>>>>>> fa906d6926bfc110d5116ed10261cc63fd3473ea

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

    public void clear() {
        catButtons.clear();
        notifyDataSetChanged();
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
<<<<<<< HEAD
            if (position != RecyclerView.NO_POSITION) {
                CategoryButton categoryButton = catButtons.get(position);
                Intent intent = new Intent(context, Intro2Activity.class);
                intent.putExtra(Comment.class.getSimpleName(), Parcels.wrap(categoryButton));
                context.startActivity(intent);
=======
            ParseUser currUser = ParseUser.getCurrentUser();
            foodPrefList = new ArrayList<>();
            if (position != RecyclerView.NO_POSITION) {
                CategoryButton categoryButton = catButtons.get(position);
                if (categoryButton.isPref == true) {
                    categoryButton.isPref = false;
                } else {
                    categoryButton.isPref = true;
                }
>>>>>>> fa906d6926bfc110d5116ed10261cc63fd3473ea
            }
        }

        public void bind(CategoryButton categoryButton) {
<<<<<<< HEAD
            // TODO: call cuisines from cuisine array
            // TODO: image database? Looks like Google Places uses reviewer images as official listing photo
            tvCuisine.setText("cuisine");
            // ivCuisine.setImageResource();
        }
    }

=======
            // TODO: image database? Looks like Google Places uses reviewer images as official listing photo
            tvCuisine.setText(categoryButton.cuisine);
        }
    }
>>>>>>> fa906d6926bfc110d5116ed10261cc63fd3473ea
}
