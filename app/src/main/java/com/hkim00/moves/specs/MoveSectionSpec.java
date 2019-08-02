package com.hkim00.moves.specs;

import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.sections.Children;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.annotations.GroupSectionSpec;
import com.facebook.litho.sections.annotations.OnCreateChildren;
import com.facebook.litho.sections.common.DataDiffSection;
import com.facebook.litho.sections.common.RenderEvent;
import com.facebook.litho.widget.ComponentRenderInfo;
import com.facebook.litho.widget.RenderInfo;
import com.hkim00.moves.models.Move;

import java.util.List;

@GroupSectionSpec
public class MoveSectionSpec {

    @OnCreateChildren
    static Children onCreateChildren(SectionContext c,
                                     @Prop List<Move> moves) {
        return Children.create()
                .child(
                        DataDiffSection.<Move>create(c)
                                .data(moves)
                                .renderEventHandler(MoveSection.onRender(c)))
                .build();
    }

    @OnEvent(RenderEvent.class)
    static RenderInfo onRender(final SectionContext c, @FromEvent Move model) {
        return ComponentRenderInfo.create()
                .component(
                        MoveItem.create(c)
                                .move(model)
                                .build())
                .build();
    }
}
