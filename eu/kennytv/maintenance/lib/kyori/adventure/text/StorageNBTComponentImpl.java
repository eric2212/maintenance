/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text;

import eu.kennytv.maintenance.lib.kyori.adventure.internal.Internals;
import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.text.AbstractNBTComponentBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.ComponentLike;
import eu.kennytv.maintenance.lib.kyori.adventure.text.NBTComponentImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.StorageNBTComponent;
import eu.kennytv.maintenance.lib.kyori.adventure.text.format.Style;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class StorageNBTComponentImpl
extends NBTComponentImpl<StorageNBTComponent, StorageNBTComponent.Builder>
implements StorageNBTComponent {
    private final Key storage;

    @NotNull
    static StorageNBTComponent create(@NotNull List<? extends ComponentLike> children, @NotNull Style style, String nbtPath, boolean interpret, @Nullable ComponentLike separator, @NotNull Key storage) {
        return new StorageNBTComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), Objects.requireNonNull(style, "style"), Objects.requireNonNull(nbtPath, "nbtPath"), interpret, ComponentLike.unbox(separator), Objects.requireNonNull(storage, "storage"));
    }

    StorageNBTComponentImpl(@NotNull List<Component> children, @NotNull Style style, String nbtPath, boolean interpret, @Nullable Component separator, Key storage) {
        super(children, style, nbtPath, interpret, separator);
        this.storage = storage;
    }

    @Override
    @NotNull
    public StorageNBTComponent nbtPath(@NotNull String nbtPath) {
        if (Objects.equals(this.nbtPath, nbtPath)) {
            return this;
        }
        return StorageNBTComponentImpl.create(this.children, this.style, nbtPath, this.interpret, this.separator, this.storage);
    }

    @Override
    @NotNull
    public StorageNBTComponent interpret(boolean interpret) {
        if (this.interpret == interpret) {
            return this;
        }
        return StorageNBTComponentImpl.create(this.children, this.style, this.nbtPath, interpret, this.separator, this.storage);
    }

    @Override
    @Nullable
    public Component separator() {
        return this.separator;
    }

    @Override
    @NotNull
    public StorageNBTComponent separator(@Nullable ComponentLike separator) {
        return StorageNBTComponentImpl.create(this.children, this.style, this.nbtPath, this.interpret, separator, this.storage);
    }

    @Override
    @NotNull
    public Key storage() {
        return this.storage;
    }

    @Override
    @NotNull
    public StorageNBTComponent storage(@NotNull Key storage) {
        if (Objects.equals(this.storage, storage)) {
            return this;
        }
        return StorageNBTComponentImpl.create(this.children, this.style, this.nbtPath, this.interpret, this.separator, storage);
    }

    @Override
    @NotNull
    public StorageNBTComponent children(@NotNull List<? extends ComponentLike> children) {
        return StorageNBTComponentImpl.create(children, this.style, this.nbtPath, this.interpret, this.separator, this.storage);
    }

    @Override
    @NotNull
    public StorageNBTComponent style(@NotNull Style style) {
        return StorageNBTComponentImpl.create(this.children, style, this.nbtPath, this.interpret, this.separator, this.storage);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StorageNBTComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        StorageNBTComponentImpl that = (StorageNBTComponentImpl)other;
        return Objects.equals(this.storage, that.storage());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.storage.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @Override
    public @NotNull StorageNBTComponent.Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static class BuilderImpl
    extends AbstractNBTComponentBuilder<StorageNBTComponent, StorageNBTComponent.Builder>
    implements StorageNBTComponent.Builder {
        @Nullable
        private Key storage;

        BuilderImpl() {
        }

        BuilderImpl(@NotNull StorageNBTComponent component) {
            super(component);
            this.storage = component.storage();
        }

        @Override
        public @NotNull StorageNBTComponent.Builder storage(@NotNull Key storage) {
            this.storage = Objects.requireNonNull(storage, "storage");
            return this;
        }

        @Override
        @NotNull
        public StorageNBTComponent build() {
            if (this.nbtPath == null) {
                throw new IllegalStateException("nbt path must be set");
            }
            if (this.storage == null) {
                throw new IllegalStateException("storage must be set");
            }
            return StorageNBTComponentImpl.create(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.separator, this.storage);
        }
    }
}

