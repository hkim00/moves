package com.hkim00.moves.specs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.facebook.litho.ClickEvent;
import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaEdge;
import com.hkim00.moves.MoveDetailsActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.models.Event;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.MoveText;
import com.hkim00.moves.models.Restaurant;

import org.parceler.Parcels;

@LayoutSpec
public class MoveItemSpec {

    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c,
                                    @Prop Move move) {


        String name = (move.name == null) ? ((MoveText) move).Cuisine : move.name ;


        return Column.create(c)
                .paddingDip(YogaEdge.ALL, 15)
                .backgroundColor(Color.WHITE)
                .clickHandler(MoveItem.onClick(c))
                .child(
                        Text.create(c)
                                .text(name)
                                .textSizeDip(15)
                                .textStyle(1)) //bold
                .build();
    }

    @OnEvent(ClickEvent.class)
    static void onClick(
            ComponentContext c,
            @FromEvent View view,
            @Prop Move move) {

        final Intent intent = new Intent(c.getAndroidContext(), MoveDetailsActivity.class);
        intent.putExtra("move", Parcels.wrap(move));
        c.getAndroidContext().startActivity(intent);
    }
}