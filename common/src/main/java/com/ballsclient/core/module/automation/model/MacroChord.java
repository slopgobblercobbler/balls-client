package com.ballsclient.core.module.automation.model;

import java.util.*;

/**
 * Macro multi-key chord representation.
 * Supports simultaneous key combination matching with configurable temporal windows.
 */
public class MacroChord {
    private final String name;
    private final Set<Integer> requiredKeys;
    private final long windowMs;

    public MacroChord(String name, Set<Integer> requiredKeys) {
        this(name, requiredKeys, 150L);
    }

    public MacroChord(String name, Set<Integer> requiredKeys, long windowMs) {
        this.name = name != null ? name : "UnnamedChord";
        this.requiredKeys = requiredKeys != null ? Collections.unmodifiableSet(new HashSet<>(requiredKeys)) : Collections.emptySet();
        this.windowMs = Math.max(0L, windowMs);
    }

    public String getName() {
        return name;
    }

    public Set<Integer> getRequiredKeys() {
        return requiredKeys;
    }

    public long getWindowMs() {
        return windowMs;
    }

    /**
     * Checks if all required chord keys are present in the currently pressed key set.
     */
    public boolean matches(Set<Integer> pressedKeys) {
        if (pressedKeys == null || requiredKeys.isEmpty()) {
            return false;
        }
        return pressedKeys.containsAll(requiredKeys);
    }

    public static boolean matchesChord(Set<Integer> currentlyPressedKeys, Set<Integer> requiredChordKeys) {
        if (currentlyPressedKeys == null || requiredChordKeys == null || requiredChordKeys.isEmpty()) {
            return false;
        }
        return currentlyPressedKeys.containsAll(requiredChordKeys);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MacroChord that = (MacroChord) o;
        return Objects.equals(requiredKeys, that.requiredKeys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredKeys);
    }
}
