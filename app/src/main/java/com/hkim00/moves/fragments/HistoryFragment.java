package com.hkim00.moves.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hkim00.moves.R;
import com.hkim00.moves.adapters.ProfileAdapter;
import com.hkim00.moves.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView rvPastMoves;
    private ProfileAdapter historyAdapter;
    private List<Restaurant> rHistoryList;
    public final static String TAG = "HistoryFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPastMoves = view.findViewById(R.id.rvPastMoves);

        rvPastMoves.setLayoutManager(new LinearLayoutManager(getContext()));
        rHistoryList = new ArrayList<>();
        historyAdapter = new ProfileAdapter(getContext(), rHistoryList);
        rvPastMoves.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
    }
}
