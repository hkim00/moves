package com.hkim00.moves.specs;


import androidx.recyclerview.widget.OrientationHelper;

import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.LinearLayoutInfo;
import com.facebook.litho.widget.Recycler;
import com.facebook.litho.widget.RecyclerBinder;
import com.facebook.litho.widget.TextInput;
import com.facebook.yoga.YogaEdge;

@LayoutSpec
public class SearchComponentSpec {

    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c,
                                 @Prop String hint,
                                 @Prop RecyclerBinder binder) {

        return Column.create(c)
                .paddingDip(YogaEdge.ALL, 10)
                .child(getQueryComponent(c, hint))
                .child(getRecyclerComponent(c, binder))
                .build();
    }


    private static Component getQueryComponent(ComponentContext c, String hint) {

        return TextInput.create(c)
                .textSizeDip(16)
                .hint(hint)
                .build();
    }

    private static Component getRecyclerComponent(ComponentContext c, RecyclerBinder binder) {
        return Recycler.create(c)
                .binder(binder)
                .build();
    }
}
