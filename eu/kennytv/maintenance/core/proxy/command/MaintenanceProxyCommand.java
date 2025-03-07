/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.core.proxy.command;

import eu.kennytv.maintenance.api.proxy.Server;
import eu.kennytv.maintenance.core.command.MaintenanceCommand;
import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import eu.kennytv.maintenance.core.proxy.SettingsProxy;
import eu.kennytv.maintenance.core.proxy.command.subcommand.AbortSingleTimerCommand;
import eu.kennytv.maintenance.core.proxy.command.subcommand.SingleEndtimerCommand;
import eu.kennytv.maintenance.core.proxy.command.subcommand.SingleScheduleTimerCommand;
import eu.kennytv.maintenance.core.proxy.command.subcommand.SingleStarttimerCommand;
import eu.kennytv.maintenance.core.proxy.command.subcommand.SingleToggleCommand;
import eu.kennytv.maintenance.core.proxy.command.subcommand.StatusCommand;
import eu.kennytv.maintenance.core.util.SenderInfo;
import java.util.ArrayList;
import java.util.List;

public abstract class MaintenanceProxyCommand
extends MaintenanceCommand {
    private final MaintenanceProxyPlugin plugin;
    private final SettingsProxy settingsBungee;

    protected MaintenanceProxyCommand(MaintenanceProxyPlugin plugin, SettingsProxy settings) {
        super(plugin, settings);
        this.plugin = plugin;
        this.settingsBungee = settings;
    }

    @Override
    protected void addToggleAndTimerCommands() {
        this.add(new SingleToggleCommand(this.plugin), "on", "off");
        this.add(new StatusCommand(this.plugin), "status");
        this.add(new SingleStarttimerCommand(this.plugin), "starttimer", "start");
        this.add(new SingleEndtimerCommand(this.plugin), "endtimer", "end");
        this.add(new SingleScheduleTimerCommand(this.plugin), "scheduletimer", "schedule");
        this.add(new AbortSingleTimerCommand(this.plugin), "aborttimer", "abort");
    }

    @Override
    public List<String> getMaintenanceServersCompletion(String s) {
        ArrayList<String> list = new ArrayList<String>();
        for (String server : this.settingsBungee.getMaintenanceServers()) {
            if (!server.toLowerCase().startsWith(s)) continue;
            list.add(server);
        }
        return list;
    }

    public Server checkSingleTimerServerArg(SenderInfo sender, String serverName) {
        Server server = this.plugin.getServer(serverName);
        if (server == null) {
            sender.send(this.settings.getMessage("serverNotFound", new String[0]));
            return null;
        }
        if (this.plugin.isServerTaskRunning(server)) {
            sender.send(this.settings.getMessage("singleTimerAlreadyRunning", new String[0]));
            return null;
        }
        return server;
    }
}

