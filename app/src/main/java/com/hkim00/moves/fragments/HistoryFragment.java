package com.hkim00.moves.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.litho.ComponentContext;
import com.facebook.litho.widget.RecyclerBinder;
import com.hkim00.moves.R;
import com.hkim00.moves.adapters.HistoryAdapter;
import com.hkim00.moves.models.Move;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    public final static String TAG = "HistoryFragment";

    private static List<Move> pastMoves;
    private static Context context;
    private RecyclerView rvMoves;
    private HistoryAdapter movesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMoves = view.findViewById(R.id.rvPastMoves);

        setupRecyclerView();
        getMoveLists();
    }

    private void getMoveLists() {
        ParseQuery<ParseObject> moveQuery = ParseQuery.getQuery("Move");

        moveQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        moveQuery.orderByDescending("createdAt");
        moveQuery.whereEqualTo("didComplete", true);
        moveQuery.findInBackground((objects, e) -> {
            if (e == null) {
                List<Move> moves = new ArrayList<>();

                for (int i = 0; i < objects.size(); i++) {
                    moves.add(Move.fromParseObject(objects.get(i)));
                }
                pastMoves.clear();
                pastMoves.addAll(moves);
                movesAdapter.notifyDataSetChanged();

            } else {
                Log.e(TAG, "Error finding history.");
                e.printStackTrace();
            }
        });
    }

    private void setupRecyclerView() {
        rvMoves.setLayoutManager(new LinearLayoutManager(getContext()));

        pastMoves = new ArrayList<>();

        movesAdapter = new HistoryAdapter(getContext(), pastMoves);
        rvMoves.setAdapter(movesAdapter);
    }
}
