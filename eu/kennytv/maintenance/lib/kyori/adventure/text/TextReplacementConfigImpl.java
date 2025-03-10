/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text;

import eu.kennytv.maintenance.lib.kyori.adventure.internal.Internals;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.PatternReplacementResult;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextReplacementConfig;
import eu.kennytv.maintenance.lib.kyori.adventure.text.TextReplacementRenderer;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TextReplacementConfigImpl
implements TextReplacementConfig {
    private final Pattern matchPattern;
    private final BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement;
    private final TextReplacementConfig.Condition continuer;

    TextReplacementConfigImpl(Builder builder) {
        this.matchPattern = builder.matchPattern;
        this.replacement = builder.replacement;
        this.continuer = builder.continuer;
    }

    @Override
    @NotNull
    public Pattern matchPattern() {
        return this.matchPattern;
    }

    TextReplacementRenderer.State createState() {
        return new TextReplacementRenderer.State(this.matchPattern, this.replacement, this.continuer);
    }

    @Override
    public @NotNull TextReplacementConfig.Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("matchPattern", this.matchPattern), ExaminableProperty.of("replacement", this.replacement), ExaminableProperty.of("continuer", this.continuer));
    }

    public String toString() {
        return Internals.toString(this);
    }

    static final class Builder
    implements TextReplacementConfig.Builder {
        @Nullable
        Pattern matchPattern;
        @Nullable
        @Nullable BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement;
        TextReplacementConfig.Condition continuer = (matchResult, index, replacement) -> PatternReplacementResult.REPLACE;

        Builder() {
        }

        Builder(TextReplacementConfigImpl instance) {
            this.matchPattern = instance.matchPattern;
            this.replacement = instance.replacement;
            this.continuer = instance.continuer;
        }

        @Override
        @NotNull
        public Builder match(@NotNull Pattern pattern) {
            this.matchPattern = Objects.requireNonNull(pattern, "pattern");
            return this;
        }

        @Override
        @NotNull
        public Builder condition(@NotNull TextReplacementConfig.Condition condition) {
            this.continuer = Objects.requireNonNull(condition, "continuation");
            return this;
        }

        @Override
        @NotNull
        public Builder replacement(@NotNull BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement) {
            this.replacement = Objects.requireNonNull(replacement, "replacement");
            return this;
        }

        @Override
        @NotNull
        public TextReplacementConfig build() {
            if (this.matchPattern == null) {
                throw new IllegalStateException("A pattern must be provided to match against");
            }
            if (this.replacement == null) {
                throw new IllegalStateException("A replacement action must be provided");
            }
            return new TextReplacementConfigImpl(this);
        }
    }
}

