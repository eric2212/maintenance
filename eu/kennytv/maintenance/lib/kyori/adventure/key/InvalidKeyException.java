/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.key;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class InvalidKeyException
extends RuntimeException {
    private static final long serialVersionUID = -5413304087321449434L;
    private final String keyNamespace;
    private final String keyValue;

    InvalidKeyException(@NotNull String keyNamespace, @NotNull String keyValue, @Nullable String message) {
        super(message);
        this.keyNamespace = keyNamespace;
        this.keyValue = keyValue;
    }

    @NotNull
    public final String keyNamespace() {
        return this.keyNamespace;
    }

    @NotNull
    public final String keyValue() {
        return this.keyValue;
    }
}

