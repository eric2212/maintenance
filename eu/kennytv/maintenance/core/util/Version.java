/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.core.util;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Nullable;

public final class Version
implements Comparable<Version> {
    private final int[] parts;
    private final String version;
    private final String tag;

    public Version(String version) {
        Preconditions.checkArgument(version != null && !version.isEmpty());
        int index = version.indexOf(45);
        String[] split = (index == -1 ? version : version.substring(0, index)).split("\\.");
        this.parts = new int[split.length];
        for (int i = 0; i < split.length; ++i) {
            this.parts[i] = Integer.parseInt(split[i]);
        }
        this.version = version;
        this.tag = index != -1 ? version.substring(index + 1) : "";
    }

    @Override
    public int compareTo(@Nullable Version version) {
        if (version == null) {
            return 0;
        }
        int max = Math.max(this.parts.length, version.parts.length);
        for (int i = 0; i < max; ++i) {
            int partB;
            int partA = i < this.parts.length ? this.parts[i] : 0;
            int n = partB = i < version.parts.length ? version.parts[i] : 0;
            if (partA < partB) {
                return -1;
            }
            if (partA <= partB) continue;
            return 1;
        }
        if (this.tag.isEmpty() && !version.tag.isEmpty()) {
            return 1;
        }
        if (!this.tag.isEmpty() && version.tag.isEmpty()) {
            return -1;
        }
        return 0;
    }

    public String getTag() {
        return this.tag;
    }

    public String toString() {
        return this.version;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Version)) {
            return false;
        }
        Version other = (Version)o;
        return this.version.equals(other.version);
    }
}

