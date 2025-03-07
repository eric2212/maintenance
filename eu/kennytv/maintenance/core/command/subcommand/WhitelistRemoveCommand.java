/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.command.subcommand;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class WhitelistRemoveCommand
extends CommandInfo {
    public WhitelistRemoveCommand(MaintenancePlugin plugin) {
        super(plugin, "whitelist.remove");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (this.checkArgs(sender, args, 2)) {
            return;
        }
        if (args[1].length() == 36) {
            UUID uuid = this.plugin.checkUUID(sender, args[1]);
            if (uuid != null) {
                this.removePlayerFromWhitelist(sender, uuid);
            }
        } else {
            this.removePlayerFromWhitelist(sender, args[1]);
        }
    }

    @Override
    public List<String> getTabCompletion(SenderInfo sender, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }
        ArrayList<String> list = new ArrayList<String>();
        String matchNameLowerCase = args[1].toLowerCase();
        for (String name : this.getSettings().getWhitelistedPlayers().values()) {
            if (!name.toLowerCase().startsWith(matchNameLowerCase)) continue;
            list.add(name);
        }
        return list;
    }

    private void removePlayerFromWhitelist(SenderInfo sender, String name) {
        this.plugin.getOfflinePlayer(name).whenComplete((selected, ex) -> {
            if (selected == null) {
                if (this.getSettings().removeWhitelistedPlayer(name)) {
                    sender.send(this.getMessage("whitelistRemoved", "%PLAYER%", name));
                } else {
                    sender.send(this.getMessage("whitelistNotFound", new String[0]));
                }
                return;
            }
            this.removePlayerFromWhitelist(sender, selected.getUuid(), selected.getName());
        });
    }

    private void removePlayerFromWhitelist(SenderInfo sender, UUID uuid) {
        this.plugin.getOfflinePlayer(uuid).whenComplete((selected, ex) -> {
            if (selected == null) {
                this.removePlayerFromWhitelist(sender, uuid, uuid.toString());
            } else {
                this.removePlayerFromWhitelist(sender, selected.getUuid(), selected.getName());
            }
        });
    }

    private void removePlayerFromWhitelist(SenderInfo sender, UUID uuid, String toReplace) {
        if (this.getSettings().removeWhitelistedPlayer(uuid)) {
            sender.send(this.getMessage("whitelistRemoved", "%PLAYER%", toReplace));
        } else {
            sender.send(this.getMessage("whitelistNotFound", new String[0]));
        }
    }
}

