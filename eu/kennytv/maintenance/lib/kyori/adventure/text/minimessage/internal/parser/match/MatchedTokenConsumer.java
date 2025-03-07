/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.MustBeInvokedByOverriders
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.UnknownNullability
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.match;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.TokenType;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public abstract class MatchedTokenConsumer<T> {
    protected final String input;
    private int lastIndex = -1;

    public MatchedTokenConsumer(@NotNull String input) {
        this.input = input;
    }

    @MustBeInvokedByOverriders
    public void accept(int start, int end, @NotNull TokenType tokenType) {
        this.lastIndex = end;
    }

    public abstract @UnknownNullability T result();

    public final int lastEndIndex() {
        return this.lastIndex;
    }
}

