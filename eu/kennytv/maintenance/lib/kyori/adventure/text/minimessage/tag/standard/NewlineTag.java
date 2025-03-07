/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.Nullable;

final class NewlineTag {
    private static final String BR = "br";
    private static final String NEWLINE = "newline";
    static final TagResolver RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("newline", "br"), NewlineTag::create, NewlineTag::claimComponent);

    private NewlineTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        return Tag.selfClosingInserting(Component.newline());
    }

    @Nullable
    static Emitable claimComponent(Component input) {
        if (Component.newline().equals(input)) {
            return emit -> emit.selfClosingTag(BR);
        }
        return null;
    }
}

