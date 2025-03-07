/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import org.jetbrains.annotations.NotNull;

public interface ClaimConsumer {
    public void style(@NotNull String var1, @NotNull Emitable var2);

    public boolean component(@NotNull Emitable var1);

    public boolean styleClaimed(@NotNull String var1);

    public boolean componentClaimed();
}

