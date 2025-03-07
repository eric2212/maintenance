/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.MappableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

final class SingleResolver
implements TagResolver.Single,
MappableResolver {
    private final String key;
    private final Tag tag;

    SingleResolver(String key, Tag tag) {
        this.key = key;
        this.tag = tag;
    }

    @Override
    @NotNull
    public String key() {
        return this.key;
    }

    @Override
    @NotNull
    public Tag tag() {
        return this.tag;
    }

    @Override
    public boolean contributeToMap(@NotNull Map<String, Tag> map) {
        map.put(this.key, this.tag);
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.key, this.tag);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        SingleResolver that = (SingleResolver)other;
        return Objects.equals(this.key, that.key) && Objects.equals(this.tag, that.tag);
    }
}

