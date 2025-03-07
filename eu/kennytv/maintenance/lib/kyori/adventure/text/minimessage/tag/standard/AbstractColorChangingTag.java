/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.internal.Internals;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.flattener.ComponentFlattener;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Inserting;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Modifying;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tree.Node;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.Collections;
import java.util.PrimitiveIterator;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractColorChangingTag
implements Modifying,
Examinable {
    private static final ComponentFlattener LENGTH_CALCULATOR = (ComponentFlattener)ComponentFlattener.builder().mapper(TextComponent.class, TextComponent::content).unknownMapper(x -> "_").build();
    private boolean visited;
    private int size = 0;
    private int disableApplyingColorDepth = -1;

    AbstractColorChangingTag() {
    }

    protected final int size() {
        return this.size;
    }

    @Override
    public final void visit(@NotNull Node current, int depth) {
        TagNode tag;
        if (this.visited) {
            throw new IllegalStateException("Color changing tag instances cannot be re-used, return a new one for each resolve");
        }
        if (current instanceof ValueNode) {
            String value = ((ValueNode)current).value();
            this.size += value.codePointCount(0, value.length());
        } else if (current instanceof TagNode && (tag = (TagNode)current).tag() instanceof Inserting) {
            LENGTH_CALCULATOR.flatten(((Inserting)tag.tag()).value(), s -> this.size += s.codePointCount(0, s.length()));
        }
    }

    @Override
    public final void postVisit() {
        this.visited = true;
        this.init();
    }

    @Override
    public final Component apply(@NotNull Component current, int depth) {
        if (this.disableApplyingColorDepth != -1 && depth > this.disableApplyingColorDepth || current.style().color() != null) {
            if (this.disableApplyingColorDepth == -1 || depth < this.disableApplyingColorDepth) {
                this.disableApplyingColorDepth = depth;
            }
            if (current instanceof TextComponent) {
                String content = ((TextComponent)current).content();
                int len = content.codePointCount(0, content.length());
                for (int i = 0; i < len; ++i) {
                    this.advanceColor();
                }
            }
            return current.children(Collections.emptyList());
        }
        this.disableApplyingColorDepth = -1;
        if (current instanceof TextComponent && ((TextComponent)current).content().length() > 0) {
            TextComponent textComponent = (TextComponent)current;
            String content = textComponent.content();
            TextComponent.Builder parent = Component.text();
            int[] holder = new int[1];
            PrimitiveIterator.OfInt it = content.codePoints().iterator();
            while (it.hasNext()) {
                holder[0] = it.nextInt();
                TextComponent comp = Component.text(new String(holder, 0, 1), current.style().color(this.color()));
                this.advanceColor();
                parent.append((Component)comp);
            }
            return parent.build();
        }
        if (!(current instanceof TextComponent)) {
            Component ret = current.children(Collections.emptyList()).colorIfAbsent(this.color());
            this.advanceColor();
            return ret;
        }
        return Component.empty().mergeStyle(current);
    }

    protected abstract void init();

    protected abstract void advanceColor();

    protected abstract TextColor color();

    @Override
    @NotNull
    public abstract Stream<? extends ExaminableProperty> examinableProperties();

    @NotNull
    public final String toString() {
        return Internals.toString(this);
    }

    public abstract boolean equals(@Nullable Object var1);

    public abstract int hashCode();
}

