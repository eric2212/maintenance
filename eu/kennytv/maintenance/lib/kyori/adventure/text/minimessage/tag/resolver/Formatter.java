/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver;

import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.Tag;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.TagPattern;
import eu.kennytv.maintenance.lib.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public final class Formatter {
    private Formatter() {
    }

    @NotNull
    public static TagResolver number(@TagPattern @NotNull String key, @NotNull Number number) {
        return TagResolver.resolver(key, (argumentQueue, context) -> {
            NumberFormat decimalFormat;
            if (argumentQueue.hasNext()) {
                String locale = argumentQueue.pop().value();
                if (argumentQueue.hasNext()) {
                    String format = argumentQueue.pop().value();
                    decimalFormat = new DecimalFormat(format, new DecimalFormatSymbols(Locale.forLanguageTag(locale)));
                } else {
                    decimalFormat = locale.contains(".") ? new DecimalFormat(locale, DecimalFormatSymbols.getInstance()) : DecimalFormat.getInstance(Locale.forLanguageTag(locale));
                }
            } else {
                decimalFormat = DecimalFormat.getInstance();
            }
            return Tag.inserting(context.deserialize(decimalFormat.format(number)));
        });
    }

    @NotNull
    public static TagResolver date(@TagPattern @NotNull String key, @NotNull TemporalAccessor time) {
        return TagResolver.resolver(key, (argumentQueue, context) -> {
            String format = argumentQueue.popOr("Format expected.").value();
            return Tag.inserting(context.deserialize(DateTimeFormatter.ofPattern(format).format(time)));
        });
    }

    @NotNull
    public static TagResolver choice(@TagPattern @NotNull String key, Number number) {
        return TagResolver.resolver(key, (argumentQueue, context) -> {
            String format = argumentQueue.popOr("Format expected.").value();
            ChoiceFormat choiceFormat = new ChoiceFormat(format);
            return Tag.inserting(context.deserialize(choiceFormat.format(number)));
        });
    }

    public static TagResolver booleanChoice(@TagPattern @NotNull String key, boolean value) {
        return TagResolver.resolver(key, (argumentQueue, context) -> {
            String trueCase = argumentQueue.popOr("True format expected.").value();
            String falseCase = argumentQueue.popOr("False format expected.").value();
            return Tag.inserting(context.deserialize(value ? trueCase : falseCase));
        });
    }
}

