/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Range
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.AbstractColorChangingTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.ColorTagResolver;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

final class GradientTag
extends AbstractColorChangingTag {
    private static final String GRADIENT = "gradient";
    static final TagResolver RESOLVER = TagResolver.resolver("gradient", GradientTag::create);
    private int index = 0;
    private double multiplier = 1.0;
    private final TextColor[] colors;
    private @Range(from=-1L, to=1L) double phase;

    static Tag create(ArgumentQueue args, Context ctx) {
        List<TextColor> textColors;
        double phase = 0.0;
        if (args.hasNext()) {
            textColors = new ArrayList();
            while (args.hasNext()) {
                OptionalDouble possiblePhase;
                Tag.Argument arg = args.pop();
                if (!args.hasNext() && (possiblePhase = arg.asDouble()).isPresent()) {
                    phase = possiblePhase.getAsDouble();
                    if (!(phase < -1.0) && !(phase > 1.0)) break;
                    throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0, 1.0] (inclusive).", phase), args);
                }
                TextColor parsedColor = ColorTagResolver.resolveColor(arg.value(), ctx);
                textColors.add(parsedColor);
            }
            if (textColors.size() == 1) {
                throw ctx.newException("Invalid gradient, not enough colors. Gradients must have at least two colors.", args);
            }
        } else {
            textColors = Collections.emptyList();
        }
        return new GradientTag(phase, textColors);
    }

    private GradientTag(double phase, List<TextColor> colors) {
        this.colors = colors.isEmpty() ? new TextColor[]{TextColor.color(0xFFFFFF), TextColor.color(0)} : colors.toArray(new TextColor[0]);
        if (phase < 0.0) {
            this.phase = 1.0 + phase;
            Collections.reverse(Arrays.asList(this.colors));
        } else {
            this.phase = phase;
        }
    }

    @Override
    protected void init() {
        this.multiplier = this.size() == 1 ? 0.0 : (double)(this.colors.length - 1) / (double)(this.size() - 1);
        this.phase *= (double)(this.colors.length - 1);
        this.index = 0;
    }

    @Override
    protected void advanceColor() {
        ++this.index;
    }

    @Override
    protected TextColor color() {
        double position = (double)this.index * this.multiplier + this.phase;
        int lowUnclamped = (int)Math.floor(position);
        int high = (int)Math.ceil(position) % this.colors.length;
        int low = lowUnclamped % this.colors.length;
        return TextColor.lerp((float)position - (float)lowUnclamped, this.colors[low], this.colors[high]);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("phase", this.phase), ExaminableProperty.of("colors", this.colors));
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        GradientTag that = (GradientTag)other;
        return this.index == that.index && this.phase == that.phase && Arrays.equals(this.colors, that.colors);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.index, this.phase);
        result = 31 * result + Arrays.hashCode(this.colors);
        return result;
    }
}

