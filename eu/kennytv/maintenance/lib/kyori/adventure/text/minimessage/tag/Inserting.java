/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.NotNull;

public interface Inserting
extends Tag {
    @NotNull
    public Component value();

    default public boolean allowsChildren() {
        return true;
    }
}

