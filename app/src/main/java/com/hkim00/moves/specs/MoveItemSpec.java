package com.hkim00.moves.specs;

import android.graphics.Color;

import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaEdge;

@LayoutSpec
public class MoveItemSpec {

    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c,
                                    @Prop String title) {

        return Column.create(c)
                .paddingDip(YogaEdge.ALL, 15)
                .backgroundColor(Color.WHITE)
                .child(
                        Text.create(c)
                                .text(title)
                                .textSizeDip(15))
                .build();
    }

}
