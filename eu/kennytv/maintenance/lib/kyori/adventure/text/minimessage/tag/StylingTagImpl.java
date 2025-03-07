/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.StyleBuilderApplicable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.AbstractTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Inserting;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.Arrays;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class StylingTagImpl
extends AbstractTag
implements Inserting {
    private final StyleBuilderApplicable[] styles;

    StylingTagImpl(StyleBuilderApplicable[] styles) {
        this.styles = styles;
    }

    @Override
    @NotNull
    public Component value() {
        return Component.text("", Style.style(this.styles));
    }

    public int hashCode() {
        return 31 + Arrays.hashCode(this.styles);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StylingTagImpl)) {
            return false;
        }
        StylingTagImpl that = (StylingTagImpl)other;
        return Arrays.equals(this.styles, that.styles);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("styles", this.styles));
    }
}

