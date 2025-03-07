/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.Token;
import java.util.Arrays;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class ParsingExceptionImpl
extends ParsingException {
    private static final long serialVersionUID = 2507190809441787202L;
    private final String originalText;
    private Token @NotNull [] tokens;

    public ParsingExceptionImpl(String message, @Nullable String originalText, @NotNull @NotNull Token @NotNull ... tokens) {
        super(message, null, true, false);
        this.tokens = tokens;
        this.originalText = originalText;
    }

    public ParsingExceptionImpl(String message, @Nullable String originalText, @Nullable Throwable cause, boolean withStackTrace, @NotNull @NotNull Token @NotNull ... tokens) {
        super(message, cause, true, withStackTrace);
        this.tokens = tokens;
        this.originalText = originalText;
    }

    @Override
    public String getMessage() {
        String arrowInfo = this.tokens().length != 0 ? "\n\t" + this.arrow() : "";
        String messageInfo = this.originalText() != null ? "\n\t" + this.originalText() + arrowInfo : "";
        return super.getMessage() + messageInfo;
    }

    @Override
    @Nullable
    public String detailMessage() {
        return super.getMessage();
    }

    @Override
    @Nullable
    public String originalText() {
        return this.originalText;
    }

    @NotNull
    public @NotNull Token @NotNull [] tokens() {
        return this.tokens;
    }

    public void tokens(@NotNull @NotNull Token @NotNull [] tokens) {
        this.tokens = tokens;
    }

    private String arrow() {
        @NotNull Token[] ts = this.tokens();
        char[] chars = new char[ts[ts.length - 1].endIndex()];
        int i = 0;
        for (Token t : ts) {
            Arrays.fill(chars, i, t.startIndex(), ' ');
            chars[t.startIndex()] = 94;
            if (Math.abs(t.startIndex() - t.endIndex()) > 1) {
                Arrays.fill(chars, t.startIndex() + 1, t.endIndex() - 1, '~');
            }
            chars[t.endIndex() - 1] = 94;
            i = t.endIndex();
        }
        return new String(chars);
    }

    @Override
    public int startIndex() {
        if (this.tokens.length == 0) {
            return -1;
        }
        return this.tokens[0].startIndex();
    }

    @Override
    public int endIndex() {
        if (this.tokens.length == 0) {
            return -1;
        }
        return this.tokens[this.tokens.length - 1].endIndex();
    }
}

