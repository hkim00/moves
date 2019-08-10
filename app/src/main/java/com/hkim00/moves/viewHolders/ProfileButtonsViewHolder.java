package com.hkim00.moves.viewHolders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hkim00.moves.R;
import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.fragments.ProfileFragment;
import com.hkim00.moves.models.Move;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProfileButtonsViewHolder extends RecyclerView.ViewHolder {
    public final static String TAG = "ProfileButtonsViewHolder";
    private Button btnSaved, btnFavorites;
    private ImageView ivSaved, ivFavorites;

    private Context context;

    private boolean didCheckSave = false;

    private boolean isFavView = true;

    public ProfileButtonsViewHolder(@NonNull View itemView) {
        super(itemView);

        ivSaved = itemView.findViewById(R.id.ivSaved);
        ivFavorites = itemView.findViewById(R.id.ivFavorites);

        btnSaved = itemView.findViewById(R.id.btnSave);
        btnFavorites = itemView.findViewById(R.id.btnFavorite);

        setupButtons();
    }

    public void bind(Context context) {
        this.context = context;
    }

    private void setupButtons() {
        ivFavorites.setImageResource(R.drawable.ufi_heart_active);
        ProfileFragment.toggleRecyclerInfo(true);
        btnFavorites.setOnClickListener(view -> {
            ProfileFragment.toggleRecyclerInfo(true);
            ivFavorites.setImageResource(R.drawable.ufi_heart_active);
            ivSaved.setImageResource(R.drawable.ufi_save);
        });
        btnSaved.setOnClickListener(view -> {
            ProfileFragment.toggleRecyclerInfo(false);
            ivFavorites.setImageResource(R.drawable.ufi_heart);
            ivSaved.setImageResource(R.drawable.ufi_save_active);
        });
    }
}


