/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface ArgumentQueue {
    public @NotNull Tag.Argument pop();

    public @NotNull Tag.Argument popOr(@NotNull String var1);

    public @NotNull Tag.Argument popOr(@NotNull Supplier<String> var1);

    public @Nullable Tag.Argument peek();

    public boolean hasNext();

    public void reset();
}

