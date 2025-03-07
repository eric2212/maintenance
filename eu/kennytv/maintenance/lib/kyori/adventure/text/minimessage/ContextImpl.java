/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage;

import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointered;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ArgumentQueueImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.Context;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.MiniMessage;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.ParsingException;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.Token;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ContextImpl
implements Context {
    private static final Token[] EMPTY_TOKEN_ARRAY = new Token[0];
    private final boolean strict;
    private final Consumer<String> debugOutput;
    private String message;
    private final MiniMessage miniMessage;
    @Nullable
    private final Pointered target;
    private final TagResolver tagResolver;
    private final UnaryOperator<String> preProcessor;
    private final UnaryOperator<Component> postProcessor;

    ContextImpl(boolean strict, Consumer<String> debugOutput, String message, MiniMessage miniMessage, @Nullable Pointered target, @Nullable TagResolver extraTags, @Nullable UnaryOperator<String> preProcessor, @Nullable UnaryOperator<Component> postProcessor) {
        this.strict = strict;
        this.debugOutput = debugOutput;
        this.message = message;
        this.miniMessage = miniMessage;
        this.target = target;
        this.tagResolver = extraTags == null ? TagResolver.empty() : extraTags;
        this.preProcessor = preProcessor == null ? UnaryOperator.identity() : preProcessor;
        this.postProcessor = postProcessor == null ? UnaryOperator.identity() : postProcessor;
    }

    public boolean strict() {
        return this.strict;
    }

    public Consumer<String> debugOutput() {
        return this.debugOutput;
    }

    @NotNull
    public String message() {
        return this.message;
    }

    void message(@NotNull String message) {
        this.message = message;
    }

    @NotNull
    public TagResolver extraTags() {
        return this.tagResolver;
    }

    public UnaryOperator<Component> postProcessor() {
        return this.postProcessor;
    }

    public UnaryOperator<String> preProcessor() {
        return this.preProcessor;
    }

    @Override
    @Nullable
    public Pointered target() {
        return this.target;
    }

    @Override
    @NotNull
    public Pointered targetOrThrow() {
        if (this.target == null) {
            throw this.newException("A target is required for this deserialization attempt");
        }
        return this.target;
    }

    @Override
    @NotNull
    public <T extends Pointered> T targetAsType(@NotNull Class<T> targetClass) {
        if (Objects.requireNonNull(targetClass, "targetClass").isInstance(this.target)) {
            return (T)((Pointered)targetClass.cast(this.target));
        }
        throw this.newException("A target with type " + targetClass.getSimpleName() + " is required for this deserialization attempt");
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String message) {
        return this.miniMessage.deserialize(Objects.requireNonNull(message, "message"), this.tagResolver);
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String message, @NotNull TagResolver resolver) {
        return this.miniMessage.deserialize(Objects.requireNonNull(message, "message"), TagResolver.builder().resolver(this.tagResolver).resolver(Objects.requireNonNull(resolver, "resolver")).build());
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String message, @NotNull @NotNull TagResolver @NotNull ... resolvers) {
        return this.miniMessage.deserialize(Objects.requireNonNull(message, "message"), TagResolver.builder().resolver(this.tagResolver).resolvers(Objects.requireNonNull(resolvers, "resolvers")).build());
    }

    @Override
    @NotNull
    public ParsingException newException(@NotNull String message) {
        return new ParsingExceptionImpl(message, this.message, null, false, EMPTY_TOKEN_ARRAY);
    }

    @Override
    @NotNull
    public ParsingException newException(@NotNull String message, @NotNull ArgumentQueue tags) {
        return new ParsingExceptionImpl(message, this.message, null, false, ContextImpl.tagsToTokens(((ArgumentQueueImpl)tags).args));
    }

    @Override
    @NotNull
    public ParsingException newException(@NotNull String message, @Nullable Throwable cause, @NotNull ArgumentQueue tags) {
        return new ParsingExceptionImpl(message, this.message, cause, false, ContextImpl.tagsToTokens(((ArgumentQueueImpl)tags).args));
    }

    private static Token[] tagsToTokens(List<? extends Tag.Argument> tags) {
        Token[] tokens = new Token[tags.size()];
        int length = tokens.length;
        for (int i = 0; i < length; ++i) {
            tokens[i] = ((TagPart)tags.get(i)).token();
        }
        return tokens;
    }
}

