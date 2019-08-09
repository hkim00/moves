package com.hkim00.moves.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hkim00.moves.R;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;

public class VenueViewHolder  extends RecyclerView.ViewHolder {
    private TextView tvVenueName;
    private ImageView ivVenuePhoto;

    public VenueViewHolder(@NonNull View itemView) {
        super(itemView);

        tvVenueName = itemView.findViewById(R.id.tvVenueName);
        ivVenuePhoto = itemView.findViewById(R.id.ivVenuePhoto);
    }

    public void bind(Context context, Move move) {
        Event event = (Event) move;

        if (event.venueName != null && event.venuePhotoUrl != null) {
            tvVenueName.setText(event.venueName);

            Glide.with(context)
                    .load(event.venuePhotoUrl)
                    .into(ivVenuePhoto);
        }
    }
}
