package com.hkim00.moves;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.models.Move;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private final static String TAG = "ProfileActivity";

    private Button btnSaved, btnFavorites, btnLogout;
    private ImageView ivProfilePic;
    private RecyclerView rvFavorites;
    private RecyclerView rvSaved;

    private TextView tvName;

    private MoveAdapter Faveadapter;
    private MoveAdapter Saveadapter;
    private List<Move> rFaveList;
    private List<Move> rSaveList;

    private ParseUser user;
    private boolean isFriend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        findViewIds();

        setupButtons();

        setupRecyclerView();

        fillUserInfo();

        checkStatus();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void fillUserInfo() {
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        tvName.setText(user.getUsername());

        if (user.has("profilePhoto")) {
            ParseFile profileImage = user.getParseFile("profilePhoto");
            if (profileImage != null) {

                Glide.with(getApplicationContext())
                        .load(profileImage.getUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivProfilePic);
            }
        }
    }

    private void findViewIds() {
        ivProfilePic = findViewById(R.id.ivProfilePic);
        btnSaved = findViewById(R.id.btnSave);
        btnFavorites = findViewById(R.id.btnFavorite);
        btnLogout = findViewById(R.id.btnLogout);
        rvFavorites = findViewById(R.id.rvMoves);
        rvSaved = findViewById(R.id.rvSaved);

        tvName = findViewById(R.id.tvName);

        btnLogout.setEnabled(false);
        btnLogout.setText("Loading");
    }

    private void setupButtons() {
        btnFavorites.setOnClickListener(view -> toggleTabs(true));

        btnSaved.setOnClickListener(view -> toggleTabs(false));

        btnLogout.setOnClickListener(view -> {
            if (isFriend) {
                unFriendUser();
            } else {
                friendUser();
            }
        });
    }

    private void setupRecyclerView() {
        rvFavorites.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rvSaved.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        //TODO: need to account for two different adapters (Events vs. Restaurant)
        rFaveList = new ArrayList<>();
        Faveadapter = new MoveAdapter(getApplicationContext(), rFaveList);
        rvFavorites.setAdapter(Faveadapter);

        rSaveList = new ArrayList<>();
        Saveadapter = new MoveAdapter(getApplicationContext(), rSaveList);
        rvSaved.setAdapter(Saveadapter);
    }

    private void toggleTabs(boolean isFav) {
        rvFavorites.setVisibility((isFav) ? View.VISIBLE : View.INVISIBLE);
        rvSaved.setVisibility((!isFav) ? View.VISIBLE : View.INVISIBLE);
    }

    private void checkStatus() {
        ParseQuery<ParseObject> friendQuery = ParseQuery.or(getSenderOrReceiverQueries());
        friendQuery.countInBackground((count, e) -> {
            if (e == null) {
                btnLogout.setEnabled(true);

                if (count > 0) {
                    showIsFriendButton();
                } else {
                    showIsNotFriendButton();
                }
            } else {
                Log.e(TAG, "Error checking if user follows");
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error checking follow status", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Error friending user", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(getApplicationContext(), "Error unfriending user", Toast.LENGTH_SHORT).show();

                            showIsFriendButton();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Error unfriending user");
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error unfriending user", Toast.LENGTH_SHORT).show();

                showIsFriendButton();
            }
        });
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


    private void showIsFriendButton() {
        btnLogout.setText("Unfriend");
        isFriend = true;
    }

    private void showIsNotFriendButton() {
        btnLogout.setText("Friend");
        isFriend = false;
    }

}


