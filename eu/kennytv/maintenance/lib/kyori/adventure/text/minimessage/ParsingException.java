/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public abstract class ParsingException
extends RuntimeException {
    private static final long serialVersionUID = 4502774670340827070L;
    public static final int LOCATION_UNKNOWN = -1;

    protected ParsingException(@Nullable String message) {
        super(message);
    }

    protected ParsingException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    protected ParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @NotNull
    public abstract String originalText();

    @Nullable
    public abstract String detailMessage();

    public abstract int startIndex();

    public abstract int endIndex();
}

