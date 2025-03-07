/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.lib.kyori.adventure.platform.bungeecord;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.jetbrains.annotations.Nullable;

final class BungeeReflection {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private BungeeReflection() {
    }

    public static boolean hasMethod(@Nullable Class<?> holderClass, String methodName, Class<?> ... parameters) {
        if (holderClass == null) {
            return false;
        }
        for (Class<?> parameter : parameters) {
            if (parameter != null) continue;
            return false;
        }
        try {
            holderClass.getMethod(methodName, parameters);
            return true;
        } catch (NoSuchMethodException noSuchMethodException) {
            return false;
        }
    }

    public static MethodHandle findMethod(@Nullable Class<?> holderClass, String methodName, Class<?> returnType, Class<?> ... parameters) {
        if (holderClass == null || returnType == null) {
            return null;
        }
        for (Class<?> parameter : parameters) {
            if (parameter != null) continue;
            return null;
        }
        try {
            return LOOKUP.findVirtual(holderClass, methodName, MethodType.methodType(returnType, parameters));
        } catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException) {
            return null;
        }
    }
}

