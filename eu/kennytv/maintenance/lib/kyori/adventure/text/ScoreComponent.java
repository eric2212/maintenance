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
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ScopedComponent;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ScoreComponent
extends BuildableComponent<ScoreComponent, Builder>,
ScopedComponent<ScoreComponent> {
    @NotNull
    public String name();

    @Contract(pure=true)
    @NotNull
    public ScoreComponent name(@NotNull String var1);

    @NotNull
    public String objective();

    @Contract(pure=true)
    @NotNull
    public ScoreComponent objective(@NotNull String var1);

    @Deprecated
    @Nullable
    public String value();

    @Deprecated
    @Contract(pure=true)
    @NotNull
    public ScoreComponent value(@Nullable String var1);

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("name", this.name()), ExaminableProperty.of("objective", this.objective()), ExaminableProperty.of("value", this.value())), BuildableComponent.super.examinableProperties());
    }

    public static interface Builder
    extends ComponentBuilder<ScoreComponent, Builder> {
        @Contract(value="_ -> this")
        @NotNull
        public Builder name(@NotNull String var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder objective(@NotNull String var1);

        @Deprecated
        @Contract(value="_ -> this")
        @NotNull
        public Builder value(@Nullable String var1);
    }
}

