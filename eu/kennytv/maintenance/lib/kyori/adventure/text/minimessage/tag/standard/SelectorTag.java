/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.SelectorComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.Nullable;

final class SelectorTag {
    private static final String SEL = "sel";
    private static final String SELECTOR = "selector";
    static final TagResolver RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("sel", "selector"), SelectorTag::create, SelectorTag::claim);

    private SelectorTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        String key = args.popOr("A selection key is required").value();
        Component separator = null;
        if (args.hasNext()) {
            separator = ctx.deserialize(args.pop().value());
        }
        return Tag.inserting(Component.selector(key, separator));
    }

    @Nullable
    static Emitable claim(Component input) {
        if (!(input instanceof SelectorComponent)) {
            return null;
        }
        SelectorComponent st = (SelectorComponent)input;
        return emit -> {
            emit.tag(SEL);
            emit.argument(st.pattern());
            if (st.separator() != null) {
                emit.argument(st.separator());
            }
        };
    }
}

