package com.ballsclient.core.module.automation.model;

import java.util.*;

/**
 * High-performance DSL tokenizer, parser, and action queue execution engine.
 * Supports scripted sequences with instruction types:
 * WAIT, CLICK, KEYPRESS, KEYDOWN, SEND, LOOK, SELECT, LOOP.
 */
public class MacroScriptEngine {

    private final Queue<MacroAction> actionQueue = new ArrayDeque<>();
    private int waitTicksRemaining = 0;
    private boolean running = false;

    public MacroScriptEngine() {
    }

    /**
     * Tokenizes a DSL macro script into a sequential list of MacroAction steps.
     * Supports semicolon-separated or newline-separated commands, and '#' or '//' comments.
     */
    public static List<MacroAction> tokenizeScript(String script) {
        if (script == null || script.isBlank()) {
            return Collections.emptyList();
        }

        List<MacroAction> steps = new ArrayList<>();
        String[] lines = script.split("[;\n]");
        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) {
                continue;
            }

            String[] parts = line.split("\\s+");
            if (parts.length == 0 || parts[0].isBlank()) {
                continue;
            }

            try {
                MacroAction.CommandType type = MacroAction.CommandType.valueOf(parts[0].toUpperCase(Locale.ROOT));
                List<String> args = new ArrayList<>(parts.length - 1);
                for (int i = 1; i < parts.length; i++) {
                    args.add(parts[i]);
                }
                steps.add(new MacroAction(type, args));
            } catch (IllegalArgumentException ignored) {
                // Ignore unrecognized command tokens gracefully
            }
        }
        return steps;
    }

    /**
     * Enqueues a parsed macro action into the execution queue.
     */
    public synchronized void enqueue(MacroAction action) {
        if (action != null) {
            actionQueue.offer(action);
        }
    }

    /**
     * Parses and enqueues an entire DSL script.
     */
    public synchronized void enqueueScript(String script) {
        List<MacroAction> actions = tokenizeScript(script);
        for (MacroAction a : actions) {
            actionQueue.offer(a);
        }
        if (!actionQueue.isEmpty()) {
            running = true;
        }
    }

    /**
     * Advances the macro engine by one client tick (50 ms).
     *
     * @return the MacroAction executed this tick, or null if idle or waiting
     */
    public synchronized MacroAction tick() {
        if (waitTicksRemaining > 0) {
            waitTicksRemaining--;
            return null;
        }

        MacroAction action = actionQueue.poll();
        if (action == null) {
            running = false;
            return null;
        }

        if (action.type() == MacroAction.CommandType.WAIT) {
            try {
                int ticks = Integer.parseInt(action.getFirstArg());
                if (ticks > 1) {
                    waitTicksRemaining = ticks - 1;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        return action;
    }

    public synchronized void clear() {
        actionQueue.clear();
        waitTicksRemaining = 0;
        running = false;
    }

    public synchronized int getRemainingActionCount() {
        return actionQueue.size();
    }

    public synchronized boolean isRunning() {
        return running || !actionQueue.isEmpty() || waitTicksRemaining > 0;
    }
}
