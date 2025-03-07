/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.TextComponent
 *  net.md_5.bungee.chat.ComponentSerializer
 */
package eu.kennytv.maintenance.bungee.util;

import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import eu.kennytv.maintenance.lib.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public final class ComponentUtil {
    public static BaseComponent[] toBadComponent(Component component) {
        return ComponentSerializer.parse((String)((String)GsonComponentSerializer.gson().serialize(component)));
    }

    public static BaseComponent toBadComponents(Component component) {
        return new TextComponent(ComponentSerializer.parse((String)((String)GsonComponentSerializer.gson().serialize(component))));
    }
}

