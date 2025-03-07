/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage;

import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointered;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface Context {
    @Nullable
    public Pointered target();

    @NotNull
    public Pointered targetOrThrow();

    @NotNull
    public <T extends Pointered> T targetAsType(@NotNull Class<T> var1);

    @NotNull
    public Component deserialize(@NotNull String var1);

    @NotNull
    public Component deserialize(@NotNull String var1, @NotNull TagResolver var2);

    @NotNull
    public Component deserialize(@NotNull String var1, @NotNull @NotNull TagResolver @NotNull ... var2);

    @NotNull
    public ParsingException newException(@NotNull String var1, @NotNull ArgumentQueue var2);

    @NotNull
    public ParsingException newException(@NotNull String var1);

    @NotNull
    public ParsingException newException(@NotNull String var1, @Nullable Throwable var2, @NotNull ArgumentQueue var3);
}

