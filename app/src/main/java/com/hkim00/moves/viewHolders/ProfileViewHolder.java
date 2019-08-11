package com.hkim00.moves.viewHolders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hkim00.moves.R;
import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.models.Move;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProfileViewHolder extends RecyclerView.ViewHolder {
    public final static String TAG = "ProfileViewHolder";

    private TextView tvName, tvDateJoined, tvFriend;
    private ImageView ivProfilePic;
    private Context context;
    private Button btnFriend;
    private View vFriendView;

    private ParseUser user;
    private boolean isFriend;


    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);
        ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        tvDateJoined = itemView.findViewById(R.id.tvDateJoined);

        btnFriend = itemView.findViewById(R.id.btnFriend);
        tvFriend = itemView.findViewById(R.id.tvFriend);
        vFriendView = itemView.findViewById(R.id.vFriendView);

        btnFriend.setOnClickListener(view -> {
            if (isFriend) {
                unFriendUser();
            } else {
                friendUser();
            }
        });
    }

    public void bind(Context context, boolean isHome, ParseUser user) {
        this.context = context;
        this.user = user;
        fillUserInfo(user);

        btnFriend.setVisibility(isHome ? View.INVISIBLE : View.VISIBLE);
        tvFriend.setVisibility(isHome ? View.INVISIBLE : View.VISIBLE);
        vFriendView.setVisibility(isHome ? View.INVISIBLE : View.VISIBLE);
    }

        private void fillUserInfo(ParseUser user) {
        if (user != null) {
            tvName.setText(user.getUsername());

            String formatDate = new SimpleDateFormat("MMM d yyyy").format(user.getCreatedAt());
            tvDateJoined.setText("Joined: " + formatDate);

            if (user.has("profilePhoto")) {
                ParseFile profileImage = user.getParseFile("profilePhoto");
                if (profileImage != null) {
                    Glide.with(context)
                            .load(profileImage.getUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(ivProfilePic);
                }
            }

            if (!user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                checkStatus();
            }
        }
    }

    private List<ParseQuery<ParseObject>> getSenderOrReceiverQueries() {
        ParseQuery<ParseObject> senderQuery = ParseQuery.getQuery("Friend");
        senderQuery.whereEqualTo("sender", ParseUser.getCurrentUser());
        senderQuery.whereEqualTo("receiver", user);
        senderQuery.whereEqualTo("isPending", false);

        ParseQuery<ParseObject> receiverQuery = ParseQuery.getQuery("Friend");
        receiverQuery.whereEqualTo("sender", user);
        receiverQuery.whereEqualTo("receiver", ParseUser.getCurrentUser());
        receiverQuery.whereEqualTo("isPending", false);

        List<ParseQuery<ParseObject>> friendQueries = new ArrayList<>();
        friendQueries.add(senderQuery);
        friendQueries.add(receiverQuery);

        return friendQueries;
    }

    private void checkStatus() {//user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        ParseQuery<ParseObject> friendQuery = ParseQuery.or(getSenderOrReceiverQueries());
        friendQuery.countInBackground((count, e) -> {
            if (e == null) {
                btnFriend.setEnabled(true);

                if (count > 0) {
                    showIsFriendButton();
                } else {
                    showIsNotFriendButton();
                }
            } else {
                Log.e(TAG, "Error checking if user follows");
                e.printStackTrace();
                Toast.makeText(context, "Error checking follow status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void friendUser() {
        showIsFriendButton();

        ParseObject friendObject = new ParseObject("Friend");
        friendObject.put("isPending", false); //have pending option if we want to send friend request and not just make automatic friends
        friendObject.put("sender", ParseUser.getCurrentUser());
        friendObject.put("receiver", user);
        friendObject.saveInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Error friending user");
                e.printStackTrace();
                Toast.makeText(context, "Error friending user", Toast.LENGTH_SHORT).show();

                showIsNotFriendButton();
            }
        });
    }

    private void unFriendUser() {
        showIsNotFriendButton();

        ParseQuery<ParseObject> friendQuery = ParseQuery.or(getSenderOrReceiverQueries());
        friendQuery.findInBackground((objects, e) -> {
            if (e == null) {
                for (ParseObject object : objects) {
                    object.deleteInBackground(e1 -> {
                        if (e1 != null) {
                            Log.e(TAG, "Error unfriending user");
                            e1.printStackTrace();
                            Toast.makeText(context, "Error unfriending user", Toast.LENGTH_SHORT).show();

                            showIsFriendButton();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Error unfriending user");
                e.printStackTrace();
                Toast.makeText(context, "Error unfriending user", Toast.LENGTH_SHORT).show();

                showIsFriendButton();
            }
        });
    }

    private void showIsFriendButton() {
        tvFriend.setText("Unfriend");
        vFriendView.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
        isFriend = true;
    }

    private void showIsNotFriendButton() {
        tvFriend.setText("Friend");
        vFriendView.setBackgroundColor(context.getResources().getColor(R.color.selected_blue));
        isFriend = false;
    }
}
