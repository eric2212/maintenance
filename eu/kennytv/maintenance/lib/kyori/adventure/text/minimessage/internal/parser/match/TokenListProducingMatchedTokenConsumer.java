/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.match;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.Token;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.TokenType;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.match.MatchedTokenConsumer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class TokenListProducingMatchedTokenConsumer
extends MatchedTokenConsumer<List<Token>> {
    private List<Token> result = null;

    public TokenListProducingMatchedTokenConsumer(@NotNull String input) {
        super(input);
    }

    @Override
    public void accept(int start, int end, @NotNull TokenType tokenType) {
        super.accept(start, end, tokenType);
        if (this.result == null) {
            this.result = new ArrayList<Token>();
        }
        this.result.add(new Token(start, end, tokenType));
    }

    @Override
    @NotNull
    public List<Token> result() {
        return this.result == null ? Collections.emptyList() : this.result;
    }
}

