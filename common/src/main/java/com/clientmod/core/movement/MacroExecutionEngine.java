package com.clientmod.core.movement;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Tick-based macro action execution queue.
 */
public class MacroExecutionEngine {
    public interface MacroAction {
        void execute();
    }

    private final Queue<MacroAction> actionQueue = new ArrayDeque<>();
    private int tickDelay = 0;

    public synchronized void queueAction(MacroAction action) {
        if (action != null) {
            actionQueue.offer(action);
        }
    }

    public synchronized void setTickDelay(int ticks) {
        this.tickDelay = Math.max(0, ticks);
    }

    public synchronized void onTick() {
        if (tickDelay > 0) {
            tickDelay--;
            return;
        }
        MacroAction action = actionQueue.poll();
        if (action != null) {
            action.execute();
        }
    }

    public synchronized int getQueueSize() {
        return actionQueue.size();
    }

    public synchronized void clear() {
        actionQueue.clear();
        tickDelay = 0;
    }
}
