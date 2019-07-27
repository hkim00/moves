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
import com.hkim00.moves.ProfileActivity;
import com.hkim00.moves.R;
import com.parse.ParseUser;

import org.parceler.Parcels;


@LayoutSpec
public class UserItemSpec {
    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c,
                                    @Prop Context context,
                                    @Prop ParseUser user) {

        return Column.create(c)
                .paddingDip(YogaEdge.ALL, 15)
                .backgroundColor(Color.WHITE)
                .clickHandler(UserItem.onClick(c))
                .child(
                        Text.create(c)
                                .text(user.getUsername())
                                .textSizeDip(15)
                                .textStyle(1)) //bold
                .build();
    }

    @OnEvent(ClickEvent.class)
    static void onClick(
            ComponentContext c,
            @FromEvent View view,
            @Prop Context context,
            @Prop ParseUser user) {

        final Intent intent = new Intent(c.getAndroidContext(), ProfileActivity.class);
        intent.putExtra("user", Parcels.wrap(user));
        c.getAndroidContext().startActivity(intent);

        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }
}


