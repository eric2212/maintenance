/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

interface MappableResolver {
    public boolean contributeToMap(@NotNull Map<String, Tag> var1);
}

