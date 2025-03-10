/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Unmodifiable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.inventory;

import eu.kennytv.maintenance.lib.kyori.adventure.builder.AbstractBuilder;
import eu.kennytv.maintenance.lib.kyori.adventure.inventory.BookImpl;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.util.Buildable;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface Book
extends Buildable<Book, Builder>,
Examinable {
    @NotNull
    public static Book book(@NotNull Component title, @NotNull Component author, @NotNull Collection<Component> pages) {
        return new BookImpl(title, author, new ArrayList<Component>(pages));
    }

    @NotNull
    public static Book book(@NotNull Component title, @NotNull Component author, @NotNull @NotNull Component @NotNull ... pages) {
        return Book.book(title, author, Arrays.asList(pages));
    }

    @NotNull
    public static Builder builder() {
        return new BookImpl.BuilderImpl();
    }

    @NotNull
    public Component title();

    @Contract(value="_ -> new", pure=true)
    @NotNull
    public Book title(@NotNull Component var1);

    @NotNull
    public Component author();

    @Contract(value="_ -> new", pure=true)
    @NotNull
    public Book author(@NotNull Component var1);

    public @Unmodifiable @NotNull List<Component> pages();

    @Contract(value="_ -> new", pure=true)
    @NotNull
    default public Book pages(@NotNull @NotNull Component @NotNull ... pages) {
        return this.pages(Arrays.asList(pages));
    }

    @Contract(value="_ -> new", pure=true)
    @NotNull
    public Book pages(@NotNull List<Component> var1);

    @Override
    @NotNull
    default public Builder toBuilder() {
        return Book.builder().title(this.title()).author(this.author()).pages(this.pages());
    }

    public static interface Builder
    extends AbstractBuilder<Book>,
    Buildable.Builder<Book> {
        @Contract(value="_ -> this")
        @NotNull
        public Builder title(@NotNull Component var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder author(@NotNull Component var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder addPage(@NotNull Component var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder pages(@NotNull @NotNull Component @NotNull ... var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder pages(@NotNull Collection<Component> var1);

        @Override
        @NotNull
        public Book build();
    }
}

