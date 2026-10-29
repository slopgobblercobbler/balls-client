package com.ballsclient.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class providing translation between keycodes and human-friendly display names.
 */
public final class KeyUtils {
    private static final Map<Integer, String> KEY_NAMES = new HashMap<>();

    static {
        KEY_NAMES.put(-1, "NONE");
        KEY_NAMES.put(0, "NONE");

        // Standard printable ASCII / GLFW
        KEY_NAMES.put(32, "SPACE");
        KEY_NAMES.put(39, "APOSTROPHE");
        KEY_NAMES.put(44, "COMMA");
        KEY_NAMES.put(45, "MINUS");
        KEY_NAMES.put(46, "PERIOD");
        KEY_NAMES.put(47, "SLASH");

        for (int i = 48; i <= 57; i++) {
            KEY_NAMES.put(i, String.valueOf((char) i));
        }

        KEY_NAMES.put(59, "SEMICOLON");
        KEY_NAMES.put(61, "EQUAL");

        for (int i = 65; i <= 90; i++) {
            KEY_NAMES.put(i, String.valueOf((char) i));
        }

        KEY_NAMES.put(91, "LBRACKET");
        KEY_NAMES.put(92, "BACKSLASH");
        KEY_NAMES.put(93, "RBRACKET");
        KEY_NAMES.put(96, "GRAVE");

        // Navigation & control
        KEY_NAMES.put(256, "ESCAPE");
        KEY_NAMES.put(257, "ENTER");
        KEY_NAMES.put(258, "TAB");
        KEY_NAMES.put(259, "BACKSPACE");
        KEY_NAMES.put(260, "INSERT");
        KEY_NAMES.put(261, "DELETE");
        KEY_NAMES.put(262, "RIGHT");
        KEY_NAMES.put(263, "LEFT");
        KEY_NAMES.put(264, "DOWN");
        KEY_NAMES.put(265, "UP");
        KEY_NAMES.put(266, "PAGE_UP");
        KEY_NAMES.put(267, "PAGE_DOWN");
        KEY_NAMES.put(268, "HOME");
        KEY_NAMES.put(269, "END");
        KEY_NAMES.put(280, "CAPS_LOCK");
        KEY_NAMES.put(281, "SCROLL_LOCK");
        KEY_NAMES.put(282, "NUM_LOCK");
        KEY_NAMES.put(283, "PRINT_SCREEN");
        KEY_NAMES.put(284, "PAUSE");

        // Function keys (GLFW 290..314)
        for (int i = 1; i <= 25; i++) {
            KEY_NAMES.put(289 + i, "F" + i);
        }

        // Keypad (GLFW 320..329)
        for (int i = 0; i <= 9; i++) {
            KEY_NAMES.put(320 + i, "NUMPAD_" + i);
        }
        KEY_NAMES.put(330, "NUMPAD_DECIMAL");
        KEY_NAMES.put(331, "NUMPAD_DIVIDE");
        KEY_NAMES.put(332, "NUMPAD_MULTIPLY");
        KEY_NAMES.put(333, "NUMPAD_SUBTRACT");
        KEY_NAMES.put(334, "NUMPAD_ADD");
        KEY_NAMES.put(335, "NUMPAD_ENTER");
        KEY_NAMES.put(336, "NUMPAD_EQUAL");

        // Modifiers (GLFW 340..348)
        KEY_NAMES.put(340, "LSHIFT");
        KEY_NAMES.put(341, "LCONTROL");
        KEY_NAMES.put(342, "LALT");
        KEY_NAMES.put(343, "LSUPER");
        KEY_NAMES.put(344, "RSHIFT");
        KEY_NAMES.put(345, "RCONTROL");
        KEY_NAMES.put(346, "RALT");
        KEY_NAMES.put(347, "RSUPER");
        KEY_NAMES.put(348, "MENU");

        // SDL Scancodes (aliases for SDL scancodes if passed directly)
        KEY_NAMES.put(224, "LCONTROL");
        KEY_NAMES.put(225, "LSHIFT");
        KEY_NAMES.put(226, "LALT");
        KEY_NAMES.put(227, "LGUI");
        KEY_NAMES.put(228, "RCONTROL");
        KEY_NAMES.put(229, "RSHIFT");
        KEY_NAMES.put(230, "RALT");
        KEY_NAMES.put(231, "RGUI");
    }

    private KeyUtils() {}

    /**
     * Converts a keycode to a human-friendly display string.
     *
     * @param keyCode The keycode or null
     * @return Display name (e.g. "NONE", "RSHIFT", "SPACE", "C", "B", "R")
     */
    public static String getKeyName(Integer keyCode) {
        if (keyCode == null || keyCode == -1 || keyCode == 0) {
            return "NONE";
        }
        String name = KEY_NAMES.get(keyCode);
        if (name != null) {
            return name;
        }
        if (keyCode >= 32 && keyCode <= 126) {
            return String.valueOf((char) keyCode.intValue()).toUpperCase();
        }
        return "KEY_" + keyCode;
    }
}
