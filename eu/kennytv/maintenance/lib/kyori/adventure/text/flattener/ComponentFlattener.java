/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.flattener;

import eu.kennytv.maintenance.lib.kyori.adventure.builder.AbstractBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.flattener.ComponentFlattenerImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.flattener.FlattenerListener;
import eu.kennytv.maintenance.lib.kyori.adventure.util.Buildable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ComponentFlattener
extends Buildable<ComponentFlattener, Builder> {
    @NotNull
    public static Builder builder() {
        return new ComponentFlattenerImpl.BuilderImpl();
    }

    @NotNull
    public static ComponentFlattener basic() {
        return ComponentFlattenerImpl.BASIC;
    }

    @NotNull
    public static ComponentFlattener textOnly() {
        return ComponentFlattenerImpl.TEXT_ONLY;
    }

    public void flatten(@NotNull Component var1, @NotNull FlattenerListener var2);

    public static interface Builder
    extends AbstractBuilder<ComponentFlattener>,
    Buildable.Builder<ComponentFlattener> {
        @NotNull
        public <T extends Component> Builder mapper(@NotNull Class<T> var1, @NotNull Function<T, String> var2);

        @NotNull
        public <T extends Component> Builder complexMapper(@NotNull Class<T> var1, @NotNull BiConsumer<T, Consumer<Component>> var2);

        @NotNull
        public Builder unknownMapper(@Nullable Function<Component, String> var1);
    }
}

