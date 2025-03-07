/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.platform;

import eu.kennytv.maintenance.lib.kyori.adventure.audience.Audience;
import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointered;
import eu.kennytv.maintenance.lib.kyori.adventure.text.flattener.ComponentFlattener;
import eu.kennytv.maintenance.lib.kyori.adventure.text.renderer.ComponentRenderer;
import java.util.UUID;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public interface AudienceProvider
extends AutoCloseable {
    @NotNull
    public Audience all();

    @NotNull
    public Audience console();

    @NotNull
    public Audience players();

    @NotNull
    public Audience player(@NotNull UUID var1);

    @NotNull
    default public Audience permission(@NotNull Key permission) {
        return this.permission(permission.namespace() + '.' + permission.value());
    }

    @NotNull
    public Audience permission(@NotNull String var1);

    @NotNull
    public Audience world(@NotNull Key var1);

    @NotNull
    public Audience server(@NotNull String var1);

    @NotNull
    public ComponentFlattener flattener();

    @Override
    public void close();

    public static interface Builder<P extends AudienceProvider, B extends Builder<P, B>> {
        @NotNull
        public B componentRenderer(@NotNull ComponentRenderer<Pointered> var1);

        @NotNull
        public B partition(@NotNull Function<Pointered, ?> var1);

        @NotNull
        default public <T> B componentRenderer(@NotNull Function<Pointered, T> partition, @NotNull ComponentRenderer<T> componentRenderer) {
            return this.partition(partition).componentRenderer(componentRenderer.mapContext(partition));
        }

        @NotNull
        public P build();
    }
}

