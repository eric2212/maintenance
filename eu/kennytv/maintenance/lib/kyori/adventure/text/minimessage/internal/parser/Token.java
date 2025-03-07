/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser;

import eu.kennytv.maintenance.lib.kyori.adventure.internal.Internals;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.TokenType;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public final class Token
implements Examinable {
    private final int startIndex;
    private final int endIndex;
    private final TokenType type;
    private List<Token> childTokens = null;

    public Token(int startIndex, int endIndex, TokenType type) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.type = type;
    }

    public int startIndex() {
        return this.startIndex;
    }

    public int endIndex() {
        return this.endIndex;
    }

    public TokenType type() {
        return this.type;
    }

    public List<Token> childTokens() {
        return this.childTokens;
    }

    public void childTokens(List<Token> childTokens) {
        this.childTokens = childTokens;
    }

    public CharSequence get(CharSequence message) {
        return message.subSequence(this.startIndex, this.endIndex);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("startIndex", this.startIndex), ExaminableProperty.of("endIndex", this.endIndex), ExaminableProperty.of("type", (Object)this.type));
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Token)) {
            return false;
        }
        Token that = (Token)other;
        return this.startIndex == that.startIndex && this.endIndex == that.endIndex && this.type == that.type;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.startIndex, this.endIndex, this.type});
    }

    public String toString() {
        return Internals.toString(this);
    }
}

