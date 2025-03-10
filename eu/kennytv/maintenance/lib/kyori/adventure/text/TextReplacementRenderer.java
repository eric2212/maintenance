/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.PatternReplacementResult;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextReplacementConfig;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TranslatableComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TranslationArgument;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.HoverEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.HoverEventSource;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.StyleSetter;
import eu.kennytv.maintenance.lib.kyori.adventure.text.renderer.ComponentRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TextReplacementRenderer
implements ComponentRenderer<State> {
    static final TextReplacementRenderer INSTANCE = new TextReplacementRenderer();

    private TextReplacementRenderer() {
    }

    @Override
    @NotNull
    public Component render(@NotNull Component component, @NotNull State state) {
        int i;
        if (!state.running) {
            return component;
        }
        boolean prevFirstMatch = state.firstMatch;
        state.firstMatch = true;
        List<Component> oldChildren = component.children();
        int oldChildrenSize = oldChildren.size();
        StyleSetter<Style> oldStyle = component.style();
        ArrayList<TextComponent> children = null;
        Component modified = component;
        if (component instanceof TextComponent) {
            String content = ((TextComponent)component).content();
            Matcher matcher = state.pattern.matcher(content);
            int replacedUntil = 0;
            while (matcher.find()) {
                PatternReplacementResult result;
                if ((result = state.continuer.shouldReplace(matcher, ++state.matchCount, state.replaceCount)) == PatternReplacementResult.CONTINUE) continue;
                if (result == PatternReplacementResult.STOP) {
                    state.running = false;
                    break;
                }
                if (matcher.start() == 0) {
                    if (matcher.end() == content.length()) {
                        ComponentLike replacement = state.replacement.apply(matcher, (TextComponent.Builder)Component.text().content(matcher.group()).style(component.style()));
                        Component component2 = modified = replacement == null ? Component.empty() : replacement.asComponent();
                        if (modified.style().hoverEvent() != null) {
                            oldStyle = oldStyle.hoverEvent((HoverEventSource)null);
                        }
                        modified = modified.style(modified.style().merge(component.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET));
                        if (children == null) {
                            children = new ArrayList(oldChildrenSize + modified.children().size());
                            children.addAll(modified.children());
                        }
                    } else {
                        modified = Component.text("", component.style());
                        ComponentLike child = state.replacement.apply(matcher, Component.text().content(matcher.group()));
                        if (child != null) {
                            if (children == null) {
                                children = new ArrayList(oldChildrenSize + 1);
                            }
                            children.add((TextComponent)child.asComponent());
                        }
                    }
                } else {
                    if (children == null) {
                        children = new ArrayList(oldChildrenSize + 2);
                    }
                    if (state.firstMatch) {
                        modified = ((TextComponent)component).content(content.substring(0, matcher.start()));
                    } else if (replacedUntil < matcher.start()) {
                        children.add(Component.text(content.substring(replacedUntil, matcher.start())));
                    }
                    ComponentLike builder = state.replacement.apply(matcher, Component.text().content(matcher.group()));
                    if (builder != null) {
                        children.add((TextComponent)builder.asComponent());
                    }
                }
                ++state.replaceCount;
                state.firstMatch = false;
                replacedUntil = matcher.end();
            }
            if (replacedUntil < content.length() && replacedUntil > 0) {
                if (children == null) {
                    children = new ArrayList<TextComponent>(oldChildrenSize);
                }
                children.add(Component.text(content.substring(replacedUntil)));
            }
        } else if (modified instanceof TranslatableComponent) {
            List<TranslationArgument> args = ((TranslatableComponent)modified).arguments();
            ArrayList<TranslationArgument> newArgs = null;
            int size = args.size();
            for (i = 0; i < size; ++i) {
                TranslationArgument replaced;
                TranslationArgument original = args.get(i);
                TranslationArgument translationArgument = replaced = original.value() instanceof Component ? TranslationArgument.component(this.render((Component)original.value(), state)) : original;
                if (replaced != original && newArgs == null) {
                    newArgs = new ArrayList<TranslationArgument>(size);
                    if (i > 0) {
                        newArgs.addAll(args.subList(0, i));
                    }
                }
                if (newArgs == null) continue;
                newArgs.add(replaced);
            }
            if (newArgs != null) {
                modified = ((TranslatableComponent)modified).arguments(newArgs);
            }
        }
        if (state.running) {
            HoverEvent<?> rendered;
            HoverEvent<?> event = oldStyle.hoverEvent();
            if (event != null && event != (rendered = event.withRenderedValue(this, state))) {
                modified = modified.style(s -> s.hoverEvent(rendered));
            }
            boolean first = true;
            for (i = 0; i < oldChildrenSize; ++i) {
                Component child = oldChildren.get(i);
                Component replaced = this.render(child, state);
                if (replaced != child) {
                    if (children == null) {
                        children = new ArrayList(oldChildrenSize);
                    }
                    if (first) {
                        children.addAll(oldChildren.subList(0, i));
                    }
                    first = false;
                }
                if (children == null) continue;
                children.add((TextComponent)replaced);
                first = false;
            }
        } else if (children != null) {
            children.addAll(oldChildren);
        }
        state.firstMatch = prevFirstMatch;
        if (children != null) {
            return modified.children((List<? extends ComponentLike>)children);
        }
        return modified;
    }

    static final class State {
        final Pattern pattern;
        final BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement;
        final TextReplacementConfig.Condition continuer;
        boolean running = true;
        int matchCount = 0;
        int replaceCount = 0;
        boolean firstMatch = true;

        State(@NotNull Pattern pattern, @NotNull BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement, @NotNull TextReplacementConfig.Condition continuer) {
            this.pattern = pattern;
            this.replacement = replacement;
            this.continuer = continuer;
        }
    }
}

