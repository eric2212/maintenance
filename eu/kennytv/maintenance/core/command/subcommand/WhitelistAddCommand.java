/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.command.subcommand;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.util.DummySenderInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public final class WhitelistAddCommand
extends CommandInfo {
    public WhitelistAddCommand(MaintenancePlugin plugin) {
        super(plugin, "whitelist.add");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (args.length == 2) {
            if (args[1].length() == 36) {
                UUID uuid = this.plugin.checkUUID(sender, args[1]);
                if (uuid != null) {
                    this.addPlayerToWhitelist(sender, uuid);
                }
            } else {
                this.addPlayerToWhitelist(sender, args[1]);
            }
        } else if (args.length == 3) {
            if (args[1].length() != 36) {
                sender.send(this.getHelpMessage());
                return;
            }
            UUID uuid = this.plugin.checkUUID(sender, args[1]);
            if (uuid != null) {
                this.addPlayerToWhitelist(sender, new DummySenderInfo(uuid, args[2]));
            }
        } else {
            sender.send(this.getHelpMessage());
        }
    }

    @Override
    public List<String> getTabCompletion(SenderInfo sender, String[] args) {
        return args.length == 2 ? this.plugin.getCommandManager().getPlayersCompletion() : Collections.emptyList();
    }

    private void addPlayerToWhitelist(SenderInfo sender, String name) {
        this.plugin.getOfflinePlayer(name).whenComplete((selected, ex) -> {
            if (ex != null) {
                this.plugin.getLogger().log(Level.SEVERE, "Error while fetching offline player", (Throwable)ex);
                sender.send(this.getMessage("offlinePlayerFetchError", new String[0]));
                return;
            }
            if (selected == null) {
                sender.send(this.getMessage("playerNotOnline", new String[0]));
                return;
            }
            this.addPlayerToWhitelist(sender, (SenderInfo)selected);
        });
    }

    private void addPlayerToWhitelist(SenderInfo sender, UUID uuid) {
        this.plugin.getOfflinePlayer(uuid).whenComplete((selected, ex) -> {
            if (ex != null) {
                this.plugin.getLogger().log(Level.SEVERE, "Error while fetching offline player", (Throwable)ex);
                sender.send(this.getMessage("offlinePlayerFetchError", new String[0]));
                return;
            }
            if (selected == null) {
                sender.send(this.getMessage("playerNotFoundUuid", new String[0]));
                return;
            }
            this.addPlayerToWhitelist(sender, (SenderInfo)selected);
        });
    }

    private void addPlayerToWhitelist(SenderInfo sender, SenderInfo selected) {
        if (this.getSettings().addWhitelistedPlayer(selected.getUuid(), selected.getName())) {
            sender.send(this.getMessage("whitelistAdded", "%PLAYER%", selected.getName()));
        } else {
            sender.send(this.getMessage("whitelistAlreadyAdded", "%PLAYER%", selected.getName()));
        }
    }
}

