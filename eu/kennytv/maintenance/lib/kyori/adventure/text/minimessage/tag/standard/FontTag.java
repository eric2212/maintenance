/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.key.InvalidKeyException;
import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class FontTag {
    static final String FONT = "font";
    static final TagResolver RESOLVER = SerializableResolver.claimingStyle("font", FontTag::create, StyleClaim.claim("font", Style::font, FontTag::emit));

    private FontTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        Key font;
        String valueOrNamespace = args.popOr("A font tag must have either arguments of either <value> or <namespace:value>").value();
        try {
            if (!args.hasNext()) {
                font = Key.key(valueOrNamespace);
            } else {
                String fontKey = args.pop().value();
                font = Key.key(valueOrNamespace, fontKey);
            }
        } catch (InvalidKeyException ex) {
            throw ctx.newException(ex.getMessage(), args);
        }
        return Tag.styling(builder -> builder.font(font));
    }

    static void emit(Key font, TokenEmitter emitter) {
        emitter.tag(FONT);
        if (font.namespace().equals("minecraft")) {
            emitter.argument(font.value());
        } else {
            emitter.arguments(font.namespace(), font.value());
        }
    }
}

