/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.renderer;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public interface ComponentRenderer<C> {
    @NotNull
    public Component render(@NotNull Component var1, @NotNull C var2);

    default public <T> ComponentRenderer<T> mapContext(Function<T, C> transformer) {
        return (component, ctx) -> this.render(component, transformer.apply(ctx));
    }
}

