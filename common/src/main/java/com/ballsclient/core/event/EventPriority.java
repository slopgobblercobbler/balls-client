package com.ballsclient.core.event;

/**
 * Execution priority slots for event listeners.
 * Handlers are invoked in order: HIGHEST -> HIGH -> NORMAL -> LOW -> LOWEST -> MONITOR.
 */
public enum EventPriority {
    HIGHEST(0),
    HIGH(1),
    NORMAL(2),
    LOW(3),
    LOWEST(4),
    MONITOR(5);

    private final int slot;

    EventPriority(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}
