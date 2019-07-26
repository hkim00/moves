package com.hkim00.moves.fragments;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.OrientationHelper;

import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.widget.LinearLayoutInfo;
import com.facebook.litho.widget.Recycler;
import com.facebook.litho.widget.RecyclerBinder;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaEdge;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.specs.SearchComponent;
import com.hkim00.moves.specs.SearchComponentSpec;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    public final static String TAG = "SearchFragment";

    private List<Move> pastMoves;
    private ComponentContext componentContext;
    private RecyclerBinder recyclerBinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return setupRecycler();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private View setupRecycler() {
        componentContext = new ComponentContext(getContext());

        recyclerBinder =  new RecyclerBinder.Builder()
                .layoutInfo(new LinearLayoutInfo(getContext(), OrientationHelper.VERTICAL, false))
                .build(componentContext);

        final Component component = SearchComponent.create(componentContext)
                .hint("Search username")
                .binder(recyclerBinder)
                .build();

        return LithoView.create(componentContext, component);
    }
}
