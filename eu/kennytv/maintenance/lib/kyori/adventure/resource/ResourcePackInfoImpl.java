/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.resource;

import eu.kennytv.maintenance.lib.kyori.adventure.internal.Internals;
import eu.kennytv.maintenance.lib.kyori.adventure.resource.ResourcePackInfo;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ResourcePackInfoImpl
implements ResourcePackInfo {
    private final UUID id;
    private final URI uri;
    private final String hash;

    ResourcePackInfoImpl(@NotNull UUID id, @NotNull URI uri, @NotNull String hash) {
        this.id = Objects.requireNonNull(id, "id");
        this.uri = Objects.requireNonNull(uri, "uri");
        this.hash = Objects.requireNonNull(hash, "hash");
    }

    @Override
    @NotNull
    public UUID id() {
        return this.id;
    }

    @Override
    @NotNull
    public URI uri() {
        return this.uri;
    }

    @Override
    @NotNull
    public String hash() {
        return this.hash;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("id", this.id), ExaminableProperty.of("uri", this.uri), ExaminableProperty.of("hash", this.hash));
    }

    public String toString() {
        return Internals.toString(this);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ResourcePackInfoImpl)) {
            return false;
        }
        ResourcePackInfoImpl that = (ResourcePackInfoImpl)other;
        return this.id.equals(that.id) && this.uri.equals(that.uri) && this.hash.equals(that.hash);
    }

    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.uri.hashCode();
        result = 31 * result + this.hash.hashCode();
        return result;
    }

    static CompletableFuture<String> computeHash(URI uri, Executor exec) {
        CompletableFuture<String> result = new CompletableFuture<String>();
        exec.execute(() -> {
            try {
                URL url = uri.toURL();
                URLConnection conn = url.openConnection();
                conn.addRequestProperty("User-Agent", "adventure/" + ResourcePackInfoImpl.class.getPackage().getSpecificationVersion() + " (pack-fetcher)");
                try (InputStream is = conn.getInputStream();){
                    int read;
                    MessageDigest digest = MessageDigest.getInstance("SHA-1");
                    byte[] buf = new byte[8192];
                    while ((read = is.read(buf)) != -1) {
                        digest.update(buf, 0, read);
                    }
                    result.complete(ResourcePackInfoImpl.bytesToString(digest.digest()));
                }
            } catch (IOException | NoSuchAlgorithmException ex) {
                result.completeExceptionally(ex);
            }
        });
        return result;
    }

    static String bytesToString(byte[] arr) {
        StringBuilder builder = new StringBuilder(arr.length * 2);
        Formatter fmt = new Formatter(builder, Locale.ROOT);
        for (int i = 0; i < arr.length; ++i) {
            fmt.format("%02x", arr[i] & 0xFF);
        }
        return builder.toString();
    }

    static final class BuilderImpl
    implements ResourcePackInfo.Builder {
        private UUID id;
        private URI uri;
        private String hash;

        BuilderImpl() {
        }

        @Override
        @NotNull
        public ResourcePackInfo.Builder id(@NotNull UUID id) {
            this.id = Objects.requireNonNull(id, "id");
            return this;
        }

        @Override
        @NotNull
        public ResourcePackInfo.Builder uri(@NotNull URI uri) {
            this.uri = Objects.requireNonNull(uri, "uri");
            if (this.id == null) {
                this.id = UUID.nameUUIDFromBytes(uri.toString().getBytes(StandardCharsets.UTF_8));
            }
            return this;
        }

        @Override
        @NotNull
        public ResourcePackInfo.Builder hash(@NotNull String hash) {
            this.hash = Objects.requireNonNull(hash, "hash");
            return this;
        }

        @Override
        @NotNull
        public ResourcePackInfo build() {
            return new ResourcePackInfoImpl(this.id, this.uri, this.hash);
        }

        @Override
        @NotNull
        public CompletableFuture<ResourcePackInfo> computeHashAndBuild(@NotNull Executor executor) {
            return ResourcePackInfoImpl.computeHash(Objects.requireNonNull(this.uri, "uri"), executor).thenApply(hash -> {
                this.hash((String)hash);
                return this.build();
            });
        }
    }
}

