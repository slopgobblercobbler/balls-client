package com.ballsclient.core.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Keybind setting mapping to GLFW keycodes.
 */
public class KeybindSetting extends Setting<Integer> {
    public static final int KEY_NONE = -1;

    public KeybindSetting(String name, String description, int defaultKeyCode) {
        super(name, description, defaultKeyCode);
    }

    public boolean isBound() {
        return getValue() != null && getValue() != KEY_NONE && getValue() != 0;
    }

    public boolean matches(int keyCode) {
        return isBound() && getValue() == keyCode;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            try {
                setValue(element.getAsInt());
            } catch (Exception ignored) {
            }
        }
    }
}
