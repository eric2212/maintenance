/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.UnknownNullability
 */
package eu.kennytv.maintenance.lib.kyori.adventure.audience;

import eu.kennytv.maintenance.lib.kyori.adventure.audience.Audience;
import eu.kennytv.maintenance.lib.kyori.adventure.audience.MessageType;
import eu.kennytv.maintenance.lib.kyori.adventure.chat.SignedMessage;
import eu.kennytv.maintenance.lib.kyori.adventure.identity.Identified;
import eu.kennytv.maintenance.lib.kyori.adventure.identity.Identity;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointer;
import eu.kennytv.maintenance.lib.kyori.adventure.resource.ResourcePackInfoLike;
import eu.kennytv.maintenance.lib.kyori.adventure.resource.ResourcePackRequest;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

final class EmptyAudience
implements Audience {
    static final EmptyAudience INSTANCE = new EmptyAudience();

    EmptyAudience() {
    }

    @Override
    @NotNull
    public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
        return Optional.empty();
    }

    @Override
    @Contract(value="_, null -> null; _, !null -> !null")
    @Nullable
    public <T> T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
        return defaultValue;
    }

    @Override
    public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
        return defaultValue.get();
    }

    @Override
    @NotNull
    public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
        return this;
    }

    @Override
    public void forEachAudience(@NotNull Consumer<? super Audience> action) {
    }

    @Override
    public void sendMessage(@NotNull ComponentLike message) {
    }

    @Override
    public void sendMessage(@NotNull Component message) {
    }

    @Override
    @Deprecated
    public void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
    }

    @Override
    @Deprecated
    public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
    }

    @Override
    public void sendMessage(@NotNull Component message,  @NotNull ChatType.Bound boundChatType) {
    }

    @Override
    public void sendMessage(@NotNull SignedMessage signedMessage,  @NotNull ChatType.Bound boundChatType) {
    }

    @Override
    public void deleteMessage(@NotNull SignedMessage.Signature signature) {
    }

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {
    }

    @Override
    public void sendPlayerListHeader(@NotNull ComponentLike header) {
    }

    @Override
    public void sendPlayerListFooter(@NotNull ComponentLike footer) {
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
    }

    @Override
    public void openBook( @NotNull Book.Builder book) {
    }

    @Override
    public void sendResourcePacks(@NotNull ResourcePackInfoLike request, @NotNull @NotNull ResourcePackInfoLike @NotNull ... others) {
    }

    @Override
    public void removeResourcePacks(@NotNull ResourcePackRequest request) {
    }

    @Override
    public void removeResourcePacks(@NotNull ResourcePackInfoLike request, @NotNull @NotNull ResourcePackInfoLike @NotNull ... others) {
    }

    public boolean equals(Object that) {
        return this == that;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        return "EmptyAudience";
    }
}

