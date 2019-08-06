package com.hkim00.moves;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.hkim00.moves.models.Cuisine;
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

        setupRecycler();
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void setupRecycler() {
        final ComponentContext c = new ComponentContext(this);

        final Component component =
                RecyclerCollectionComponent.create(c)
                        .disablePTR(true)
                        .section(
                                MoveSection.create(new SectionContext(c)).moves(moves).build())
                        .build();

        setContentView(LithoView.create(c, component));
    }
}
