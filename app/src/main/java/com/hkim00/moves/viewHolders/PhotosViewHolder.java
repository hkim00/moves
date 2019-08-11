package com.hkim00.moves.viewHolders;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.adapters.PhotoAdapter;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.MovePhoto;
import com.hkim00.moves.models.Restaurant;

import java.util.List;

public class PhotosViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private PhotoAdapter adapter;
    private RecyclerView rvPhotos;

    public PhotosViewHolder(@NonNull View itemView) {
        super(itemView);

        rvPhotos = itemView.findViewById(R.id.rvPhotos);
    }

    private void setupRecyclerView(List<MovePhoto> photoReferences) {
        rvPhotos.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adapter = new PhotoAdapter(context, photoReferences);
        rvPhotos.setAdapter(adapter);
    }

    public void bind(Context context, Move move) {
        this.context = context;

        if (move.lat != null) {
            Restaurant restaurant = (Restaurant) move;

            if (restaurant.movePhotos.size() > 0) {
                setupRecyclerView(restaurant.movePhotos);
            }
        }
    }
}
