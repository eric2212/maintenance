/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.RegExp
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text;

import eu.kennytv.maintenance.lib.kyori.adventure.builder.AbstractBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.PatternReplacementResult;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextReplacementConfigImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.util.Buildable;
import eu.kennytv.maintenance.lib.kyori.adventure.util.IntFunction2;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TextReplacementConfig
extends Buildable<TextReplacementConfig, Builder>,
Examinable {
    @NotNull
    public static Builder builder() {
        return new TextReplacementConfigImpl.Builder();
    }

    @NotNull
    public Pattern matchPattern();

    @FunctionalInterface
    public static interface Condition {
        @NotNull
        public PatternReplacementResult shouldReplace(@NotNull MatchResult var1, int var2, int var3);
    }

    public static interface Builder
    extends AbstractBuilder<TextReplacementConfig>,
    Buildable.Builder<TextReplacementConfig> {
        @Contract(value="_ -> this")
        default public Builder matchLiteral(String literal) {
            return this.match(Pattern.compile(literal, 16));
        }

        @Contract(value="_ -> this")
        @NotNull
        default public Builder match(@NotNull @RegExp String pattern) {
            return this.match(Pattern.compile(pattern));
        }

        @Contract(value="_ -> this")
        @NotNull
        public Builder match(@NotNull Pattern var1);

        @NotNull
        default public Builder once() {
            return this.times(1);
        }

        @Contract(value="_ -> this")
        @NotNull
        default public Builder times(int times) {
            return this.condition((int index, int replaced) -> replaced < times ? PatternReplacementResult.REPLACE : PatternReplacementResult.STOP);
        }

        @Contract(value="_ -> this")
        @NotNull
        default public Builder condition(@NotNull IntFunction2<PatternReplacementResult> condition) {
            return this.condition((MatchResult result, int matchCount, int replaced) -> (PatternReplacementResult)((Object)((Object)condition.apply(matchCount, replaced))));
        }

        @Contract(value="_ -> this")
        @NotNull
        public Builder condition(@NotNull Condition var1);

        @Contract(value="_ -> this")
        @NotNull
        default public Builder replacement(@NotNull String replacement) {
            Objects.requireNonNull(replacement, "replacement");
            return this.replacement((TextComponent.Builder builder) -> builder.content(replacement));
        }

        @Contract(value="_ -> this")
        @NotNull
        default public Builder replacement(@Nullable ComponentLike replacement) {
            @Nullable Component baked = ComponentLike.unbox(replacement);
            return this.replacement((MatchResult result, TextComponent.Builder input) -> baked);
        }

        @Contract(value="_ -> this")
        @NotNull
        default public Builder replacement(@NotNull Function<TextComponent.Builder, @Nullable ComponentLike> replacement) {
            Objects.requireNonNull(replacement, "replacement");
            return this.replacement((MatchResult result, TextComponent.Builder input) -> (ComponentLike)replacement.apply((TextComponent.Builder)input));
        }

        @Contract(value="_ -> this")
        @NotNull
        public Builder replacement(@NotNull BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> var1);
    }
}

