/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Range
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.event;

import eu.kennytv.maintenance.lib.kyori.adventure.internal.Internals;
import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.key.Keyed;
import eu.kennytv.maintenance.lib.kyori.adventure.nbt.api.BinaryTagHolder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.DataComponentValue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.DataComponentValueConverterRegistry;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.HoverEventSource;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.StyleBuilderApplicable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.renderer.ComponentRenderer;
import eu.kennytv.maintenance.lib.kyori.adventure.util.Index;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public final class HoverEvent<V>
implements Examinable,
HoverEventSource<V>,
StyleBuilderApplicable {
    private final Action<V> action;
    private final V value;

    @NotNull
    public static HoverEvent<Component> showText(@NotNull ComponentLike text) {
        return HoverEvent.showText(text.asComponent());
    }

    @NotNull
    public static HoverEvent<Component> showText(@NotNull Component text) {
        return new HoverEvent<Component>(Action.SHOW_TEXT, text);
    }

    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull Key item, @Range(from=0L, to=0x7FFFFFFFL) int count) {
        return HoverEvent.showItem((Keyed)item, count, Collections.emptyMap());
    }

    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull Keyed item, @Range(from=0L, to=0x7FFFFFFFL) int count) {
        return HoverEvent.showItem(item, count, Collections.emptyMap());
    }

    @Deprecated
    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull Key item, @Range(from=0L, to=0x7FFFFFFFL) int count, @Nullable BinaryTagHolder nbt) {
        return HoverEvent.showItem(ShowItem.showItem(item, count, nbt));
    }

    @Deprecated
    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull Keyed item, @Range(from=0L, to=0x7FFFFFFFL) int count, @Nullable BinaryTagHolder nbt) {
        return HoverEvent.showItem(ShowItem.showItem(item, count, nbt));
    }

    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull Keyed item, @Range(from=0L, to=0x7FFFFFFFL) int count, @NotNull Map<Key, ? extends DataComponentValue> dataComponents) {
        return HoverEvent.showItem(ShowItem.showItem(item, count, dataComponents));
    }

    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull ShowItem item) {
        return new HoverEvent<ShowItem>(Action.SHOW_ITEM, item);
    }

    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull Key type, @NotNull UUID id) {
        return HoverEvent.showEntity(type, id, null);
    }

    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull Keyed type, @NotNull UUID id) {
        return HoverEvent.showEntity(type, id, null);
    }

    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull Key type, @NotNull UUID id, @Nullable Component name) {
        return HoverEvent.showEntity(ShowEntity.of(type, id, name));
    }

    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull Keyed type, @NotNull UUID id, @Nullable Component name) {
        return HoverEvent.showEntity(ShowEntity.of(type, id, name));
    }

    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull ShowEntity entity) {
        return new HoverEvent<ShowEntity>(Action.SHOW_ENTITY, entity);
    }

    @Deprecated
    @NotNull
    public static HoverEvent<String> showAchievement(@NotNull String value) {
        return new HoverEvent<String>(Action.SHOW_ACHIEVEMENT, value);
    }

    @NotNull
    public static <V> HoverEvent<V> hoverEvent(@NotNull Action<V> action, @NotNull V value) {
        return new HoverEvent<V>(action, value);
    }

    private HoverEvent(@NotNull Action<V> action, @NotNull V value) {
        this.action = Objects.requireNonNull(action, "action");
        this.value = Objects.requireNonNull(value, "value");
    }

    @NotNull
    public Action<V> action() {
        return this.action;
    }

    @NotNull
    public V value() {
        return this.value;
    }

    @NotNull
    public HoverEvent<V> value(@NotNull V value) {
        return new HoverEvent<V>(this.action, value);
    }

    @NotNull
    public <C> HoverEvent<V> withRenderedValue(@NotNull ComponentRenderer<C> renderer, @NotNull C context) {
        V oldValue = this.value;
        V newValue = ((Action)this.action).renderer.render(renderer, context, oldValue);
        if (newValue != oldValue) {
            return new HoverEvent<V>(this.action, newValue);
        }
        return this;
    }

    @Override
    @NotNull
    public HoverEvent<V> asHoverEvent() {
        return this;
    }

    @Override
    @NotNull
    public HoverEvent<V> asHoverEvent(@NotNull UnaryOperator<V> op) {
        if (op == UnaryOperator.identity()) {
            return this;
        }
        return new HoverEvent<V>(this.action, op.apply(this.value));
    }

    @Override
    public void styleApply( @NotNull Style.Builder style) {
        style.hoverEvent((HoverEventSource)this);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        HoverEvent that = (HoverEvent)other;
        return this.action == that.action && this.value.equals(that.value);
    }

    public int hashCode() {
        int result = this.action.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("action", this.action), ExaminableProperty.of("value", this.value));
    }

    public String toString() {
        return Internals.toString(this);
    }

    public static final class Action<V> {
        public static final Action<Component> SHOW_TEXT = new Action<Component>("show_text", Component.class, true, new Renderer<Component>(){

            @Override
            @NotNull
            public <C> Component render(@NotNull ComponentRenderer<C> renderer, @NotNull C context, @NotNull Component value) {
                return renderer.render(value, context);
            }
        });
        public static final Action<ShowItem> SHOW_ITEM = new Action<ShowItem>("show_item", ShowItem.class, true, new Renderer<ShowItem>(){

            @Override
            @NotNull
            public <C> ShowItem render(@NotNull ComponentRenderer<C> renderer, @NotNull C context, @NotNull ShowItem value) {
                return value;
            }
        });
        public static final Action<ShowEntity> SHOW_ENTITY = new Action<ShowEntity>("show_entity", ShowEntity.class, true, new Renderer<ShowEntity>(){

            @Override
            @NotNull
            public <C> ShowEntity render(@NotNull ComponentRenderer<C> renderer, @NotNull C context, @NotNull ShowEntity value) {
                if (value.name == null) {
                    return value;
                }
                return value.name(renderer.render(value.name, context));
            }
        });
        @Deprecated
        public static final Action<String> SHOW_ACHIEVEMENT = new Action<String>("show_achievement", String.class, true, new Renderer<String>(){

            @Override
            @NotNull
            public <C> String render(@NotNull ComponentRenderer<C> renderer, @NotNull C context, @NotNull String value) {
                return value;
            }
        });
        public static final Index<String, Action<?>> NAMES = Index.create(constant -> constant.name, SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY, SHOW_ACHIEVEMENT);
        private final String name;
        private final Class<V> type;
        private final boolean readable;
        private final Renderer<V> renderer;

        Action(String name, Class<V> type, boolean readable, Renderer<V> renderer) {
            this.name = name;
            this.type = type;
            this.readable = readable;
            this.renderer = renderer;
        }

        @NotNull
        public Class<V> type() {
            return this.type;
        }

        public boolean readable() {
            return this.readable;
        }

        @NotNull
        public String toString() {
            return this.name;
        }

        @FunctionalInterface
        static interface Renderer<V> {
            @NotNull
            public <C> V render(@NotNull ComponentRenderer<C> var1, @NotNull C var2, @NotNull V var3);
        }
    }

    public static final class ShowEntity
    implements Examinable {
        private final Key type;
        private final UUID id;
        private final Component name;

        @NotNull
        public static ShowEntity showEntity(@NotNull Key type, @NotNull UUID id) {
            return ShowEntity.showEntity(type, id, null);
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowEntity of(@NotNull Key type, @NotNull UUID id) {
            return ShowEntity.of(type, id, null);
        }

        @NotNull
        public static ShowEntity showEntity(@NotNull Keyed type, @NotNull UUID id) {
            return ShowEntity.showEntity(type, id, null);
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowEntity of(@NotNull Keyed type, @NotNull UUID id) {
            return ShowEntity.of(type, id, null);
        }

        @NotNull
        public static ShowEntity showEntity(@NotNull Key type, @NotNull UUID id, @Nullable Component name) {
            return new ShowEntity(Objects.requireNonNull(type, "type"), Objects.requireNonNull(id, "id"), name);
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowEntity of(@NotNull Key type, @NotNull UUID id, @Nullable Component name) {
            return new ShowEntity(Objects.requireNonNull(type, "type"), Objects.requireNonNull(id, "id"), name);
        }

        @NotNull
        public static ShowEntity showEntity(@NotNull Keyed type, @NotNull UUID id, @Nullable Component name) {
            return new ShowEntity(Objects.requireNonNull(type, "type").key(), Objects.requireNonNull(id, "id"), name);
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowEntity of(@NotNull Keyed type, @NotNull UUID id, @Nullable Component name) {
            return new ShowEntity(Objects.requireNonNull(type, "type").key(), Objects.requireNonNull(id, "id"), name);
        }

        private ShowEntity(@NotNull Key type, @NotNull UUID id, @Nullable Component name) {
            this.type = type;
            this.id = id;
            this.name = name;
        }

        @NotNull
        public Key type() {
            return this.type;
        }

        @NotNull
        public ShowEntity type(@NotNull Key type) {
            if (Objects.requireNonNull(type, "type").equals(this.type)) {
                return this;
            }
            return new ShowEntity(type, this.id, this.name);
        }

        @NotNull
        public ShowEntity type(@NotNull Keyed type) {
            return this.type(Objects.requireNonNull(type, "type").key());
        }

        @NotNull
        public UUID id() {
            return this.id;
        }

        @NotNull
        public ShowEntity id(@NotNull UUID id) {
            if (Objects.requireNonNull(id).equals(this.id)) {
                return this;
            }
            return new ShowEntity(this.type, id, this.name);
        }

        @Nullable
        public Component name() {
            return this.name;
        }

        @NotNull
        public ShowEntity name(@Nullable Component name) {
            if (Objects.equals(name, this.name)) {
                return this;
            }
            return new ShowEntity(this.type, this.id, name);
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || this.getClass() != other.getClass()) {
                return false;
            }
            ShowEntity that = (ShowEntity)other;
            return this.type.equals(that.type) && this.id.equals(that.id) && Objects.equals(this.name, that.name);
        }

        public int hashCode() {
            int result = this.type.hashCode();
            result = 31 * result + this.id.hashCode();
            result = 31 * result + Objects.hashCode(this.name);
            return result;
        }

        @Override
        @NotNull
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("type", this.type), ExaminableProperty.of("id", this.id), ExaminableProperty.of("name", this.name));
        }

        public String toString() {
            return Internals.toString(this);
        }
    }

    public static final class ShowItem
    implements Examinable {
        private final Key item;
        private final int count;
        @Nullable
        private final BinaryTagHolder nbt;
        private final Map<Key, DataComponentValue> dataComponents;

        @NotNull
        public static ShowItem showItem(@NotNull Key item, @Range(from=0L, to=0x7FFFFFFFL) int count) {
            return ShowItem.showItem((Keyed)item, count, Collections.emptyMap());
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowItem of(@NotNull Key item, @Range(from=0L, to=0x7FFFFFFFL) int count) {
            return ShowItem.showItem((Keyed)item, count, Collections.emptyMap());
        }

        @NotNull
        public static ShowItem showItem(@NotNull Keyed item, @Range(from=0L, to=0x7FFFFFFFL) int count) {
            return ShowItem.showItem(item, count, Collections.emptyMap());
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowItem of(@NotNull Keyed item, @Range(from=0L, to=0x7FFFFFFFL) int count) {
            return ShowItem.of(item, count, null);
        }

        @Deprecated
        @NotNull
        public static ShowItem showItem(@NotNull Key item, @Range(from=0L, to=0x7FFFFFFFL) int count, @Nullable BinaryTagHolder nbt) {
            return new ShowItem(Objects.requireNonNull(item, "item"), count, nbt, Collections.emptyMap());
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowItem of(@NotNull Key item, @Range(from=0L, to=0x7FFFFFFFL) int count, @Nullable BinaryTagHolder nbt) {
            return new ShowItem(Objects.requireNonNull(item, "item"), count, nbt, Collections.emptyMap());
        }

        @Deprecated
        @NotNull
        public static ShowItem showItem(@NotNull Keyed item, @Range(from=0L, to=0x7FFFFFFFL) int count, @Nullable BinaryTagHolder nbt) {
            return new ShowItem(Objects.requireNonNull(item, "item").key(), count, nbt, Collections.emptyMap());
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
        @NotNull
        public static ShowItem of(@NotNull Keyed item, @Range(from=0L, to=0x7FFFFFFFL) int count, @Nullable BinaryTagHolder nbt) {
            return new ShowItem(Objects.requireNonNull(item, "item").key(), count, nbt, Collections.emptyMap());
        }

        @NotNull
        public static ShowItem showItem(@NotNull Keyed item, @Range(from=0L, to=0x7FFFFFFFL) int count, @NotNull Map<Key, ? extends DataComponentValue> dataComponents) {
            return new ShowItem(Objects.requireNonNull(item, "item").key(), count, null, dataComponents);
        }

        private ShowItem(@NotNull Key item, @Range(from=0L, to=0x7FFFFFFFL) int count, @Nullable BinaryTagHolder nbt, @NotNull Map<Key, ? extends DataComponentValue> dataComponents) {
            this.item = item;
            this.count = count;
            this.nbt = nbt;
            this.dataComponents = Collections.unmodifiableMap(new HashMap<Key, DataComponentValue>(dataComponents));
        }

        @NotNull
        public Key item() {
            return this.item;
        }

        @NotNull
        public ShowItem item(@NotNull Key item) {
            if (Objects.requireNonNull(item, "item").equals(this.item)) {
                return this;
            }
            return new ShowItem(item, this.count, this.nbt, this.dataComponents);
        }

        public @Range(from=0L, to=0x7FFFFFFFL) int count() {
            return this.count;
        }

        @NotNull
        public ShowItem count(@Range(from=0L, to=0x7FFFFFFFL) int count) {
            if (count == this.count) {
                return this;
            }
            return new ShowItem(this.item, count, this.nbt, this.dataComponents);
        }

        @Deprecated
        @Nullable
        public BinaryTagHolder nbt() {
            return this.nbt;
        }

        @Deprecated
        @NotNull
        public ShowItem nbt(@Nullable BinaryTagHolder nbt) {
            if (Objects.equals(nbt, this.nbt)) {
                return this;
            }
            return new ShowItem(this.item, this.count, nbt, Collections.emptyMap());
        }

        @NotNull
        public Map<Key, DataComponentValue> dataComponents() {
            return this.dataComponents;
        }

        @NotNull
        public ShowItem dataComponents(@NotNull Map<Key, DataComponentValue> holder) {
            if (Objects.equals(this.dataComponents, holder)) {
                return this;
            }
            return new ShowItem(this.item, this.count, null, holder.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<Key, DataComponentValue>(holder)));
        }

        @NotNull
        public <V extends DataComponentValue> Map<Key, V> dataComponentsAs(@NotNull Class<V> targetType) {
            if (this.dataComponents.isEmpty()) {
                return Collections.emptyMap();
            }
            HashMap<Key, V> results = new HashMap<Key, V>(this.dataComponents.size());
            for (Map.Entry<Key, DataComponentValue> entry : this.dataComponents.entrySet()) {
                results.put(entry.getKey(), DataComponentValueConverterRegistry.convert(targetType, entry.getKey(), entry.getValue()));
            }
            return Collections.unmodifiableMap(results);
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || this.getClass() != other.getClass()) {
                return false;
            }
            ShowItem that = (ShowItem)other;
            return this.item.equals(that.item) && this.count == that.count && Objects.equals(this.nbt, that.nbt) && Objects.equals(this.dataComponents, that.dataComponents);
        }

        public int hashCode() {
            int result = this.item.hashCode();
            result = 31 * result + Integer.hashCode(this.count);
            result = 31 * result + Objects.hashCode(this.nbt);
            result = 31 * result + Objects.hashCode(this.dataComponents);
            return result;
        }

        @Override
        @NotNull
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("item", this.item), ExaminableProperty.of("count", this.count), ExaminableProperty.of("nbt", this.nbt), ExaminableProperty.of("dataComponents", this.dataComponents));
        }

        public String toString() {
            return Internals.toString(this);
        }
    }
}

