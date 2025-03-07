/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.yaml.snakeyaml.Yaml
 */
package eu.kennytv.maintenance.core.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.Yaml;

public final class ConfigSerializer {
    private static final String[] EMPTY = new String[0];
    private static final String PATH_SEPARATOR_STRING = ".";
    private static final String PATH_SEPARATOR_QUOTED = Pattern.quote(".");
    private static final int INDENT_UNIT = 2;

    public static String serialize(String header, Map<String, Object> data, Map<String, String[]> comments, Yaml yaml) {
        if (data.isEmpty()) {
            return yaml.dump(null);
        }
        String rawYaml = yaml.dump(data);
        StringBuilder fileData = new StringBuilder();
        int currentKeyIndents = 0;
        String key = "";
        for (String line : rawYaml.split("\n")) {
            String[] strings;
            boolean keyLine;
            if (line.isEmpty()) continue;
            int indent = ConfigSerializer.getIndents(line);
            int indents = indent / 2;
            String substring = line.substring(indent);
            if (substring.trim().isEmpty() || substring.charAt(0) == '-') {
                keyLine = false;
            } else if (indents <= currentKeyIndents) {
                String[] array = key.split(PATH_SEPARATOR_QUOTED);
                int backspace = currentKeyIndents - indents + 1;
                key = ConfigSerializer.join(array, array.length - backspace);
                keyLine = true;
            } else {
                boolean bl = keyLine = line.indexOf(58) != -1;
            }
            if (!keyLine) {
                fileData.append(line).append('\n');
                continue;
            }
            String newKey = substring.split(Pattern.quote(":"))[0];
            if (!key.isEmpty()) {
                key = key + PATH_SEPARATOR_STRING;
            }
            key = key + newKey;
            if (comments != null && (strings = comments.get(key)) != null) {
                String indentText = indent > 0 ? line.substring(0, indent) : "";
                for (String comment : strings) {
                    if (comment.isEmpty()) {
                        fileData.append('\n');
                        continue;
                    }
                    fileData.append(indentText).append(comment).append('\n');
                }
            }
            currentKeyIndents = indents;
            fileData.append(line).append('\n');
        }
        return header != null && !header.isEmpty() ? header + fileData : fileData.toString();
    }

    public static Map<String, String[]> deserializeComments(String data) {
        HashMap<String, String[]> comments = new HashMap<String, String[]>();
        ArrayList<String> currentComments = new ArrayList<String>();
        boolean header = true;
        boolean multiLineValue = false;
        int currentIndents = 0;
        String key = "";
        for (String line : data.split("\n")) {
            String s = line.trim();
            if (s.startsWith("#")) {
                currentComments.add(s);
                continue;
            }
            if (header) {
                if (!currentComments.isEmpty()) {
                    currentComments.add("");
                    comments.put(".header", currentComments.toArray(EMPTY));
                    currentComments.clear();
                }
                header = false;
            }
            if (s.isEmpty()) {
                currentComments.add(s);
                continue;
            }
            if (s.startsWith("- |")) {
                multiLineValue = true;
                continue;
            }
            int indent = ConfigSerializer.getIndents(line);
            int indents = indent / 2;
            if (multiLineValue) {
                if (indents > currentIndents) continue;
                multiLineValue = false;
            }
            if (indents <= currentIndents) {
                int backspace;
                String[] array = key.split(PATH_SEPARATOR_QUOTED);
                int delta = array.length - (backspace = currentIndents - indents + 1);
                key = delta >= 0 ? ConfigSerializer.join(array, delta) : key;
            }
            String separator = key.isEmpty() ? "" : PATH_SEPARATOR_STRING;
            String lineKey = line.indexOf(58) != -1 ? line.split(Pattern.quote(":"))[0] : line;
            key = key + separator + lineKey.substring(indent);
            currentIndents = indents;
            if (currentComments.isEmpty()) continue;
            comments.put(key, currentComments.toArray(EMPTY));
            currentComments.clear();
        }
        return comments;
    }

    private static int getIndents(String line) {
        int count = 0;
        for (int i = 0; i < line.length() && line.charAt(i) == ' '; ++i) {
            ++count;
        }
        return count;
    }

    private static String join(String[] array, int length) {
        CharSequence[] copy = new String[length];
        System.arraycopy(array, 0, copy, 0, length);
        return String.join((CharSequence)PATH_SEPARATOR_STRING, copy);
    }
}

