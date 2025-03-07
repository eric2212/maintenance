/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Emitable {
    public void emit(@NotNull TokenEmitter var1);
}

