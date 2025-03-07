/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.lib.kyori.adventure.nbt;

import java.io.IOException;

class StringTagParseException
extends IOException {
    private static final long serialVersionUID = -3001637554903912905L;
    private final CharSequence buffer;
    private final int position;

    StringTagParseException(String message, CharSequence buffer, int position) {
        super(message);
        this.buffer = buffer;
        this.position = position;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + "(at position " + this.position + ")";
    }
}

