/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.AbstractTag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Inserting;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

final class CallbackStylingTagImpl
extends AbstractTag
implements Inserting {
    private final Consumer<Style.Builder> styles;

    CallbackStylingTagImpl(Consumer<Style.Builder> styles) {
        this.styles = styles;
    }

    @Override
    @NotNull
    public Component value() {
        return Component.text("", Style.style(this.styles));
    }

    public int hashCode() {
        return Objects.hash(this.styles);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CallbackStylingTagImpl)) {
            return false;
        }
        CallbackStylingTagImpl that = (CallbackStylingTagImpl)other;
        return Objects.equals(this.styles, that.styles);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("styles", this.styles));
    }
}

