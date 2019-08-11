package com.hkim00.moves.viewHolders;

import android.content.Context;
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
import com.hkim00.moves.models.Move;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProfileViewHolder extends RecyclerView.ViewHolder {
    public final static String TAG = "ProfileViewHolder";
    ParseUser currUser;

    private TextView tvName, tvDateJoined;
    private ImageView ivProfilePic;
    private Context context;

    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);
        ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        tvDateJoined = itemView.findViewById(R.id.tvDateJoined);
    }

    public void bind(Context context) {
        this.context = context;
        fillUserInfo();
    }

        private void fillUserInfo() {
        currUser = ParseUser.getCurrentUser();
        tvName.setText(currUser.getUsername());

        String formatDate = new SimpleDateFormat("MMM d yyyy").format(currUser.getCreatedAt());
        tvDateJoined.setText("Joined: " + formatDate);

        if (currUser.has("profilePhoto")) {
            ParseFile profileImage = currUser.getParseFile("profilePhoto");
            if (profileImage != null) {
                Glide.with(context)
                        .load(profileImage.getUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivProfilePic);
            }
        }
    }
}
