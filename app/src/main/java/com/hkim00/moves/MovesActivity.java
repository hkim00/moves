package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.specs.MoveSection;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MovesActivity extends AppCompatActivity {

    List<Move> moves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moves);

        moves = new ArrayList<>();
        moves.addAll(Parcels.unwrap(getIntent().getParcelableExtra("moves")));

        final ComponentContext context = new ComponentContext(this);

        final Component component =
                RecyclerCollectionComponent.create(context)
                        .disablePTR(true)
                        .section(
                                MoveSection.create(new SectionContext(context))
                                        .moves(moves)
                                        .build())
                        .build();

        setContentView(LithoView.create(context, component));
    }
}
