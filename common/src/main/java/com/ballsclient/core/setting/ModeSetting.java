package com.ballsclient.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Mode carousel setting selecting from a fixed list of discrete mode strings.
 */
public class ModeSetting extends Setting<String> {
    private final List<String> modes;

    public ModeSetting(String name, String description, String defaultMode, String... modes) {
        this(name, description, defaultMode, Arrays.asList(modes));
    }

    public ModeSetting(String name, String description, String defaultMode, List<String> modes) {
        super(name, description, defaultMode);
        if (modes == null || modes.isEmpty()) {
            throw new IllegalArgumentException("Modes list cannot be null or empty");
        }
        this.modes = Collections.unmodifiableList(modes);
        boolean found = false;
        for (String m : this.modes) {
            if (m.equalsIgnoreCase(defaultMode)) {
                found = true;
                super.setValue(m);
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Default mode '" + defaultMode + "' not found in modes: " + modes);
        }
    }

    public List<String> getModes() {
        return modes;
    }

    public boolean is(String mode) {
        return getValue() != null && getValue().equalsIgnoreCase(mode);
    }

    public void cycle() {
        int currentIndex = modes.indexOf(getValue());
        if (currentIndex == -1) {
            setValue(modes.get(0));
        } else {
            setValue(modes.get((currentIndex + 1) % modes.size()));
        }
    }

    @Override
    public void setValue(String newMode) {
        if (newMode != null) {
            for (String m : modes) {
                if (m.equalsIgnoreCase(newMode)) {
                    super.setValue(m);
                    return;
                }
            }
        }
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            try {
                setValue(element.getAsString());
            } catch (Exception ignored) {
            }
        }
    }
}
