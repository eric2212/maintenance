/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package eu.kennytv.maintenance.lib.kyori.adventure.text.event;

import eu.kennytv.maintenance.lib.kyori.adventure.audience.Audience;
import eu.kennytv.maintenance.lib.kyori.adventure.permission.PermissionChecker;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.ClickCallback;
import eu.kennytv.maintenance.lib.kyori.adventure.text.event.ClickEvent;
import eu.kennytv.maintenance.lib.kyori.adventure.util.Services;
import eu.kennytv.maintenance.lib.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;

final class ClickCallbackInternals {
    static final PermissionChecker ALWAYS_FALSE = PermissionChecker.always(TriState.FALSE);
    static final ClickCallback.Provider PROVIDER = Services.service(ClickCallback.Provider.class).orElseGet(Fallback::new);

    private ClickCallbackInternals() {
    }

    static final class Fallback
    implements ClickCallback.Provider {
        Fallback() {
        }

        @Override
        @NotNull
        public ClickEvent create(@NotNull ClickCallback<Audience> callback, @NotNull ClickCallback.Options options) {
            return ClickEvent.suggestCommand("Callbacks are not supported on this platform!");
        }
    }
}

