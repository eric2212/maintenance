/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.TypeAdapter
 *  com.google.gson.TypeAdapterFactory
 *  com.google.gson.reflect.TypeToken
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.text.BlockNBTComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.ClickEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.HoverEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextDecoration;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.BlockNBTComponentPosSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.ClickEventActionSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.ComponentSerializerImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.HoverEventActionSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.KeySerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.ShowEntitySerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.ShowItemSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.StyleSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.TextColorSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.TextColorWrapper;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.TextDecorationSerializer;
import org.jetbrains.annotations.Nullable;

final class SerializerFactory
implements TypeAdapterFactory {
    static final Class<Key> KEY_TYPE = Key.class;
    static final Class<Component> COMPONENT_TYPE = Component.class;
    static final Class<Style> STYLE_TYPE = Style.class;
    static final Class<ClickEvent.Action> CLICK_ACTION_TYPE = ClickEvent.Action.class;
    static final Class<HoverEvent.Action> HOVER_ACTION_TYPE = HoverEvent.Action.class;
    static final Class<HoverEvent.ShowItem> SHOW_ITEM_TYPE = HoverEvent.ShowItem.class;
    static final Class<HoverEvent.ShowEntity> SHOW_ENTITY_TYPE = HoverEvent.ShowEntity.class;
    static final Class<TextColorWrapper> COLOR_WRAPPER_TYPE = TextColorWrapper.class;
    static final Class<TextColor> COLOR_TYPE = TextColor.class;
    static final Class<TextDecoration> TEXT_DECORATION_TYPE = TextDecoration.class;
    static final Class<BlockNBTComponent.Pos> BLOCK_NBT_POS_TYPE = BlockNBTComponent.Pos.class;
    private final boolean downsampleColors;
    private final LegacyHoverEventSerializer legacyHoverSerializer;
    private final boolean emitLegacyHover;

    SerializerFactory(boolean downsampleColors, @Nullable LegacyHoverEventSerializer legacyHoverSerializer, boolean emitLegacyHover) {
        this.downsampleColors = downsampleColors;
        this.legacyHoverSerializer = legacyHoverSerializer;
        this.emitLegacyHover = emitLegacyHover;
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class rawType = type.getRawType();
        if (COMPONENT_TYPE.isAssignableFrom(rawType)) {
            return ComponentSerializerImpl.create(gson);
        }
        if (KEY_TYPE.isAssignableFrom(rawType)) {
            return KeySerializer.INSTANCE;
        }
        if (STYLE_TYPE.isAssignableFrom(rawType)) {
            return StyleSerializer.create(this.legacyHoverSerializer, this.emitLegacyHover, gson);
        }
        if (CLICK_ACTION_TYPE.isAssignableFrom(rawType)) {
            return ClickEventActionSerializer.INSTANCE;
        }
        if (HOVER_ACTION_TYPE.isAssignableFrom(rawType)) {
            return HoverEventActionSerializer.INSTANCE;
        }
        if (SHOW_ITEM_TYPE.isAssignableFrom(rawType)) {
            return ShowItemSerializer.create(gson);
        }
        if (SHOW_ENTITY_TYPE.isAssignableFrom(rawType)) {
            return ShowEntitySerializer.create(gson);
        }
        if (COLOR_WRAPPER_TYPE.isAssignableFrom(rawType)) {
            return TextColorWrapper.Serializer.INSTANCE;
        }
        if (COLOR_TYPE.isAssignableFrom(rawType)) {
            return this.downsampleColors ? TextColorSerializer.DOWNSAMPLE_COLOR : TextColorSerializer.INSTANCE;
        }
        if (TEXT_DECORATION_TYPE.isAssignableFrom(rawType)) {
            return TextDecorationSerializer.INSTANCE;
        }
        if (BLOCK_NBT_POS_TYPE.isAssignableFrom(rawType)) {
            return BlockNBTComponentPosSerializer.INSTANCE;
        }
        return null;
    }
}

