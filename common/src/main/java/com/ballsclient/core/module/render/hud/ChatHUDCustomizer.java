package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Animated chat HUD customizer with adjustable background opacity and timestamps.
 */
public class ChatHUDCustomizer extends Module {
    private final NumberSetting backgroundOpacity = new NumberSetting("BgOpacity", "Background opacity for chat box", 0.5, 0.0, 1.0, 0.05);
    private final BooleanSetting timestamps = new BooleanSetting("Timestamps", "Prefix chat messages with [HH:mm] timestamp", true);
    private final NumberSetting maxLines = new NumberSetting("MaxLines", "Chat scrollback buffer limit", 100.0, 20.0, 500.0, 10.0);

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private final Deque<String> messageHistory = new ArrayDeque<>();

    public ChatHUDCustomizer() {
        super("chat-hud-customizer", "Chat HUD Customizer", "Chat visual tweaks, background opacity, and timestamps", Category.RENDER);
        addSettings(backgroundOpacity, timestamps, maxLines);
    }

    public synchronized void addMessage(String message) {
        if (message == null) return;
        String formatted = message;
        if (timestamps.getValue()) {
            String timePrefix = "[" + LocalTime.now().format(TIME_FORMAT) + "] ";
            formatted = timePrefix + message;
        }
        messageHistory.addLast(formatted);
        while (messageHistory.size() > maxLines.getIntValue()) {
            messageHistory.pollFirst();
        }
    }

    public synchronized List<String> getRecentMessages() {
        return Collections.unmodifiableList(new ArrayList<>(messageHistory));
    }

    public double getBackgroundOpacity() {
        return backgroundOpacity.getValue();
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render custom chat
    }
}
