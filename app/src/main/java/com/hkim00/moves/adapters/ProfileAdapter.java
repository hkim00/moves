package com.hkim00.moves.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.HomeActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.fragments.ProfileFragment;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.viewHolders.ChooseMoveViewHolder;
import com.hkim00.moves.viewHolders.DetailsViewHolder;
import com.hkim00.moves.viewHolders.MovesViewHolder;
import com.hkim00.moves.viewHolders.PhotosViewHolder;
import com.hkim00.moves.viewHolders.ProfileButtonsViewHolder;
import com.hkim00.moves.viewHolders.ProfileViewHolder;
import com.hkim00.moves.viewHolders.TransportationViewHolder;
import com.hkim00.moves.viewHolders.VenueViewHolder;
import com.parse.ParseUser;

import java.util.List;

public class ProfileAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int PROFILE_POSITION = 0;
    private static final int SAVE_FAVE_POSITION = PROFILE_POSITION + 1;
    private static final int MOVES_POSITION = SAVE_FAVE_POSITION + 1;

    private Context context;
    private List<Move> moves;
    private boolean isHome;
    private ParseUser user;

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public ProfileAdapter(Context context, List<Move> moves, ParseUser user) {
        this.context = context;
        this.moves = moves;
        this.user = user;

        this.isHome = context instanceof HomeActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == PROFILE_POSITION) {
            View view = inflater.inflate(R.layout.item_profile, parent, false);
            viewHolder = new ProfileViewHolder(view);
        }

        else if (viewType == SAVE_FAVE_POSITION) {
            View view = inflater.inflate(R.layout.item_save_fave, parent, false);
            viewHolder = new ProfileButtonsViewHolder(view);
        }

        else {
            View view = inflater.inflate(R.layout.item_move, parent, false);
            viewHolder = new MovesViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == PROFILE_POSITION) {
            final ProfileViewHolder viewHolder = (ProfileViewHolder) holder;
            viewHolder.bind(context, isHome, user);
        } else if (position == SAVE_FAVE_POSITION) {
            final ProfileButtonsViewHolder viewHolder = (ProfileButtonsViewHolder) holder;
            viewHolder.bind(context);
        } else {
            final MovesViewHolder viewHolder = (MovesViewHolder) holder;
            viewHolder.bind(context, moves.get(position-2));
        }
    }

    @Override
    public int getItemCount() {
        return (!isHome) ? 1 : 2 + moves.size();
    }
}
