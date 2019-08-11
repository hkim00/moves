package com.hkim00.moves.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hkim00.moves.ProfileActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.TripActivity;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private Context context;
    private List<ParseUser> users;


    public SearchUserAdapter(Context context, List<ParseUser> users) {
            this.context = context;
            this.users = users;
    }


    @NonNull
    @Override
    public SearchUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
            return new SearchUserAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchUserAdapter.ViewHolder holder, int position) {
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
            goToProfileActivity(user);
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

        private void goToProfileActivity(ParseUser user) {
            if (user != null) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user", Parcels.wrap(user));
                context.startActivity(intent);

                if (context instanceof Activity) {
                    ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        }
    }
}
