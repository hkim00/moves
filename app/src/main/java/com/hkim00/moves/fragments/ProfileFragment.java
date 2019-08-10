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

import com.hkim00.moves.LogInActivity;

import com.hkim00.moves.R;
import com.hkim00.moves.adapters.ProfileAdapter;
import com.hkim00.moves.models.Move;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    public final static String TAG = "ProfileFragment";
    ParseUser currUser;

    private Button btnSaved, btnFavorites, btnLogout;
    private TextView tvName, tvDateJoined;
    private ImageView ivProfilePic;
    private static ImageView ivSaved, ivFavorites;

    private RecyclerView rvProfileFragment;
    private static ProfileAdapter movesAdapter;
    private static List<Move> saveMoves, favMoves, moves;

    private static boolean didCheckSave = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewIds(view);

        setupRecyclerView();


    }

    private void getViewIds(View view) {
        rvProfileFragment = view.findViewById(R.id.rvMoves);
    }


    private void setupRecyclerView() {
        rvProfileFragment.setLayoutManager(new LinearLayoutManager(getContext()));

        moves = new ArrayList<>();

        movesAdapter = new ProfileAdapter(getContext(), moves);
        rvProfileFragment.setAdapter(movesAdapter);
    }

    public static void getMoveLists(String listType) {
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
                        results.add(Move.fromParseObject(objects.get(i)));
                    }

                    if (listType.equals("saved")) {
                        saveMoves = results;
                        didCheckSave = true;
                    } else {
                        favMoves = results;
                        didCheckSave = false;
                    }

                    updateMoves(results);
                } else {
                    Log.e(TAG, "Error finding saved list.");
                    e.printStackTrace();
                }
            });
        }


    public static void updateMoves(List<Move> replacementArray) {
        moves.clear();
        moves.addAll(replacementArray);
        movesAdapter.notifyDataSetChanged();
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


    public static void toggleRecyclerInfo(boolean isFavView) {
        if (!didCheckSave && !isFavView) {
            getMoveLists("saved");
        } else {
            getMoveLists("favMoves");
        }
    }

}
