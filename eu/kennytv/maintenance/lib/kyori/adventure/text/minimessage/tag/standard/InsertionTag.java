/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class InsertionTag {
    private static final String INSERTION = "insert";
    static final TagResolver RESOLVER = SerializableResolver.claimingStyle("insert", InsertionTag::create, StyleClaim.claim("insert", Style::insertion, InsertionTag::emit));

    private InsertionTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        String insertion = args.popOr("A value is required to produce an insertion component").value();
        return Tag.styling(b -> b.insertion(insertion));
    }

    static void emit(String insertion, TokenEmitter emitter) {
        emitter.tag(INSERTION).argument(insertion);
    }
}

