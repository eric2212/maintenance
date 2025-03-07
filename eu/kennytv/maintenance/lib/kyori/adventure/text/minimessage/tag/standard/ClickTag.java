/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.event.ClickEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.QuotingOverride;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

final class ClickTag {
    private static final String CLICK = "click";
    static final TagResolver RESOLVER = SerializableResolver.claimingStyle("click", ClickTag::create, StyleClaim.claim("click", Style::clickEvent, (event, emitter) -> emitter.tag(CLICK).argument(ClickEvent.Action.NAMES.key(event.action())).argument(event.value(), QuotingOverride.QUOTED)));

    private ClickTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        String actionName = args.popOr(() -> "A click tag requires an action of one of " + ClickEvent.Action.NAMES.keys()).lowerValue();
        @Nullable ClickEvent.Action action = ClickEvent.Action.NAMES.value(actionName);
        if (action == null) {
            throw ctx.newException("Unknown click event action '" + actionName + "'", args);
        }
        String value = args.popOr("Click event actions require a value").value();
        return Tag.styling(ClickEvent.clickEvent(action, value));
    }
}

