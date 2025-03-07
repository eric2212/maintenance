/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package eu.kennytv.maintenance.core.config;

import eu.kennytv.maintenance.core.config.Config;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public class ConfigSection {
    protected final Config root;
    protected final String currentPath;
    protected Map<String, Object> values;

    ConfigSection(Config root, String currentPath) {
        this(root, currentPath, new HashMap<String, Object>());
    }

    ConfigSection(Config root, String currentPath, Map<String, Object> values) {
        this.root = root;
        this.currentPath = currentPath;
        this.values = values;
    }

    @Nullable
    public <E> E get(String key) {
        return (E)this.getObject(key);
    }

    @Nullable
    public <E> E get(String key, @Nullable E def) {
        Object o = this.getObject(key);
        return (E)(o != null ? o : def);
    }

    @Nullable
    public Object getObject(String key) {
        return this.getObject(key, null);
    }

    @Nullable
    public Object getObject(String key, Object def) {
        int sectionStartIndex;
        int nextSeparatorIndex = -1;
        ConfigSection section = this;
        while ((nextSeparatorIndex = key.indexOf(46, sectionStartIndex = nextSeparatorIndex + 1)) != -1) {
            if ((section = section.getSection(key.substring(sectionStartIndex, nextSeparatorIndex))) != null) continue;
            return def;
        }
        String subKey = key.substring(sectionStartIndex);
        if (section == this) {
            Object result = this.values.get(subKey);
            return result != null ? result : def;
        }
        return section.getObject(subKey, def);
    }

    @Nullable
    public <E> E getOrSet(String key, @Nullable E def) {
        Object o = this.getObject(key);
        if (o != null) {
            return (E)o;
        }
        this.set(key, def);
        return def;
    }

    @Nullable
    public ConfigSection getSection(String key) {
        Object o = this.getObject(key);
        if (!(o instanceof Map)) {
            return null;
        }
        return new ConfigSection(this.getRoot(), this.getFullKeyInPath(key), (Map)o);
    }

    public ConfigSection getOrCreateSection(String key) {
        Object o = this.getObject(key);
        if (!(o instanceof Map)) {
            this.set(key, new LinkedHashMap());
            return this.getSection(key);
        }
        return new ConfigSection(this.getRoot(), this.getFullKeyInPath(key), (Map)o);
    }

    public boolean contains(String key) {
        return this.getObject(key) != null;
    }

    public void set(String key, @Nullable Object value) {
        int sectionStartIndex;
        int nextSeparatorIndex = -1;
        ConfigSection section = this;
        while ((nextSeparatorIndex = key.indexOf(46, sectionStartIndex = nextSeparatorIndex + 1)) != -1) {
            section = section.getOrCreateSection(key.substring(sectionStartIndex, nextSeparatorIndex));
        }
        String sectionKey = key.substring(sectionStartIndex);
        if (value == null) {
            section.values.remove(sectionKey);
            this.getRoot().getComments().remove(this.getFullKeyInPath(key));
        } else {
            section.values.put(sectionKey, value);
        }
    }

    public void move(String key, String toKey) {
        Object o = this.getObject(key);
        if (o != null) {
            this.remove(key);
            this.set(toKey, o);
        }
    }

    public void remove(String key) {
        this.set(key, null);
    }

    public Set<String> getKeys() {
        return this.values.keySet();
    }

    public Map<String, Object> getValues() {
        return this.values;
    }

    public boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        Object o = this.getObject(key);
        return o instanceof Boolean ? (Boolean)o : def;
    }

    @Nullable
    public String getString(String key) {
        return (String)this.get(key);
    }

    @Nullable
    public String getString(String key, @Nullable String def) {
        Object o = this.getObject(key);
        return o instanceof String ? (String)o : def;
    }

    public int getInt(String key) {
        return this.getInt(key, 0);
    }

    public int getInt(String key, int def) {
        Object o = this.getObject(key);
        return o instanceof Number ? ((Number)o).intValue() : def;
    }

    public double getDouble(String key) {
        return this.getDouble(key, 0.0);
    }

    public double getDouble(String key, double def) {
        Object o = this.getObject(key);
        return o instanceof Number ? ((Number)o).doubleValue() : def;
    }

    public long getLong(String key) {
        return this.getLong(key, 0L);
    }

    public long getLong(String key, long def) {
        Object o = this.getObject(key);
        return o instanceof Number ? ((Number)o).longValue() : def;
    }

    @Nullable
    public List<String> getStringList(String key) {
        return (List)this.get(key);
    }

    @Nullable
    public List<String> getStringList(String key, @Nullable List<String> def) {
        List<String> list = (List<String>)this.get(key);
        return list != null ? list : def;
    }

    @Nullable
    public List<Integer> getIntList(String key) {
        return (List)this.get(key);
    }

    @Nullable
    public List<Integer> getIntList(String key, @Nullable List<Integer> def) {
        List<Integer> list = (List<Integer>)this.get(key);
        return list != null ? list : def;
    }

    public boolean addMissingFields(ConfigSection fromSection) {
        boolean changed = false;
        for (Map.Entry<String, Object> entry : fromSection.values.entrySet()) {
            String key = entry.getKey();
            Object value = this.values.get(key);
            if (value != null) {
                Object newValue = entry.getValue();
                if (!(value instanceof Map) || !(newValue instanceof Map)) continue;
                changed |= this.getSection(key).addMissingFields(fromSection.getSection(key));
                continue;
            }
            this.values.put(key, entry.getValue());
            changed = true;
        }
        return changed;
    }

    public Config getRoot() {
        return this.root;
    }

    public String getCurrentPath() {
        return this.currentPath;
    }

    protected String getFullKeyInPath(String key) {
        return this.currentPath.isEmpty() ? key : this.currentPath + "." + key;
    }
}

