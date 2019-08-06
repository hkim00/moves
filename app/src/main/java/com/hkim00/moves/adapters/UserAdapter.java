package com.hkim00.moves.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hkim00.moves.R;
import com.hkim00.moves.TripActivity;
import com.hkim00.moves.models.Trip;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<ParseUser> users;


    public UserAdapter(Context context, List<ParseUser> users) {
        this.context = context;
        this.users = users;
    }


    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        ParseUser user = users.get(position);
        holder.user = user;
        holder.bind(user);
    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProfilePic, ivCheckmark;
        private TextView tvUsername;
        public ParseUser user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            ivCheckmark = itemView.findViewById(R.id.ivCheckmark);
            tvUsername = itemView.findViewById(R.id.tvUsername);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            toggleCheckmark();
        }

        public void bind(ParseUser user) {
            tvUsername.setText(user.getUsername());

            ivCheckmark.setVisibility(View.INVISIBLE);

            if (user.has("profilePhoto")) {
                ParseFile profileImage = user.getParseFile("profilePhoto");
                if (profileImage != null) {

                    Glide.with(context)
                            .load(profileImage.getUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(ivProfilePic);
                }
            }
        }

        private void toggleCheckmark() {
            if (ivCheckmark.getVisibility() == View.VISIBLE) {
                ivCheckmark.setVisibility(View.INVISIBLE);
                TripActivity.selectedFriends.remove(user);
            } else {
                ivCheckmark.setVisibility(View.VISIBLE);
                TripActivity.selectedFriends.add(user);
            }
        }
    }
}
