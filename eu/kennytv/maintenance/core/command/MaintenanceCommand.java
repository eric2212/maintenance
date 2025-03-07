/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.core.command;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.core.command.CommandInfo;
import eu.kennytv.maintenance.core.command.subcommand.AbortTimerCommand;
import eu.kennytv.maintenance.core.command.subcommand.DebugCommand;
import eu.kennytv.maintenance.core.command.subcommand.DumpCommand;
import eu.kennytv.maintenance.core.command.subcommand.EndtimerCommand;
import eu.kennytv.maintenance.core.command.subcommand.HelpCommand;
import eu.kennytv.maintenance.core.command.subcommand.MotdCommand;
import eu.kennytv.maintenance.core.command.subcommand.ReloadCommand;
import eu.kennytv.maintenance.core.command.subcommand.RemoveMotdCommand;
import eu.kennytv.maintenance.core.command.subcommand.ScheduleTimerCommand;
import eu.kennytv.maintenance.core.command.subcommand.SetMotdCommand;
import eu.kennytv.maintenance.core.command.subcommand.StarttimerCommand;
import eu.kennytv.maintenance.core.command.subcommand.ToggleCommand;
import eu.kennytv.maintenance.core.command.subcommand.UpdateCommand;
import eu.kennytv.maintenance.core.command.subcommand.WhitelistAddCommand;
import eu.kennytv.maintenance.core.command.subcommand.WhitelistCommand;
import eu.kennytv.maintenance.core.command.subcommand.WhitelistRemoveCommand;
import eu.kennytv.maintenance.core.util.SenderInfo;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.Nullable;

public abstract class MaintenanceCommand {
    private static final long MAX_TASK_DURATION_SECONDS = TimeUnit.DAYS.toSeconds(28L);
    protected final MaintenancePlugin plugin;
    protected final Settings settings;
    private final Map<String, CommandInfo> commandExecutors = new LinkedHashMap<String, CommandInfo>();
    private final List<CommandInfo> commands = new ArrayList<CommandInfo>();
    private final HelpCommand help;

    protected MaintenanceCommand(MaintenancePlugin plugin, Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
        this.help = new HelpCommand(plugin);
    }

    protected void registerCommands() {
        this.add(this.help, "help");
        this.add(new ReloadCommand(this.plugin), "reload");
        this.addToggleAndTimerCommands();
        this.add(new WhitelistCommand(this.plugin), "whitelist");
        this.add(new WhitelistAddCommand(this.plugin), "add");
        this.add(new WhitelistRemoveCommand(this.plugin), "remove");
        this.add(new SetMotdCommand(this.plugin), "setmotd");
        this.add(new RemoveMotdCommand(this.plugin), "removemotd");
        this.add(new MotdCommand(this.plugin), "motd");
        this.add(new UpdateCommand(this.plugin), "update", "forceupdate");
        this.add(new DumpCommand(this.plugin), "dump");
        this.add(new DebugCommand(this.plugin), "debug");
    }

    protected void add(CommandInfo command, String ... aliases) {
        this.commands.add(command);
        for (String alias : aliases) {
            this.commandExecutors.put(alias, command);
        }
    }

    public void execute(SenderInfo sender, String[] args) {
        if (!sender.hasMaintenancePermission("command")) {
            sender.send(this.settings.getMessage("noPermission", new String[0]));
            return;
        }
        if (args.length == 0) {
            this.help.sendUsage(sender);
            return;
        }
        CommandInfo command = this.commandExecutors.get(args[0].toLowerCase());
        if (command == null) {
            this.help.sendUsage(sender);
            return;
        }
        if (!command.hasPermission(sender)) {
            sender.send(this.settings.getMessage("noPermission", new String[0]));
            return;
        }
        command.execute(sender, args);
    }

    public List<String> getSuggestions(SenderInfo sender, String[] args) {
        if (!sender.hasMaintenancePermission("command") || args.length == 0) {
            return Collections.emptyList();
        }
        String s = args[0].toLowerCase();
        if (args.length == 1) {
            ArrayList<String> list = new ArrayList<String>();
            for (Map.Entry<String, CommandInfo> entry : this.commandExecutors.entrySet()) {
                String command = entry.getKey();
                CommandInfo info = entry.getValue();
                if (!info.isVisible() || !command.startsWith(s) || !info.hasPermission(sender)) continue;
                list.add(command);
            }
            return list;
        }
        CommandInfo info = this.commandExecutors.get(args[0]);
        return info != null && info.hasPermission(sender) ? info.getTabCompletion(sender, args) : Collections.emptyList();
    }

    @Nullable
    public Duration parseDurationAndCheckTask(SenderInfo sender, String time, boolean taskCheck) {
        Duration duration;
        if (this.plugin.isNumeric(time)) {
            duration = Duration.ofMinutes(Integer.parseInt(time));
        } else {
            try {
                duration = Duration.parse("PT" + time.toUpperCase(Locale.ROOT));
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        if (taskCheck && this.plugin.isTaskRunning()) {
            sender.send(this.settings.getMessage("timerAlreadyRunning", new String[0]));
            return null;
        }
        long seconds = duration.getSeconds();
        if (seconds > MAX_TASK_DURATION_SECONDS) {
            sender.send(this.settings.getMessage("timerTooLong", new String[0]));
            return null;
        }
        if (seconds < 1L) {
            sender.sendRich("<i><dark_gray>[kennytv whispers to you] <gray>Think about running a timer for a negative amount of minutes. Doesn't work <b>that</b> <gray>well.");
            return null;
        }
        return duration;
    }

    @Nullable
    public Duration parseDurationAndCheckTask(SenderInfo sender, String time) {
        return this.parseDurationAndCheckTask(sender, time, true);
    }

    protected void addToggleAndTimerCommands() {
        this.add(new ToggleCommand(this.plugin), "on", "off");
        this.add(new StarttimerCommand(this.plugin), "starttimer", "start");
        this.add(new EndtimerCommand(this.plugin), "endtimer", "end");
        this.add(new ScheduleTimerCommand(this.plugin), "scheduletimer", "schedule");
        this.add(new AbortTimerCommand(this.plugin), "aborttimer", "abort");
    }

    public List<String> getServersCompletion(String s) {
        return null;
    }

    public List<String> getMaintenanceServersCompletion(String s) {
        return null;
    }

    public List<String> getPlayersCompletion() {
        return null;
    }

    public List<CommandInfo> getCommands() {
        return this.commands;
    }
}

