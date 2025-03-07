/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.node;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.Token;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ValueNode
extends ElementNode {
    private final String value;

    ValueNode(@Nullable ElementNode parent, @Nullable Token token, @NotNull String sourceMessage, @NotNull String value) {
        super(parent, token, sourceMessage);
        this.value = value;
    }

    abstract String valueName();

    @NotNull
    public String value() {
        return this.value;
    }

    @Override
    @NotNull
    public Token token() {
        return Objects.requireNonNull(super.token(), "token is not set");
    }

    @Override
    @NotNull
    public StringBuilder buildToString(@NotNull StringBuilder sb, int indent) {
        char[] in = this.ident(indent);
        sb.append(in).append(this.valueName()).append("('").append(this.value).append("')\n");
        return sb;
    }
}

