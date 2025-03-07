/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.StyleBuilderApplicable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.CallbackStylingTagImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.InsertingImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.PreProcess;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.PreProcessTagImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.StylingTagImpl;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface Tag {
    @NotNull
    public static PreProcess preProcessParsed(@NotNull String content) {
        return new PreProcessTagImpl(Objects.requireNonNull(content, "content"));
    }

    @NotNull
    public static Tag inserting(@NotNull Component content) {
        return new InsertingImpl(true, Objects.requireNonNull(content, "content must not be null"));
    }

    @NotNull
    public static Tag inserting(@NotNull ComponentLike value) {
        return Tag.inserting(Objects.requireNonNull(value, "value").asComponent());
    }

    @NotNull
    public static Tag selfClosingInserting(@NotNull Component content) {
        return new InsertingImpl(false, Objects.requireNonNull(content, "content must not be null"));
    }

    @NotNull
    public static Tag selfClosingInserting(@NotNull ComponentLike value) {
        return Tag.selfClosingInserting(Objects.requireNonNull(value, "value").asComponent());
    }

    @NotNull
    public static Tag styling(Consumer<Style.Builder> styles) {
        return new CallbackStylingTagImpl(styles);
    }

    @NotNull
    public static Tag styling(@NotNull @NotNull StyleBuilderApplicable @NotNull ... actions) {
        Objects.requireNonNull(actions, "actions");
        int length = actions.length;
        for (int i = 0; i < length; ++i) {
            if (actions[i] != null) continue;
            throw new NullPointerException("actions[" + i + "]");
        }
        return new StylingTagImpl(Arrays.copyOf(actions, actions.length));
    }

    @ApiStatus.NonExtendable
    public static interface Argument {
        @NotNull
        public String value();

        @NotNull
        default public String lowerValue() {
            return this.value().toLowerCase(Locale.ROOT);
        }

        default public boolean isTrue() {
            return "true".equals(this.value()) || "on".equals(this.value());
        }

        default public boolean isFalse() {
            return "false".equals(this.value()) || "off".equals(this.value());
        }

        @NotNull
        default public OptionalInt asInt() {
            try {
                return OptionalInt.of(Integer.parseInt(this.value()));
            } catch (NumberFormatException ex) {
                return OptionalInt.empty();
            }
        }

        @NotNull
        default public OptionalDouble asDouble() {
            try {
                return OptionalDouble.of(Double.parseDouble(this.value()));
            } catch (NumberFormatException ex) {
                return OptionalDouble.empty();
            }
        }
    }
}

