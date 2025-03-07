/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.command.subcommand;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.util.SenderInfo;
import eu.kennytv.maintenance.lib.kyori.adventure.text.Component;
import java.util.Map;
import java.util.UUID;

public final class WhitelistCommand
extends CommandInfo {
    public WhitelistCommand(MaintenancePlugin plugin) {
        super(plugin, "whitelist.list");
    }

    @Override
    public void execute(SenderInfo sender, String[] args) {
        if (this.checkArgs(sender, args, 1)) {
            return;
        }
        Map<UUID, String> players = this.getSettings().getWhitelistedPlayers();
        if (players.isEmpty()) {
            sender.send(this.getMessage("whitelistEmpty", new String[0]));
            return;
        }
        sender.send(this.getMessage("whitelistedPlayers", new String[0]));
        Component format = this.getMessage("whitelistedPlayersFormat", new String[0]);
        for (Map.Entry<UUID, String> entry : players.entrySet()) {
            sender.send(format.replaceText(builder -> builder.matchLiteral("%NAME%").replacement((String)entry.getValue())).replaceText(builder -> builder.matchLiteral("%UUID%").replacement(((UUID)entry.getKey()).toString())));
        }
        sender.send(Component.empty());
    }
}

