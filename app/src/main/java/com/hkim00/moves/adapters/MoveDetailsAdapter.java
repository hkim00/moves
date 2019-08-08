package com.hkim00.moves.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.hkim00.moves.viewHolders.DetailsViewHolder;
import com.hkim00.moves.viewHolders.PhotosViewHolder;
import com.hkim00.moves.viewHolders.TransportationViewHolder;

import java.util.List;

public class MoveDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<Move> moves;


    public MoveDetailsAdapter(Context context, List<Move> moves) {
        this.context = context;
        this.moves = moves;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_move_details, parent, false);
            viewHolder = new DetailsViewHolder(view);
        } else if (viewType == 1) {
            View view = inflater.inflate(R.layout.item_transportation, parent, false);
            viewHolder = new TransportationViewHolder(view);
        } else if (viewType == 2 && moves.get(0).moveType.equals("food")) {
            View view = inflater.inflate(R.layout.item_photos, parent, false);
            viewHolder = new PhotosViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            final DetailsViewHolder detailsViewHolder = (DetailsViewHolder) holder;
            detailsViewHolder.bind(context, moves.get(0));
        } else if (position == 1) {
            final TransportationViewHolder transportationViewHolder = (TransportationViewHolder) holder;
            transportationViewHolder.bind(context, moves.get(0));
        } else if (position == 2 && moves.get(0).moveType.equals("food")) {
            final PhotosViewHolder photosViewHolder = (PhotosViewHolder) holder;
            photosViewHolder.bind(context, moves.get(0));
        }
    }


    @Override
    public int getItemCount() {
        if (moves.get(0).lat != null && moves.get(0).moveType.equals("food")) {
            Restaurant restaurant = (Restaurant) moves.get(0);

            return (restaurant.photoReferences.size() > 0) ? 3 : 2;
        } else {
            return 2;
        }
    }

}
