package com.hkim00.moves.specs;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.hkim00.moves.HomeActivity;
import com.hkim00.moves.ProfileActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.fragments.HomeFragment;
import com.hkim00.moves.fragments.SearchFragment;
import com.parse.ParseUser;

import org.parceler.Parcels;

import static java.security.AccessController.getContext;


@LayoutSpec
public class UserItemSpec {
    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c,
                                    @Prop Context context,
                                    @Prop ParseUser user,
                                    @Prop Boolean isAddFriend) {

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
            @Prop ParseUser user,
            @Prop Boolean isAddFriend) {

        if (!isAddFriend) {
            final Intent intent = new Intent(c.getAndroidContext(), ProfileActivity.class);
            intent.putExtra("user", Parcels.wrap(user));
            c.getAndroidContext().startActivity(intent);

            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        } else {
            Fragment fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("friend", user);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = HomeActivity.fragmentManager;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}


