/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.ParserDirective;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class ResetTag {
    private static final String RESET = "reset";
    static final TagResolver RESOLVER = TagResolver.resolver("reset", ParserDirective.RESET);

    private ResetTag() {
    }
}

