package com.hkim00.moves.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hkim00.moves.LogInActivity;

import com.hkim00.moves.R;
import com.hkim00.moves.adapters.MoveAdapter;
import com.hkim00.moves.models.Move;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    public final static String TAG = "ProfileFragment";
    ParseUser currUser;

    private Button btnSaved, btnFavorites, btnLogout;
    private TextView tvName;
    private ImageView ivProfilePic, ivSaved, ivFavorites;

    private RecyclerView rvMoves;
    private MoveAdapter movesAdapter;
    private List<Move> saveMoves, favMoves, moves;

    private boolean didCheckSave = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewIds(view);

        setupButtons();

        setupRecyclerView();

        fillUserInfo();
    }

    private void getViewIds(View view) {
        tvName = view.findViewById(R.id.tvName);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        ivSaved = view.findViewById(R.id.ivSaved);
        ivFavorites = view.findViewById(R.id.ivFavorites);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnSaved =  view.findViewById(R.id.btnSave);
        btnFavorites = view.findViewById(R.id.btnFavorite);
        rvMoves = view.findViewById(R.id.rvMoves);
    }

    private void fillUserInfo() {
        currUser = ParseUser.getCurrentUser();
        tvName.setText(currUser.getUsername());

        if (currUser.has("profilePhoto")) {
            ParseFile profileImage = currUser.getParseFile("profilePhoto");
            if (profileImage != null) {
                Glide.with(getContext())
                        .load(profileImage.getUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivProfilePic);
            }
        }

        getMoveLists("favorites");
    }

    private void setupRecyclerView() {
        rvMoves.setLayoutManager(new LinearLayoutManager(getContext()));

        saveMoves = new ArrayList<>();
        favMoves = new ArrayList<>();
        moves = new ArrayList<>();

        MovesAdapter = new MoveAdapter(getContext(), moves);
        rvMoves.setAdapter(movesAdapter);

    }

    private void getMoveLists(String listType) {
        ParseQuery<ParseObject> moveQuery = ParseQuery.getQuery("Move");

        moveQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        moveQuery.orderByDescending("createdAt");
        if (listType.equals("saved")) {
            moveQuery.whereEqualTo("didSave", true);
        } else {
            moveQuery.whereEqualTo("didFavorite", true);
        }
            moveQuery.findInBackground((objects, e) -> {
                if (e == null) {
                    List<Move> results = new ArrayList<>();

                    for (int i = 0; i < objects.size(); i++) {
                        if (objects.get(i).getString("moveType").equals("food")) {
                            results.add(Move.fromParseObject(objects.get(i)));
                        } else {
                            results.add(Move.fromParseObject(objects.get(i)));
                        }
                    }

                    if (listType.equals("saved")) {
                        saveMoves = results;
                        didCheckSave = true;
                    } else {
                        favMoves = results;
                    }

                    updateMoves(results);
                } else {
                    Log.e(TAG, "Error finding saved list.");
                    e.printStackTrace();
                }
            });
        }


    private void updateMoves(List<Move> replacementArray) {
        moves.clear();
        moves.addAll(replacementArray);
        movesAdapter.notifyDataSetChanged();
    }


    private void setupButtons() {
        btnLogout.setOnClickListener(view -> logout());
        btnFavorites.setOnClickListener(view -> toggleRecyclerInfo(true));
        btnSaved.setOnClickListener(view -> toggleRecyclerInfo(false));
    }


    private void logout() {
        ParseUser.logOut();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("location", 0); //0 for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        final Intent intent = new Intent(getContext(), LogInActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    private void toggleRecyclerInfo(boolean isFavView) {
        ivSaved.setImageResource((isFavView) ? R.drawable.ufi_save : R.drawable.ufi_save_active);
        ivFavorites.setImageResource((isFavView) ? R.drawable.ufi_heart_active : R.drawable.ufi_heart);

        if (!didCheckSave && !isFavView) {
            getMoveLists("saved");
        } else {
            updateMoves((isFavView) ? favMoves : saveMoves);
        }
    }
}
