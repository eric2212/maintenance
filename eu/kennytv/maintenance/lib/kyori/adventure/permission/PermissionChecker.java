/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.permission;

import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.permission.PermissionCheckers;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointer;
import eu.kennytv.maintenance.lib.kyori.adventure.util.TriState;
import java.util.Objects;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

public interface PermissionChecker
extends Predicate<String> {
    public static final Pointer<PermissionChecker> POINTER = Pointer.pointer(PermissionChecker.class, Key.key("adventure", "permission"));

    @NotNull
    public static PermissionChecker always(@NotNull TriState state) {
        Objects.requireNonNull(state);
        if (state == TriState.TRUE) {
            return PermissionCheckers.TRUE;
        }
        if (state == TriState.FALSE) {
            return PermissionCheckers.FALSE;
        }
        return PermissionCheckers.NOT_SET;
    }

    @NotNull
    public TriState value(@NotNull String var1);

    @Override
    default public boolean test(@NotNull String permission) {
        return this.value(permission) == TriState.TRUE;
    }
}

