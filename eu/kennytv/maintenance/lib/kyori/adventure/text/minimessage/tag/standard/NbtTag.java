/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard;

import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.text.BlockNBTComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.EntityNBTComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.NBTComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.NBTComponentBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.StorageNBTComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.Nullable;

final class NbtTag {
    private static final String NBT = "nbt";
    private static final String DATA = "data";
    private static final String BLOCK = "block";
    private static final String ENTITY = "entity";
    private static final String STORAGE = "storage";
    private static final String INTERPRET = "interpret";
    static final TagResolver RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("nbt", "data"), NbtTag::resolve, NbtTag::emit);

    private NbtTag() {
    }

    static Tag resolve(ArgumentQueue args, Context ctx) throws ParsingException {
        NBTComponentBuilder<BlockNBTComponent, BlockNBTComponent.Builder> builder;
        String type = args.popOr("a type of block, entity, or storage is required").lowerValue();
        if (BLOCK.equals(type)) {
            String pos = args.popOr("A position is required").value();
            try {
                builder = Component.blockNBT().pos(BlockNBTComponent.Pos.fromString(pos));
            } catch (IllegalArgumentException ex) {
                throw ctx.newException(ex.getMessage(), args);
            }
        } else if (ENTITY.equals(type)) {
            builder = Component.entityNBT().selector(args.popOr("A selector is required").value());
        } else if (STORAGE.equals(type)) {
            builder = Component.storageNBT().storage(Key.key(args.popOr("A storage key is required").value()));
        } else {
            throw ctx.newException("Unknown nbt tag type '" + type + "'", args);
        }
        builder.nbtPath(args.popOr("An NBT path is required").value());
        if (args.hasNext()) {
            String popped = args.pop().value();
            if (INTERPRET.equalsIgnoreCase(popped)) {
                builder.interpret(true);
            } else {
                builder.separator(ctx.deserialize(popped));
                if (args.hasNext() && args.pop().value().equalsIgnoreCase(INTERPRET)) {
                    builder.interpret(true);
                }
            }
        }
        return Tag.inserting((Component)builder.build());
    }

    @Nullable
    static Emitable emit(Component comp) {
        String id;
        String type;
        if (comp instanceof BlockNBTComponent) {
            type = BLOCK;
            id = ((BlockNBTComponent)comp).pos().asString();
        } else if (comp instanceof EntityNBTComponent) {
            type = ENTITY;
            id = ((EntityNBTComponent)comp).selector();
        } else if (comp instanceof StorageNBTComponent) {
            type = STORAGE;
            id = ((StorageNBTComponent)comp).storage().asString();
        } else {
            return null;
        }
        return out -> {
            NBTComponent nbt = (NBTComponent)comp;
            out.tag(NBT).argument(type).argument(id).argument(nbt.nbtPath());
            if (nbt.separator() != null) {
                out.argument(nbt.separator());
            }
            if (nbt.interpret()) {
                out.argument(INTERPRET);
            }
        };
    }
}

