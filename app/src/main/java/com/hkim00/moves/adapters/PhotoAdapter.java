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

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;
    private List<String> photoReferences;

    public PhotoAdapter(Context context, List<String> photoReferences) {
        this.context = context;
        this.photoReferences = photoReferences;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, int position) {
        holder.bind(photoReferences.get(position));
    }

    @Override
    public int getItemCount() {
        return photoReferences.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPhoto = itemView.findViewById(R.id.ivPhoto);
        }

        public void bind(String photoReference) {
            String maxWidth = "100";
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + maxWidth + "&photoreference=" + photoReference + "&key=" + context.getString(R.string.api_key);

            Glide.with(context)
                    .load(photoUrl)
                    .into(ivPhoto);
        }
    }
}
