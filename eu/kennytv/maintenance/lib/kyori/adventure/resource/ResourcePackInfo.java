/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.resource;

import eu.kennytv.maintenance.lib.kyori.adventure.builder.AbstractBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.resource.ResourcePackInfoImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.resource.ResourcePackInfoLike;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface ResourcePackInfo
extends Examinable,
ResourcePackInfoLike {
    @NotNull
    public static ResourcePackInfo resourcePackInfo(@NotNull UUID id, @NotNull URI uri, @NotNull String hash) {
        return new ResourcePackInfoImpl(id, uri, hash);
    }

    @NotNull
    public static Builder resourcePackInfo() {
        return new ResourcePackInfoImpl.BuilderImpl();
    }

    @NotNull
    public UUID id();

    @NotNull
    public URI uri();

    @NotNull
    public String hash();

    @Override
    @NotNull
    default public ResourcePackInfo asResourcePackInfo() {
        return this;
    }

    public static interface Builder
    extends AbstractBuilder<ResourcePackInfo>,
    ResourcePackInfoLike {
        @Contract(value="_ -> this")
        @NotNull
        public Builder id(@NotNull UUID var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder uri(@NotNull URI var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder hash(@NotNull String var1);

        @Override
        @NotNull
        public ResourcePackInfo build();

        @NotNull
        default public CompletableFuture<ResourcePackInfo> computeHashAndBuild() {
            return this.computeHashAndBuild(ForkJoinPool.commonPool());
        }

        @NotNull
        public CompletableFuture<ResourcePackInfo> computeHashAndBuild(@NotNull Executor var1);

        @Override
        @NotNull
        default public ResourcePackInfo asResourcePackInfo() {
            return this.build();
        }
    }
}

