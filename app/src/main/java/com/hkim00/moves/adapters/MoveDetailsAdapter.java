package com.hkim00.moves.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.hkim00.moves.viewHolders.ChooseMoveViewHolder;
import com.hkim00.moves.viewHolders.DetailsViewHolder;
import com.hkim00.moves.viewHolders.PhotosViewHolder;
import com.hkim00.moves.viewHolders.TransportationViewHolder;
import com.hkim00.moves.viewHolders.VenueViewHolder;

import java.util.List;

public class MoveDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int DETAILS_POSITION = 0;
    private static final int CHOOSE_MOVE_POSITION = DETAILS_POSITION + 1;
    private static final int TRANSPORTATION_POSITION = 2;
    private static final int IMAGE_POSITION = 3;


    private Context context;
    private List<Move> moves;
    private boolean isTrip;


    public MoveDetailsAdapter(Context context, List<Move> moves, boolean isTrip) {
        this.context = context;
        this.moves = moves;
        this.isTrip = isTrip;
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

        if (viewType == DETAILS_POSITION) {
            View view = inflater.inflate(R.layout.item_move_details, parent, false);
            viewHolder = new DetailsViewHolder(view);
        }

        else if (viewType == CHOOSE_MOVE_POSITION) {
            View view = inflater.inflate(R.layout.item_choose_move, parent, false);
            viewHolder = new ChooseMoveViewHolder(view);
        }

        else if (viewType == TRANSPORTATION_POSITION) {
            View view = inflater.inflate(R.layout.item_transportation, parent, false);
            viewHolder = new TransportationViewHolder(view);
        }

        else if (viewType == IMAGE_POSITION) {
            View view = inflater.inflate((moves.get(0).moveType.equals("food")) ? R.layout.item_photos : R.layout.item_venue, parent, false);
            viewHolder = (moves.get(0).moveType.equals("food")) ? new PhotosViewHolder(view) : new VenueViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == DETAILS_POSITION) {
            final DetailsViewHolder viewHolder = (DetailsViewHolder) holder;
            viewHolder.bind(context, moves.get(0));
        }

        else if (position == CHOOSE_MOVE_POSITION) {
            final ChooseMoveViewHolder viewHolder = (ChooseMoveViewHolder) holder;
            viewHolder.bind(context, moves.get(0), isTrip);
        }

        else if (position == TRANSPORTATION_POSITION) {
            final TransportationViewHolder viewHolder = (TransportationViewHolder) holder;
            viewHolder.bind(context, moves.get(0));
        }

        else if (position == IMAGE_POSITION && moves.get(0).moveType.equals("food")) {
            final PhotosViewHolder viewHolder = (PhotosViewHolder) holder;
            viewHolder.bind(context, moves.get(0));
        }

        else if (position == IMAGE_POSITION && moves.get(0).moveType.equals("event")) {
            final VenueViewHolder viewHolder = (VenueViewHolder) holder;
            viewHolder.bind(context, moves.get(0));
        }
    }


    @Override
    public int getItemCount() {
        if (moves.get(0).didCheckHTTPDetails && moves.get(0).moveType.equals("food")) {
            Restaurant restaurant = (Restaurant) moves.get(0);

            return (restaurant.photoReferences.size() > 0) ? 4 : 3;
        } else if (moves.get(0).didCheckHTTPDetails && moves.get(0).moveType.equals("event")) {
            Event event = (Event) moves.get(0);

            return (event.venueName != null && event.venuePhotoUrl != null) ? 4 : 3;
        }

        return 3;
    }

}
