/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package eu.kennytv.maintenance.lib.kyori.adventure.platform.facet;

import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class FacetPointers {
    private static final String NAMESPACE = "adventure_platform";
    public static final Pointer<String> SERVER = Pointer.pointer(String.class, Key.key("adventure_platform", "server"));
    public static final Pointer<Key> WORLD = Pointer.pointer(Key.class, Key.key("adventure_platform", "world"));
    public static final Pointer<Type> TYPE = Pointer.pointer(Type.class, Key.key("adventure_platform", "type"));

    private FacetPointers() {
    }

    public static enum Type {
        PLAYER,
        CONSOLE,
        OTHER;

    }
}

