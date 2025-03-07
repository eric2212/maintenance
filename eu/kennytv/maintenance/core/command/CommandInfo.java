/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.core.command;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public abstract class CommandInfo {
    protected final MaintenancePlugin plugin;
    private final String helpMessageKey;
    private final String permission;
    private final boolean visible;

    protected CommandInfo(MaintenancePlugin plugin, @Nullable String permission) {
        this(plugin, permission, true);
    }

    protected CommandInfo(MaintenancePlugin plugin, @Nullable String permission, boolean visible) {
        this.plugin = plugin;
        this.permission = permission;
        this.helpMessageKey = "help" + this.getClass().getSimpleName().replace("Command", "");
        this.visible = visible;
    }

    public boolean hasPermission(SenderInfo sender) {
        return sender.hasMaintenancePermission(this.permission);
    }

    public boolean isVisible() {
        return this.visible;
    }

    public Component getHelpMessage() {
        return this.getMessage(this.helpMessageKey, new String[0]);
    }

    public List<String> getTabCompletion(SenderInfo sender, String[] args) {
        return Collections.emptyList();
    }

    public abstract void execute(SenderInfo var1, String[] var2);

    protected boolean checkPermission(SenderInfo sender, String permission) {
        if (!sender.hasMaintenancePermission(permission)) {
            sender.send(this.getMessage("noPermission", new String[0]));
            return true;
        }
        return false;
    }

    protected boolean checkArgs(SenderInfo sender, String[] args, int length) {
        if (args.length != length) {
            sender.send(this.getHelpMessage());
            return true;
        }
        return false;
    }

    protected Component getMessage(String path, String ... replacements) {
        return this.plugin.getSettings().getMessage(path, replacements);
    }

    protected Settings getSettings() {
        return this.plugin.getSettings();
    }
}

