/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.flattener;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.KeybindComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ScoreComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.SelectorComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TranslatableComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.flattener.ComponentFlattener;
import eu.kennytv.maintenance.lib.kyori.adventure.text.flattener.FlattenerListener;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.util.InheritanceAwareMap;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ComponentFlattenerImpl
implements ComponentFlattener {
    static final ComponentFlattener BASIC = (ComponentFlattener)new BuilderImpl().mapper(KeybindComponent.class, component -> component.keybind()).mapper(ScoreComponent.class, component -> {
        @Nullable String value = component.value();
        return value != null ? value : "";
    }).mapper(SelectorComponent.class, SelectorComponent::pattern).mapper(TextComponent.class, TextComponent::content).mapper(TranslatableComponent.class, component -> {
        @Nullable String fallback = component.fallback();
        return fallback != null ? fallback : component.key();
    }).build();
    static final ComponentFlattener TEXT_ONLY = (ComponentFlattener)new BuilderImpl().mapper(TextComponent.class, TextComponent::content).build();
    private static final int MAX_DEPTH = 512;
    private final InheritanceAwareMap<Component, Handler> flatteners;
    private final Function<Component, String> unknownHandler;

    ComponentFlattenerImpl(InheritanceAwareMap<Component, Handler> flatteners, @Nullable Function<Component, String> unknownHandler) {
        this.flatteners = flatteners;
        this.unknownHandler = unknownHandler;
    }

    @Override
    public void flatten(@NotNull Component input, @NotNull FlattenerListener listener) {
        this.flatten0(input, listener, 0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void flatten0(@NotNull Component input, @NotNull FlattenerListener listener, int depth) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(listener, "listener");
        if (input == Component.empty()) {
            return;
        }
        if (depth > 512) {
            throw new IllegalStateException("Exceeded maximum depth of 512 while attempting to flatten components!");
        }
        @Nullable Handler flattener = this.flattener(input);
        Style inputStyle = input.style();
        listener.pushStyle(inputStyle);
        try {
            if (flattener != null) {
                flattener.handle(this, input, listener, depth + 1);
            }
            if (!input.children().isEmpty() && listener.shouldContinue()) {
                for (Component child : input.children()) {
                    this.flatten0(child, listener, depth + 1);
                }
            }
        } finally {
            listener.popStyle(inputStyle);
        }
    }

    @Nullable
    private <T extends Component> Handler flattener(T test) {
        Handler flattener = this.flatteners.get(test.getClass());
        if (flattener == null && this.unknownHandler != null) {
            return (self, component, listener, depth) -> listener.component(this.unknownHandler.apply(component));
        }
        return flattener;
    }

    @Override
    public @NotNull ComponentFlattener.Builder toBuilder() {
        return new BuilderImpl(this.flatteners, this.unknownHandler);
    }

    static final class BuilderImpl
    implements ComponentFlattener.Builder {
        private final InheritanceAwareMap.Builder<Component, Handler> flatteners;
        @Nullable
        private Function<Component, String> unknownHandler;

        BuilderImpl() {
            this.flatteners = InheritanceAwareMap.builder().strict(true);
        }

        BuilderImpl(InheritanceAwareMap<Component, Handler> flatteners, @Nullable Function<Component, String> unknownHandler) {
            this.flatteners = InheritanceAwareMap.builder(flatteners).strict(true);
            this.unknownHandler = unknownHandler;
        }

        @Override
        @NotNull
        public ComponentFlattener build() {
            return new ComponentFlattenerImpl((InheritanceAwareMap)this.flatteners.build(), this.unknownHandler);
        }

        @Override
        public <T extends Component> @NotNull ComponentFlattener.Builder mapper(@NotNull Class<T> type, @NotNull Function<T, String> converter) {
            this.flatteners.put(type, (self, component, listener, depth) -> listener.component((String)converter.apply(component)));
            return this;
        }

        @Override
        public <T extends Component> @NotNull ComponentFlattener.Builder complexMapper(@NotNull Class<T> type, @NotNull BiConsumer<T, Consumer<Component>> converter) {
            this.flatteners.put(type, (self, component, listener, depth) -> converter.accept(component, c -> self.flatten0(c, listener, depth)));
            return this;
        }

        @Override
        public @NotNull ComponentFlattener.Builder unknownMapper(@Nullable Function<Component, String> converter) {
            this.unknownHandler = converter;
            return this;
        }
    }

    @FunctionalInterface
    static interface Handler {
        public void handle(ComponentFlattenerImpl var1, Component var2, FlattenerListener var3, int var4);
    }
}

