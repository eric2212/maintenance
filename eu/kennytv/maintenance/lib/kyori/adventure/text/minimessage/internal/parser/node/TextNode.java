/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.node;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.Token;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TextNode
extends ValueNode {
    private static boolean isEscape(int escape) {
        return escape == 60 || escape == 92;
    }

    public TextNode(@Nullable ElementNode parent, @NotNull Token token, @NotNull String sourceMessage) {
        super(parent, token, sourceMessage, TokenParser.unescape(sourceMessage, token.startIndex(), token.endIndex(), TextNode::isEscape));
    }

    @Override
    String valueName() {
        return "TextNode";
    }
}

