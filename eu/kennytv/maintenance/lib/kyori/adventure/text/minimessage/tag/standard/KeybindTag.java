/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.KeybindComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

final class KeybindTag {
    public static final String KEYBIND = "key";
    static final TagResolver RESOLVER = SerializableResolver.claimingComponent("key", KeybindTag::create, KeybindTag::emit);

    private KeybindTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        return Tag.inserting(Component.keybind(args.popOr("A keybind id is required").value()));
    }

    @Nullable
    static Emitable emit(Component component) {
        if (!(component instanceof KeybindComponent)) {
            return null;
        }
        String key = ((KeybindComponent)component).keybind();
        return emit -> emit.tag(KEYBIND).argument(key);
    }
}

