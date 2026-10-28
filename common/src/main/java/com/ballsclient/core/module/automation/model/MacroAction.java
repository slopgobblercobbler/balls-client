package com.ballsclient.core.module.automation.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Single executable step in a macro action queue.
 */
public record MacroAction(CommandType type, List<String> args) {

    public enum CommandType {
        WAIT,
        CLICK,
        KEYPRESS,
        KEYDOWN,
        SEND,
        LOOK,
        SELECT,
        LOOP
    }

    public MacroAction {
        Objects.requireNonNull(type, "CommandType cannot be null");
        args = args != null ? Collections.unmodifiableList(args) : Collections.emptyList();
    }

    public String getFirstArg() {
        return !args.isEmpty() ? args.get(0) : "";
    }

    public String getArg(int index, String defaultValue) {
        if (index >= 0 && index < args.size()) {
            return args.get(index);
        }
        return defaultValue;
    }
}
