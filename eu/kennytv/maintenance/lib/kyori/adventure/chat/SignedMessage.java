/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.chat;

import eu.kennytv.maintenance.lib.kyori.adventure.chat.SignedMessageImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.identity.Identified;
import eu.kennytv.maintenance.lib.kyori.adventure.identity.Identity;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.time.Instant;
import java.util.stream.Stream;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface SignedMessage
extends Identified,
Examinable {
    @Contract(value="_ -> new", pure=true)
    @NotNull
    public static Signature signature(byte[] signature) {
        return new SignedMessageImpl.SignatureImpl(signature);
    }

    @Contract(value="_, _ -> new", pure=true)
    @NotNull
    public static SignedMessage system(@NotNull String message, @Nullable ComponentLike unsignedContent) {
        return new SignedMessageImpl(message, ComponentLike.unbox(unsignedContent));
    }

    @Contract(pure=true)
    @NotNull
    public Instant timestamp();

    @Contract(pure=true)
    public long salt();

    @Contract(pure=true)
    @Nullable
    public Signature signature();

    @Contract(pure=true)
    @Nullable
    public Component unsignedContent();

    @Contract(pure=true)
    @NotNull
    public String message();

    @Contract(pure=true)
    default public boolean isSystem() {
        return this.identity() == Identity.nil();
    }

    @Contract(pure=true)
    default public boolean canDelete() {
        return this.signature() != null;
    }

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("timestamp", this.timestamp()), ExaminableProperty.of("salt", this.salt()), ExaminableProperty.of("signature", this.signature()), ExaminableProperty.of("unsignedContent", this.unsignedContent()), ExaminableProperty.of("message", this.message()));
    }

    @ApiStatus.NonExtendable
    public static interface Signature
    extends Examinable {
        @Contract(pure=true)
        public byte[] bytes();

        @Override
        @NotNull
        default public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("bytes", this.bytes()));
        }
    }
}

