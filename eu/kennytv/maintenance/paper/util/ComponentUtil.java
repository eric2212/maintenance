/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  net.kyori.adventure.text.Component
 *  net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
 */
package eu.kennytv.maintenance.paper.util;

import com.google.gson.JsonElement;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public final class ComponentUtil {
    public static final boolean PAPER = ComponentUtil.isPaper();

    public static net.kyori.adventure.text.Component toPaperComponent(Component component) {
        JsonElement json = eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().serializeToTree(component);
        return GsonComponentSerializer.gson().deserializeFromTree(json);
    }

    public static String toLegacy(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    private static boolean isPaper() {
        try {
            Class<?> componentClass = Class.forName("net.kyori.adventure.text.Component");
            Class.forName("org.bukkit.entity.Player").getDeclaredMethod("kick", componentClass);
            return true;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return false;
        }
    }
}

