package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * ChatCommandQueue — Queues multi-step server commands with anti-spam throttle pacing.
 */
public class ChatCommandQueue extends Module {
    private final NumberSetting commandDelayMs = new NumberSetting("CommandDelayMs", "Delay between successive chat dispatches in milliseconds", 500.0, 100.0, 2000.0, 50.0);
    private final BooleanSetting suppressInGameOutput = new BooleanSetting("SuppressInGameOutput", "Hides automated bot commands from chat HUD", false);

    private final Queue<String> commandQueue = new ArrayDeque<>();
    private long lastDispatchTime = 0L;

    public ChatCommandQueue() {
        super("chat-command-queue", "Chat Command Queue", "Queues multi-step server commands with anti-spam throttle pacing (e.g. /home, /sell)", Category.AUTOMATION);
        addSettings(commandDelayMs, suppressInGameOutput);
    }

    public NumberSetting getCommandDelayMs() {
        return commandDelayMs;
    }

    public BooleanSetting getSuppressInGameOutput() {
        return suppressInGameOutput;
    }

    public synchronized void enqueueCommand(String command) {
        if (command != null && !command.isBlank()) {
            commandQueue.offer(command);
        }
    }

    public synchronized String pollCommand(long currentTime) {
        if (commandQueue.isEmpty()) {
            return null;
        }
        if (currentTime - lastDispatchTime < commandDelayMs.getValue()) {
            return null;
        }
        lastDispatchTime = currentTime;
        return commandQueue.poll();
    }

    public synchronized int getPendingCount() {
        return commandQueue.size();
    }
}
