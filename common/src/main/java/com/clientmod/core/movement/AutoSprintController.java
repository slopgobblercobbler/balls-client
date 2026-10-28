package com.clientmod.core.movement;

/**
 * Pure Java Auto-Sprint state machine.
 */
public class AutoSprintController {
    private boolean enabled = false;
    private boolean shouldSprint = false;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            this.shouldSprint = false;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void onTick(boolean forwardPressed) {
        if (!enabled) {
            shouldSprint = false;
            return;
        }
        shouldSprint = forwardPressed;
    }

    public boolean shouldSprint() {
        return shouldSprint;
    }
}
