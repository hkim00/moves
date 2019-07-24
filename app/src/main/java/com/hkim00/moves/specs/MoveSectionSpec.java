package com.hkim00.moves.specs;

import android.graphics.Color;

import com.facebook.litho.sections.Children;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.annotations.GroupSectionSpec;
import com.facebook.litho.sections.annotations.OnCreateChildren;
import com.facebook.litho.sections.common.SingleComponentSection;

import java.util.ArrayList;
import java.util.List;

@GroupSectionSpec
public class MoveSectionSpec {

    @OnCreateChildren
    static Children onCreateChildren(final SectionContext c) {
        Children.Builder builder = Children.create();

        for (int i = 0; i < 32; i++) {
            builder.child(
                    SingleComponentSection.create(c)
                            .key(String.valueOf(i))
                            .component(MoveItem.create(c)
                                    .color(i % 2 == 0 ? Color.WHITE : Color.LTGRAY)
                                    .title(i + ". Hello, world!")
                                    .subtitle("Litho tutorial")
                                    .build()));
        }
        return builder.build();
    }

    private static List<Integer> generateData(int count) {
        final List<Integer> data = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            data.add(i);
        }
        return data;
    }
}
