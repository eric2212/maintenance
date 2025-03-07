/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.identity;

import eu.kennytv.maintenance.lib.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;

public interface Identified {
    @NotNull
    public Identity identity();
}

