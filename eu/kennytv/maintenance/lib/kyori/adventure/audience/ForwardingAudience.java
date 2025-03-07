/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$OverrideOnly
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.UnknownNullability
 */
package eu.kennytv.maintenance.lib.kyori.adventure.audience;

import eu.kennytv.maintenance.lib.kyori.adventure.audience.Audience;
import eu.kennytv.maintenance.lib.kyori.adventure.audience.Audiences;
import eu.kennytv.maintenance.lib.kyori.adventure.audience.MessageType;
import eu.kennytv.maintenance.lib.kyori.adventure.bossbar.BossBar;
import eu.kennytv.maintenance.lib.kyori.adventure.chat.SignedMessage;
import eu.kennytv.maintenance.lib.kyori.adventure.identity.Identified;
import eu.kennytv.maintenance.lib.kyori.adventure.identity.Identity;
import eu.kennytv.maintenance.lib.kyori.adventure.inventory.Book;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointer;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointers;
import eu.kennytv.maintenance.lib.kyori.adventure.resource.ResourcePackRequest;
import eu.kennytv.maintenance.lib.kyori.adventure.sound.Sound;
import eu.kennytv.maintenance.lib.kyori.adventure.sound.SoundStop;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.title.TitlePart;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

@FunctionalInterface
public interface ForwardingAudience
extends Audience {
    @ApiStatus.OverrideOnly
    @NotNull
    public Iterable<? extends Audience> audiences();

    @Override
    @NotNull
    default public Pointers pointers() {
        return Pointers.empty();
    }

    @Override
    @NotNull
    default public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
        @Nullable ArrayList<Audience> audiences = null;
        for (Audience audience : this.audiences()) {
            Audience filtered;
            if (!filter.test(audience) || (filtered = audience.filterAudience(filter)) == Audience.empty()) continue;
            if (audiences == null) {
                audiences = new ArrayList<Audience>();
            }
            audiences.add(filtered);
        }
        return audiences != null ? Audience.audience(audiences) : Audience.empty();
    }

    @Override
    default public void forEachAudience(@NotNull Consumer<? super Audience> action) {
        for (Audience audience : this.audiences()) {
            audience.forEachAudience(action);
        }
    }

    @Override
    default public void sendMessage(@NotNull Component message) {
        for (Audience audience : this.audiences()) {
            audience.sendMessage(message);
        }
    }

    @Override
    default public void sendMessage(@NotNull Component message,  @NotNull ChatType.Bound boundChatType) {
        for (Audience audience : this.audiences()) {
            audience.sendMessage(message, boundChatType);
        }
    }

    @Override
    default public void sendMessage(@NotNull SignedMessage signedMessage,  @NotNull ChatType.Bound boundChatType) {
        for (Audience audience : this.audiences()) {
            audience.sendMessage(signedMessage, boundChatType);
        }
    }

    @Override
    default public void deleteMessage(@NotNull SignedMessage.Signature signature) {
        for (Audience audience : this.audiences()) {
            audience.deleteMessage(signature);
        }
    }

    @Override
    @Deprecated
    default public void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
        for (Audience audience : this.audiences()) {
            audience.sendMessage(source, message, type);
        }
    }

    @Override
    @Deprecated
    default public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
        for (Audience audience : this.audiences()) {
            audience.sendMessage(source, message, type);
        }
    }

    @Override
    default public void sendActionBar(@NotNull Component message) {
        for (Audience audience : this.audiences()) {
            audience.sendActionBar(message);
        }
    }

    @Override
    default public void sendPlayerListHeader(@NotNull Component header) {
        for (Audience audience : this.audiences()) {
            audience.sendPlayerListHeader(header);
        }
    }

    @Override
    default public void sendPlayerListFooter(@NotNull Component footer) {
        for (Audience audience : this.audiences()) {
            audience.sendPlayerListFooter(footer);
        }
    }

    @Override
    default public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
        for (Audience audience : this.audiences()) {
            audience.sendPlayerListHeaderAndFooter(header, footer);
        }
    }

    @Override
    default public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
        for (Audience audience : this.audiences()) {
            audience.sendTitlePart(part, value);
        }
    }

    @Override
    default public void clearTitle() {
        for (Audience audience : this.audiences()) {
            audience.clearTitle();
        }
    }

    @Override
    default public void resetTitle() {
        for (Audience audience : this.audiences()) {
            audience.resetTitle();
        }
    }

    @Override
    default public void showBossBar(@NotNull BossBar bar) {
        for (Audience audience : this.audiences()) {
            audience.showBossBar(bar);
        }
    }

    @Override
    default public void hideBossBar(@NotNull BossBar bar) {
        for (Audience audience : this.audiences()) {
            audience.hideBossBar(bar);
        }
    }

    @Override
    default public void playSound(@NotNull Sound sound) {
        for (Audience audience : this.audiences()) {
            audience.playSound(sound);
        }
    }

    @Override
    default public void playSound(@NotNull Sound sound, double x, double y, double z) {
        for (Audience audience : this.audiences()) {
            audience.playSound(sound, x, y, z);
        }
    }

    @Override
    default public void playSound(@NotNull Sound sound, @NotNull Sound.Emitter emitter) {
        for (Audience audience : this.audiences()) {
            audience.playSound(sound, emitter);
        }
    }

    @Override
    default public void stopSound(@NotNull SoundStop stop) {
        for (Audience audience : this.audiences()) {
            audience.stopSound(stop);
        }
    }

    @Override
    default public void openBook(@NotNull Book book) {
        for (Audience audience : this.audiences()) {
            audience.openBook(book);
        }
    }

    @Override
    default public void sendResourcePacks(@NotNull ResourcePackRequest request) {
        for (Audience audience : this.audiences()) {
            audience.sendResourcePacks(request);
        }
    }

    @Override
    default public void removeResourcePacks(@NotNull Iterable<UUID> ids) {
        for (Audience audience : this.audiences()) {
            audience.removeResourcePacks(ids);
        }
    }

    @Override
    default public void removeResourcePacks(@NotNull UUID id, @NotNull @NotNull UUID @NotNull ... others) {
        for (Audience audience : this.audiences()) {
            audience.removeResourcePacks(id, others);
        }
    }

    @Override
    default public void clearResourcePacks() {
        for (Audience audience : this.audiences()) {
            audience.clearResourcePacks();
        }
    }

    public static interface Single
    extends ForwardingAudience {
        @ApiStatus.OverrideOnly
        @NotNull
        public Audience audience();

        @Override
        @Deprecated
        @NotNull
        default public Iterable<? extends Audience> audiences() {
            return Collections.singleton(this.audience());
        }

        @Override
        @NotNull
        default public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
            return this.audience().get(pointer);
        }

        @Override
        @Contract(value="_, null -> null; _, !null -> !null")
        @Nullable
        default public <T> T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
            return this.audience().getOrDefault(pointer, defaultValue);
        }

        @Override
        default public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
            return this.audience().getOrDefaultFrom(pointer, defaultValue);
        }

        @Override
        @NotNull
        default public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
            Audience audience = this.audience();
            return filter.test(audience) ? this : Audience.empty();
        }

        @Override
        default public void forEachAudience(@NotNull Consumer<? super Audience> action) {
            this.audience().forEachAudience(action);
        }

        @Override
        @NotNull
        default public Pointers pointers() {
            return this.audience().pointers();
        }

        @Override
        default public void sendMessage(@NotNull Component message) {
            this.audience().sendMessage(message);
        }

        @Override
        default public void sendMessage(@NotNull Component message,  @NotNull ChatType.Bound boundChatType) {
            this.audience().sendMessage(message, boundChatType);
        }

        @Override
        default public void sendMessage(@NotNull SignedMessage signedMessage,  @NotNull ChatType.Bound boundChatType) {
            this.audience().sendMessage(signedMessage, boundChatType);
        }

        @Override
        default public void deleteMessage(@NotNull SignedMessage.Signature signature) {
            this.audience().deleteMessage(signature);
        }

        @Override
        @Deprecated
        default public void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
            this.audience().sendMessage(source, message, type);
        }

        @Override
        @Deprecated
        default public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
            this.audience().sendMessage(source, message, type);
        }

        @Override
        default public void sendActionBar(@NotNull Component message) {
            this.audience().sendActionBar(message);
        }

        @Override
        default public void sendPlayerListHeader(@NotNull Component header) {
            this.audience().sendPlayerListHeader(header);
        }

        @Override
        default public void sendPlayerListFooter(@NotNull Component footer) {
            this.audience().sendPlayerListFooter(footer);
        }

        @Override
        default public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
            this.audience().sendPlayerListHeaderAndFooter(header, footer);
        }

        @Override
        default public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
            this.audience().sendTitlePart(part, value);
        }

        @Override
        default public void clearTitle() {
            this.audience().clearTitle();
        }

        @Override
        default public void resetTitle() {
            this.audience().resetTitle();
        }

        @Override
        default public void showBossBar(@NotNull BossBar bar) {
            this.audience().showBossBar(bar);
        }

        @Override
        default public void hideBossBar(@NotNull BossBar bar) {
            this.audience().hideBossBar(bar);
        }

        @Override
        default public void playSound(@NotNull Sound sound) {
            this.audience().playSound(sound);
        }

        @Override
        default public void playSound(@NotNull Sound sound, double x, double y, double z) {
            this.audience().playSound(sound, x, y, z);
        }

        @Override
        default public void playSound(@NotNull Sound sound, @NotNull Sound.Emitter emitter) {
            this.audience().playSound(sound, emitter);
        }

        @Override
        default public void stopSound(@NotNull SoundStop stop) {
            this.audience().stopSound(stop);
        }

        @Override
        default public void openBook(@NotNull Book book) {
            this.audience().openBook(book);
        }

        @Override
        default public void sendResourcePacks(@NotNull ResourcePackRequest request) {
            this.audience().sendResourcePacks(request.callback(Audiences.unwrapCallback(this, this.audience(), request.callback())));
        }

        @Override
        default public void removeResourcePacks(@NotNull Iterable<UUID> ids) {
            this.audience().removeResourcePacks(ids);
        }

        @Override
        default public void removeResourcePacks(@NotNull UUID id, @NotNull @NotNull UUID @NotNull ... others) {
            this.audience().removeResourcePacks(id, others);
        }

        @Override
        default public void clearResourcePacks() {
            this.audience().clearResourcePacks();
        }
    }
}

