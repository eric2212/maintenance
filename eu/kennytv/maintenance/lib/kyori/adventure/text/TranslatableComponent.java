/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text;

import eu.kennytv.maintenance.lib.kyori.adventure.text.BuildableComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ScopedComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TranslationArgument;
import eu.kennytv.maintenance.lib.kyori.adventure.translation.Translatable;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TranslatableComponent
extends BuildableComponent<TranslatableComponent, Builder>,
ScopedComponent<TranslatableComponent> {
    @NotNull
    public String key();

    @Contract(pure=true)
    @NotNull
    default public TranslatableComponent key(@NotNull Translatable translatable) {
        return this.key(Objects.requireNonNull(translatable, "translatable").translationKey());
    }

    @Contract(pure=true)
    @NotNull
    public TranslatableComponent key(@NotNull String var1);

    @Deprecated
    @NotNull
    public List<Component> args();

    @Deprecated
    @Contract(pure=true)
    @NotNull
    default public TranslatableComponent args(@NotNull @NotNull ComponentLike @NotNull ... args) {
        return this.arguments(args);
    }

    @Deprecated
    @Contract(pure=true)
    @NotNull
    default public TranslatableComponent args(@NotNull List<? extends ComponentLike> args) {
        return this.arguments(args);
    }

    @NotNull
    public List<TranslationArgument> arguments();

    @Contract(pure=true)
    @NotNull
    public TranslatableComponent arguments(@NotNull @NotNull ComponentLike @NotNull ... var1);

    @Contract(pure=true)
    @NotNull
    public TranslatableComponent arguments(@NotNull List<? extends ComponentLike> var1);

    @Nullable
    public String fallback();

    @Contract(pure=true)
    @NotNull
    public TranslatableComponent fallback(@Nullable String var1);

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("key", this.key()), ExaminableProperty.of("arguments", this.arguments()), ExaminableProperty.of("fallback", this.fallback())), BuildableComponent.super.examinableProperties());
    }

    public static interface Builder
    extends ComponentBuilder<TranslatableComponent, Builder> {
        @Contract(pure=true)
        @NotNull
        default public Builder key(@NotNull Translatable translatable) {
            return this.key(Objects.requireNonNull(translatable, "translatable").translationKey());
        }

        @Contract(value="_ -> this")
        @NotNull
        public Builder key(@NotNull String var1);

        @Deprecated
        @Contract(value="_ -> this")
        @NotNull
        default public Builder args(@NotNull ComponentBuilder<?, ?> arg) {
            return this.arguments(arg);
        }

        @Deprecated
        @Contract(value="_ -> this")
        @NotNull
        default public Builder args(@NotNull @NotNull ComponentBuilder<?, ?> @NotNull ... args) {
            return this.arguments(args);
        }

        @Deprecated
        @Contract(value="_ -> this")
        @NotNull
        default public Builder args(@NotNull Component arg) {
            return this.arguments(arg);
        }

        @Deprecated
        @Contract(value="_ -> this")
        @NotNull
        default public Builder args(@NotNull @NotNull ComponentLike @NotNull ... args) {
            return this.arguments(args);
        }

        @Deprecated
        @Contract(value="_ -> this")
        @NotNull
        default public Builder args(@NotNull List<? extends ComponentLike> args) {
            return this.arguments(args);
        }

        @Contract(value="_ -> this")
        @NotNull
        public Builder arguments(@NotNull @NotNull ComponentLike @NotNull ... var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder arguments(@NotNull List<? extends ComponentLike> var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder fallback(@Nullable String var1);
    }
}

