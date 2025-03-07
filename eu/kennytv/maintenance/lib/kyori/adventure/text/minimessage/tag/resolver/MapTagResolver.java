/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.MappableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class MapTagResolver
implements TagResolver.WithoutArguments,
MappableResolver {
    private final Map<String, ? extends Tag> tagMap;

    MapTagResolver(@NotNull Map<String, ? extends Tag> placeholderMap) {
        this.tagMap = placeholderMap;
    }

    @Override
    @Nullable
    public Tag resolve(@NotNull String name) {
        return this.tagMap.get(name);
    }

    @Override
    public boolean contributeToMap(@NotNull Map<String, Tag> map) {
        map.putAll(this.tagMap);
        return true;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MapTagResolver)) {
            return false;
        }
        MapTagResolver that = (MapTagResolver)other;
        return Objects.equals(this.tagMap, that.tagMap);
    }

    public int hashCode() {
        return Objects.hash(this.tagMap);
    }
}

