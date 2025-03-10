/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.inventory;

import eu.kennytv.maintenance.lib.kyori.adventure.internal.Internals;
import eu.kennytv.maintenance.lib.kyori.adventure.inventory.Book;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

final class BookImpl
implements Book {
    private final Component title;
    private final Component author;
    private final List<Component> pages;

    BookImpl(@NotNull Component title, @NotNull Component author, @NotNull List<Component> pages) {
        this.title = Objects.requireNonNull(title, "title");
        this.author = Objects.requireNonNull(author, "author");
        this.pages = Collections.unmodifiableList(Objects.requireNonNull(pages, "pages"));
    }

    @Override
    @NotNull
    public Component title() {
        return this.title;
    }

    @Override
    @NotNull
    public Book title(@NotNull Component title) {
        return new BookImpl(Objects.requireNonNull(title, "title"), this.author, this.pages);
    }

    @Override
    @NotNull
    public Component author() {
        return this.author;
    }

    @Override
    @NotNull
    public Book author(@NotNull Component author) {
        return new BookImpl(this.title, Objects.requireNonNull(author, "author"), this.pages);
    }

    @Override
    @NotNull
    public List<Component> pages() {
        return this.pages;
    }

    @Override
    @NotNull
    public Book pages(@NotNull List<Component> pages) {
        return new BookImpl(this.title, this.author, new ArrayList<Component>((Collection)Objects.requireNonNull(pages, "pages")));
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("title", this.title), ExaminableProperty.of("author", this.author), ExaminableProperty.of("pages", this.pages));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookImpl)) {
            return false;
        }
        BookImpl that = (BookImpl)o;
        return this.title.equals(that.title) && this.author.equals(that.author) && this.pages.equals(that.pages);
    }

    public int hashCode() {
        int result = this.title.hashCode();
        result = 31 * result + this.author.hashCode();
        result = 31 * result + this.pages.hashCode();
        return result;
    }

    public String toString() {
        return Internals.toString(this);
    }

    static final class BuilderImpl
    implements Book.Builder {
        private Component title = Component.empty();
        private Component author = Component.empty();
        private final List<Component> pages = new ArrayList<Component>();

        BuilderImpl() {
        }

        @Override
        @NotNull
        public Book.Builder title(@NotNull Component title) {
            this.title = Objects.requireNonNull(title, "title");
            return this;
        }

        @Override
        @NotNull
        public Book.Builder author(@NotNull Component author) {
            this.author = Objects.requireNonNull(author, "author");
            return this;
        }

        @Override
        @NotNull
        public Book.Builder addPage(@NotNull Component page) {
            this.pages.add(Objects.requireNonNull(page, "page"));
            return this;
        }

        @Override
        @NotNull
        public Book.Builder pages(@NotNull Collection<Component> pages) {
            this.pages.addAll(Objects.requireNonNull(pages, "pages"));
            return this;
        }

        @Override
        @NotNull
        public Book.Builder pages(@NotNull @NotNull Component @NotNull ... pages) {
            Collections.addAll(this.pages, pages);
            return this;
        }

        @Override
        @NotNull
        public Book build() {
            return new BookImpl(this.title, this.author, new ArrayList<Component>(this.pages));
        }
    }
}

