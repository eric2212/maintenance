/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text;

import eu.kennytv.maintenance.lib.kyori.adventure.builder.AbstractBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.text.BuildableComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentBuilderApplicable;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.ClickEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.HoverEventSource;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.MutableStyleSetter;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextColor;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.TextDecoration;
import eu.kennytv.maintenance.lib.kyori.adventure.util.Buildable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface ComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>>
extends AbstractBuilder<C>,
Buildable.Builder<C>,
ComponentBuilderApplicable,
ComponentLike,
MutableStyleSetter<B> {
    @Contract(value="_ -> this")
    @NotNull
    public B append(@NotNull Component var1);

    @Contract(value="_ -> this")
    @NotNull
    default public B append(@NotNull ComponentLike component) {
        return this.append(component.asComponent());
    }

    @Contract(value="_ -> this")
    @NotNull
    default public B append(@NotNull ComponentBuilder<?, ?> builder) {
        return this.append((Component)builder.build());
    }

    @Contract(value="_ -> this")
    @NotNull
    public B append(@NotNull @NotNull Component @NotNull ... var1);

    @Contract(value="_ -> this")
    @NotNull
    public B append(@NotNull @NotNull ComponentLike @NotNull ... var1);

    @Contract(value="_ -> this")
    @NotNull
    public B append(@NotNull Iterable<? extends ComponentLike> var1);

    @NotNull
    default public B appendNewline() {
        return this.append((Component)Component.newline());
    }

    @NotNull
    default public B appendSpace() {
        return this.append((Component)Component.space());
    }

    @Contract(value="_ -> this")
    @NotNull
    default public B apply(@NotNull Consumer<? super ComponentBuilder<?, ?>> consumer) {
        consumer.accept(this);
        return (B)this;
    }

    @Contract(value="_ -> this")
    @NotNull
    public B applyDeep(@NotNull Consumer<? super ComponentBuilder<?, ?>> var1);

    @Contract(value="_ -> this")
    @NotNull
    public B mapChildren(@NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> var1);

    @Contract(value="_ -> this")
    @NotNull
    public B mapChildrenDeep(@NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> var1);

    @NotNull
    public List<Component> children();

    @Contract(value="_ -> this")
    @NotNull
    public B style(@NotNull Style var1);

    @Contract(value="_ -> this")
    @NotNull
    public B style(@NotNull Consumer<Style.Builder> var1);

    @Override
    @Contract(value="_ -> this")
    @NotNull
    public B font(@Nullable Key var1);

    @Override
    @Contract(value="_ -> this")
    @NotNull
    public B color(@Nullable TextColor var1);

    @Override
    @Contract(value="_ -> this")
    @NotNull
    public B colorIfAbsent(@Nullable TextColor var1);

    @Override
    @Contract(value="_, _ -> this")
    @NotNull
    default public B decorations(@NotNull Set<TextDecoration> decorations, boolean flag) {
        return (B)((ComponentBuilder)MutableStyleSetter.super.decorations((Set)decorations, flag));
    }

    @Override
    @Contract(value="_ -> this")
    @NotNull
    default public B decorate(@NotNull TextDecoration decoration) {
        return (B)this.decoration(decoration, TextDecoration.State.TRUE);
    }

    @Override
    @Contract(value="_ -> this")
    @NotNull
    default public B decorate(@NotNull @NotNull TextDecoration @NotNull ... decorations) {
        return (B)((ComponentBuilder)MutableStyleSetter.super.decorate(decorations));
    }

    @Override
    @Contract(value="_, _ -> this")
    @NotNull
    default public B decoration(@NotNull TextDecoration decoration, boolean flag) {
        return (B)this.decoration(decoration, TextDecoration.State.byBoolean(flag));
    }

    @Override
    @Contract(value="_ -> this")
    @NotNull
    default public B decorations(@NotNull Map<TextDecoration, TextDecoration.State> decorations) {
        return (B)((ComponentBuilder)MutableStyleSetter.super.decorations((Map)decorations));
    }

    @Override
    @Contract(value="_, _ -> this")
    @NotNull
    public B decoration(@NotNull TextDecoration var1, @NotNull TextDecoration.State var2);

    @Override
    @Contract(value="_, _ -> this")
    @NotNull
    public B decorationIfAbsent(@NotNull TextDecoration var1, @NotNull TextDecoration.State var2);

    @Override
    @Contract(value="_ -> this")
    @NotNull
    public B clickEvent(@Nullable ClickEvent var1);

    @Override
    @Contract(value="_ -> this")
    @NotNull
    public B hoverEvent(@Nullable HoverEventSource<?> var1);

    @Override
    @Contract(value="_ -> this")
    @NotNull
    public B insertion(@Nullable String var1);

    @Contract(value="_ -> this")
    @NotNull
    default public B mergeStyle(@NotNull Component that) {
        return this.mergeStyle(that, Style.Merge.all());
    }

    @Contract(value="_, _ -> this")
    @NotNull
    default public B mergeStyle(@NotNull Component that, @NotNull Style.Merge @NotNull ... merges) {
        return this.mergeStyle(that, Style.Merge.merges(merges));
    }

    @Contract(value="_, _ -> this")
    @NotNull
    public B mergeStyle(@NotNull Component var1, @NotNull Set<Style.Merge> var2);

    @NotNull
    public B resetStyle();

    @Override
    @NotNull
    public C build();

    @Contract(value="_ -> this")
    @NotNull
    default public B applicableApply(@NotNull ComponentBuilderApplicable applicable) {
        applicable.componentBuilderApply(this);
        return (B)this;
    }

    @Override
    default public void componentBuilderApply(@NotNull ComponentBuilder<?, ?> component) {
        component.append(this);
    }

    @Override
    @NotNull
    default public Component asComponent() {
        return this.build();
    }
}

