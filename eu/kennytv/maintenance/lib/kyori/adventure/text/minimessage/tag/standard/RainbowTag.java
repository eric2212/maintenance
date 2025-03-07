/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.AbstractColorChangingTag;
import eu.kennytv.maintenance.lib.kyori.adventure.util.HSVLike;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class RainbowTag
extends AbstractColorChangingTag {
    private static final String REVERSE = "!";
    private static final String RAINBOW = "rainbow";
    static final TagResolver RESOLVER = TagResolver.resolver("rainbow", RainbowTag::create);
    private final boolean reversed;
    private final double dividedPhase;
    private int colorIndex = 0;

    static Tag create(ArgumentQueue args, Context ctx) {
        boolean reversed = false;
        int phase = 0;
        if (args.hasNext()) {
            String value = args.pop().value();
            if (value.startsWith(REVERSE)) {
                reversed = true;
                value = value.substring(REVERSE.length());
            }
            if (value.length() > 0) {
                try {
                    phase = Integer.parseInt(value);
                } catch (NumberFormatException ex) {
                    throw ctx.newException("Expected phase, got " + value);
                }
            }
        }
        return new RainbowTag(reversed, phase);
    }

    private RainbowTag(boolean reversed, int phase) {
        this.reversed = reversed;
        this.dividedPhase = (double)phase / 10.0;
    }

    @Override
    protected void init() {
        if (this.reversed) {
            this.colorIndex = this.size() - 1;
        }
    }

    @Override
    protected void advanceColor() {
        this.colorIndex = this.reversed ? (this.colorIndex == 0 ? this.size() - 1 : --this.colorIndex) : ++this.colorIndex;
    }

    @Override
    protected TextColor color() {
        float index = this.colorIndex;
        float hue = (float)(((double)(index / (float)this.size()) + this.dividedPhase) % 1.0);
        return TextColor.color(HSVLike.hsvLike(hue, 1.0f, 1.0f));
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("phase", this.dividedPhase));
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        RainbowTag that = (RainbowTag)other;
        return this.colorIndex == that.colorIndex && this.dividedPhase == that.dividedPhase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.colorIndex, this.dividedPhase);
    }
}

