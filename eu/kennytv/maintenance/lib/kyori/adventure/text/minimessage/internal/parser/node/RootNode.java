/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.node;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tree.Node;
import org.jetbrains.annotations.NotNull;

public final class RootNode
extends ElementNode
implements Node.Root {
    private final String beforePreprocessing;

    public RootNode(@NotNull String sourceMessage, @NotNull String beforePreprocessing) {
        super(null, null, sourceMessage);
        this.beforePreprocessing = beforePreprocessing;
    }

    @Override
    @NotNull
    public String input() {
        return this.beforePreprocessing;
    }
}

