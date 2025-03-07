/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.luckperms.api.LuckPermsProvider
 *  net.luckperms.api.context.ContextCalculator
 *  net.luckperms.api.context.ContextConsumer
 *  net.luckperms.api.context.ContextSet
 *  net.luckperms.api.context.ImmutableContextSet
 *  net.luckperms.api.context.ImmutableContextSet$Builder
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.core.proxy.hook;

import eu.kennytv.maintenance.core.proxy.MaintenanceProxyPlugin;
import java.util.function.Function;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import org.jetbrains.annotations.NotNull;

public final class LuckPermsProxyHook {
    public static <T> void register(MaintenanceProxyPlugin plugin, Function<T, String> function) {
        LuckPermsProvider.get().getContextManager().registerCalculator(new MaintenanceProxyCalculator(plugin, function));
    }

    public static final class MaintenanceProxyCalculator<T>
    implements ContextCalculator<T> {
        private static final String MAINTENANCE_ON_KEY = "is-maintenance-on-";
        private static final String ON_MAINTENANCE_SERVER_KEY = "on-maintenance-server";
        private final MaintenanceProxyPlugin maintenance;
        private final Function<T, String> playerServerFunction;

        private MaintenanceProxyCalculator(MaintenanceProxyPlugin plugin, Function<T, String> playerServerFunction) {
            this.maintenance = plugin;
            this.playerServerFunction = playerServerFunction;
        }

        public void calculate(@NotNull T target, @NotNull ContextConsumer consumer) {
            consumer.accept("is-maintenance", Boolean.toString(this.maintenance.isMaintenance()));
            String server = this.playerServerFunction.apply(target);
            if (server != null) {
                String value = Boolean.toString(this.maintenance.getSettingsProxy().isMaintenance(server));
                consumer.accept(MAINTENANCE_ON_KEY + server, value);
                consumer.accept(ON_MAINTENANCE_SERVER_KEY, value);
            }
        }

        public ContextSet estimatePotentialContexts() {
            ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
            for (String serverName : this.maintenance.getServers()) {
                String key = MAINTENANCE_ON_KEY + serverName;
                builder.add(key, "true").add(key, "false");
            }
            return builder.add(ON_MAINTENANCE_SERVER_KEY, "true").add(ON_MAINTENANCE_SERVER_KEY, "false").add("is-maintenance", "true").add("is-maintenance", "false").build();
        }
    }
}

