/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ScoreComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

final class ScoreTag {
    public static final String SCORE = "score";
    static final TagResolver RESOLVER = SerializableResolver.claimingComponent("score", ScoreTag::create, ScoreTag::emit);

    private ScoreTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        String name = args.popOr("A scoreboard member name is required").value();
        String objective = args.popOr("An objective name is required").value();
        return Tag.inserting(Component.score(name, objective));
    }

    @Nullable
    static Emitable emit(Component component) {
        if (!(component instanceof ScoreComponent)) {
            return null;
        }
        ScoreComponent score = (ScoreComponent)component;
        return emit -> emit.tag(SCORE).argument(score.name()).argument(score.objective());
    }
}

