/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.translation;

import eu.kennytv.maintenance.lib.kyori.adventure.internal.Internals;
import eu.kennytv.maintenance.lib.kyori.adventure.key.Key;
import eu.kennytv.maintenance.lib.kyori.adventure.translation.TranslationLocales;
import eu.kennytv.maintenance.lib.kyori.adventure.translation.TranslationRegistry;
import eu.kennytv.maintenance.lib.kyori.adventure.util.TriState;
import eu.kennytv.maintenance.lib.kyori.examination.Examinable;
import eu.kennytv.maintenance.lib.kyori.examination.ExaminableProperty;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TranslationRegistryImpl
implements Examinable,
TranslationRegistry {
    private final Key name;
    private final Map<String, Translation> translations = new ConcurrentHashMap<String, Translation>();
    private Locale defaultLocale = Locale.US;

    TranslationRegistryImpl(Key name) {
        this.name = name;
    }

    @Override
    public void register(@NotNull String key, @NotNull Locale locale, @NotNull MessageFormat format) {
        this.translations.computeIfAbsent(key, x$0 -> new Translation((String)x$0)).register(locale, format);
    }

    @Override
    public void unregister(@NotNull String key) {
        this.translations.remove(key);
    }

    @Override
    @NotNull
    public Key name() {
        return this.name;
    }

    @Override
    public boolean contains(@NotNull String key) {
        return this.translations.containsKey(key);
    }

    @Override
    @NotNull
    public TriState hasAnyTranslations() {
        if (!this.translations.isEmpty()) {
            return TriState.TRUE;
        }
        return TriState.FALSE;
    }

    @Override
    @Nullable
    public MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        Translation translation = this.translations.get(key);
        if (translation == null) {
            return null;
        }
        return translation.translate(locale);
    }

    @Override
    public void defaultLocale(@NotNull Locale defaultLocale) {
        this.defaultLocale = Objects.requireNonNull(defaultLocale, "defaultLocale");
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("translations", this.translations));
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TranslationRegistryImpl)) {
            return false;
        }
        TranslationRegistryImpl that = (TranslationRegistryImpl)other;
        return this.name.equals(that.name) && this.translations.equals(that.translations) && this.defaultLocale.equals(that.defaultLocale);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.translations, this.defaultLocale);
    }

    public String toString() {
        return Internals.toString(this);
    }

    final class Translation
    implements Examinable {
        private final String key;
        private final Map<Locale, MessageFormat> formats;

        Translation(String key) {
            this.key = Objects.requireNonNull(key, "translation key");
            this.formats = new ConcurrentHashMap<Locale, MessageFormat>();
        }

        void register(@NotNull Locale locale, @NotNull MessageFormat format) {
            if (this.formats.putIfAbsent(Objects.requireNonNull(locale, "locale"), Objects.requireNonNull(format, "message format")) != null) {
                throw new IllegalArgumentException(String.format("Translation already exists: %s for %s", this.key, locale));
            }
        }

        @Nullable
        MessageFormat translate(@NotNull Locale locale) {
            MessageFormat format = this.formats.get(Objects.requireNonNull(locale, "locale"));
            if (format == null && (format = this.formats.get(new Locale(locale.getLanguage()))) == null && (format = this.formats.get(TranslationRegistryImpl.this.defaultLocale)) == null) {
                format = this.formats.get(TranslationLocales.global());
            }
            return format;
        }

        @Override
        @NotNull
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("key", this.key), ExaminableProperty.of("formats", this.formats));
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Translation)) {
                return false;
            }
            Translation that = (Translation)other;
            return this.key.equals(that.key) && this.formats.equals(that.formats);
        }

        public int hashCode() {
            return Objects.hash(this.key, this.formats);
        }

        public String toString() {
            return Internals.toString(this);
        }
    }
}

