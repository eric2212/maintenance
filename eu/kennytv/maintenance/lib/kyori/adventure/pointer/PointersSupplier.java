/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.pointer;

import eu.kennytv.maintenance.lib.kyori.adventure.builder.AbstractBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointer;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.Pointers;
import eu.kennytv.maintenance.lib.kyori.adventure.pointer.PointersSupplierImpl;
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PointersSupplier<T> {
    @NotNull
    public static <T> Builder<T> builder() {
        return new PointersSupplierImpl.BuilderImpl();
    }

    @NotNull
    public Pointers view(@NotNull T var1);

    public <P> boolean supports(@NotNull Pointer<P> var1);

    @Nullable
    public <P> Function<? super T, P> resolver(@NotNull Pointer<P> var1);

    public static interface Builder<T>
    extends AbstractBuilder<PointersSupplier<T>> {
        @Contract(value="_ -> this")
        @NotNull
        public Builder<T> parent(@Nullable PointersSupplier<? super T> var1);

        @Contract(value="_, _ -> this")
        @NotNull
        public <P> Builder<T> resolving(@NotNull Pointer<P> var1, @NotNull Function<T, P> var2);
    }
}

