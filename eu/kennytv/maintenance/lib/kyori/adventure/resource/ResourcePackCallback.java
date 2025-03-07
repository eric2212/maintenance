/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.resource;

import eu.kennytv.maintenance.lib.kyori.adventure.audience.Audience;
import eu.kennytv.maintenance.lib.kyori.adventure.resource.ResourcePackCallbacks;
import eu.kennytv.maintenance.lib.kyori.adventure.resource.ResourcePackStatus;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ResourcePackCallback {
    @NotNull
    public static ResourcePackCallback noOp() {
        return ResourcePackCallbacks.NO_OP;
    }

    @NotNull
    public static ResourcePackCallback onTerminal(@NotNull BiConsumer<UUID, Audience> success, @NotNull BiConsumer<UUID, Audience> failure) {
        return (uuid, status, audience) -> {
            if (status == ResourcePackStatus.SUCCESSFULLY_LOADED) {
                success.accept(uuid, audience);
            } else if (!status.intermediate()) {
                failure.accept(uuid, audience);
            }
        };
    }

    public void packEventReceived(@NotNull UUID var1, @NotNull ResourcePackStatus var2, @NotNull Audience var3);
}

