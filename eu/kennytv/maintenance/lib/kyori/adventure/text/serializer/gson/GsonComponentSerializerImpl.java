/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.TypeAdapterFactory
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapterFactory;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.ComponentSerializerImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.SerializerFactory;
import eu.kennytv.maintenance.lib.kyori.adventure.util.Services;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class GsonComponentSerializerImpl
implements GsonComponentSerializer {
    private static final Optional<GsonComponentSerializer.Provider> SERVICE = Services.service(GsonComponentSerializer.Provider.class);
    static final Consumer<GsonComponentSerializer.Builder> BUILDER = SERVICE.map(GsonComponentSerializer.Provider::builder).orElseGet(() -> builder -> {});
    private final Gson serializer;
    private final UnaryOperator<GsonBuilder> populator;
    private final boolean downsampleColor;
    @Nullable
    private final LegacyHoverEventSerializer legacyHoverSerializer;
    private final boolean emitLegacyHover;

    GsonComponentSerializerImpl(boolean downsampleColor, @Nullable LegacyHoverEventSerializer legacyHoverSerializer, boolean emitLegacyHover) {
        this.downsampleColor = downsampleColor;
        this.legacyHoverSerializer = legacyHoverSerializer;
        this.emitLegacyHover = emitLegacyHover;
        this.populator = builder -> {
            builder.registerTypeAdapterFactory((TypeAdapterFactory)new SerializerFactory(downsampleColor, legacyHoverSerializer, emitLegacyHover));
            return builder;
        };
        this.serializer = ((GsonBuilder)this.populator.apply(new GsonBuilder().disableHtmlEscaping())).create();
    }

    @Override
    @NotNull
    public Gson serializer() {
        return this.serializer;
    }

    @Override
    @NotNull
    public UnaryOperator<GsonBuilder> populator() {
        return this.populator;
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String string) {
        Component component = (Component)this.serializer().fromJson(string, Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(string);
        }
        return component;
    }

    @Override
    @Nullable
    public Component deserializeOr(@Nullable String input, @Nullable Component fallback) {
        if (input == null) {
            return fallback;
        }
        Component component = (Component)this.serializer().fromJson(input, Component.class);
        if (component == null) {
            return fallback;
        }
        return component;
    }

    @Override
    @NotNull
    public String serialize(@NotNull Component component) {
        return this.serializer().toJson((Object)component);
    }

    @Override
    @NotNull
    public Component deserializeFromTree(@NotNull JsonElement input) {
        Component component = (Component)this.serializer().fromJson(input, Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(input);
        }
        return component;
    }

    @Override
    @NotNull
    public JsonElement serializeToTree(@NotNull Component component) {
        return this.serializer().toJsonTree((Object)component);
    }

    @Override
    @NotNull
    public GsonComponentSerializer.Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static /* synthetic */ Optional access$000() {
        return SERVICE;
    }

    static final class BuilderImpl
    implements GsonComponentSerializer.Builder {
        private boolean downsampleColor = false;
        @Nullable
        private LegacyHoverEventSerializer legacyHoverSerializer;
        private boolean emitLegacyHover = false;

        BuilderImpl() {
            BUILDER.accept(this);
        }

        BuilderImpl(GsonComponentSerializerImpl serializer) {
            this();
            this.downsampleColor = serializer.downsampleColor;
            this.emitLegacyHover = serializer.emitLegacyHover;
            this.legacyHoverSerializer = serializer.legacyHoverSerializer;
        }

        @Override
        @NotNull
        public GsonComponentSerializer.Builder downsampleColors() {
            this.downsampleColor = true;
            return this;
        }

        @Override
        @NotNull
        public GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable LegacyHoverEventSerializer serializer) {
            this.legacyHoverSerializer = serializer;
            return this;
        }

        @Override
        @NotNull
        public GsonComponentSerializer.Builder emitLegacyHoverEvent() {
            this.emitLegacyHover = true;
            return this;
        }

        @Override
        @NotNull
        public GsonComponentSerializer build() {
            if (this.legacyHoverSerializer == null) {
                return this.downsampleColor ? Instances.LEGACY_INSTANCE : Instances.INSTANCE;
            }
            return new GsonComponentSerializerImpl(this.downsampleColor, this.legacyHoverSerializer, this.emitLegacyHover);
        }
    }

    static final class Instances {
        static final GsonComponentSerializer INSTANCE = GsonComponentSerializerImpl.access$000().map(GsonComponentSerializer.Provider::gson).orElseGet(() -> new GsonComponentSerializerImpl(false, null, false));
        static final GsonComponentSerializer LEGACY_INSTANCE = GsonComponentSerializerImpl.access$000().map(GsonComponentSerializer.Provider::gsonLegacy).orElseGet(() -> new GsonComponentSerializerImpl(true, null, true));

        Instances() {
        }
    }
}

