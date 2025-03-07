/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TranslatableComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TranslationArgument;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;

final class TranslatableTag {
    private static final String TR = "tr";
    private static final String TRANSLATE = "translate";
    private static final String LANG = "lang";
    static final TagResolver RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("lang", "translate", "tr"), TranslatableTag::create, TranslatableTag::claim);

    private TranslatableTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        List with;
        String key = args.popOr("A translation key is required").value();
        if (args.hasNext()) {
            with = new ArrayList();
            while (args.hasNext()) {
                with.add(ctx.deserialize(args.pop().value()));
            }
        } else {
            with = Collections.emptyList();
        }
        return Tag.inserting(Component.translatable(key, with));
    }

    @Nullable
    static Emitable claim(Component input) {
        if (!(input instanceof TranslatableComponent) || ((TranslatableComponent)input).fallback() != null) {
            return null;
        }
        TranslatableComponent tr = (TranslatableComponent)input;
        return emit -> {
            emit.tag(LANG);
            emit.argument(tr.key());
            for (TranslationArgument with : tr.arguments()) {
                emit.argument(with.asComponent());
            }
        };
    }
}

