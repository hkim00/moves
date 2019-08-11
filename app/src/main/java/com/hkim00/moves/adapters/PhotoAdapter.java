package com.hkim00.moves.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hkim00.moves.R;
import com.hkim00.moves.models.MovePhoto;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;
    private List<MovePhoto> movePhotos;

    public PhotoAdapter(Context context, List<MovePhoto> movePhotos) {
        this.context = context;
        this.movePhotos = movePhotos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, int position) {
        holder.bind(movePhotos.get(position));
    }

    @Override
    public int getItemCount() {
        return movePhotos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPhoto = itemView.findViewById(R.id.ivPhoto);
        }

        public void bind(MovePhoto movePhoto) {
             Glide.with(context)
                    .load(movePhoto.getPhotoURL(context))
                    .into(ivPhoto);
        }
    }
}
