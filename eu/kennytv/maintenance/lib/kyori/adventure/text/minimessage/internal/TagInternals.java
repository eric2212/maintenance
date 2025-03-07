/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.intellij.lang.annotations.RegExp
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.internal;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.TagPattern;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class TagInternals {
    @RegExp
    public static final String TAG_NAME_REGEX = "[!?#]?[a-z0-9_-]*";
    private static final Pattern TAG_NAME_PATTERN = Pattern.compile("[!?#]?[a-z0-9_-]*");

    private TagInternals() {
    }

    public static void assertValidTagName(@TagPattern @NotNull String tagName) {
        if (!TAG_NAME_PATTERN.matcher(Objects.requireNonNull(tagName)).matches()) {
            throw new IllegalArgumentException("Tag name must match pattern " + TAG_NAME_PATTERN.pattern() + ", was " + tagName);
        }
    }

    public static boolean sanitizeAndCheckValidTagName(@TagPattern @NotNull String tagName) {
        return TAG_NAME_PATTERN.matcher(Objects.requireNonNull(tagName).toLowerCase(Locale.ROOT)).matches();
    }

    public static void sanitizeAndAssertValidTagName(@TagPattern @NotNull String tagName) {
        TagInternals.assertValidTagName(Objects.requireNonNull(tagName).toLowerCase(Locale.ROOT));
    }
}

