/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.luckperms.api.LuckPermsProvider
 *  net.luckperms.api.context.ContextCalculator
 *  net.luckperms.api.context.ContextConsumer
 *  net.luckperms.api.context.ContextSet
 *  net.luckperms.api.context.ImmutableContextSet
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.core.hook;

import eu.kennytv.maintenance.api.Maintenance;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import org.jetbrains.annotations.NotNull;

public final class LuckPermsHook {
    public static <T> void register(Maintenance maintenance) {
        LuckPermsProvider.get().getContextManager().registerCalculator(new MaintenanceCalculator(maintenance));
    }

    public static final class MaintenanceCalculator<T>
    implements ContextCalculator<T> {
        public static final String MAINTENANCE_KEY = "is-maintenance";
        private final Maintenance maintenance;

        private MaintenanceCalculator(Maintenance maintenance) {
            this.maintenance = maintenance;
        }

        public void calculate(@NotNull T target, @NotNull ContextConsumer consumer) {
            consumer.accept(MAINTENANCE_KEY, Boolean.toString(this.maintenance.isMaintenance()));
        }

        public ContextSet estimatePotentialContexts() {
            return ImmutableContextSet.builder().add(MAINTENANCE_KEY, "true").add(MAINTENANCE_KEY, "false").build();
        }
    }
}

