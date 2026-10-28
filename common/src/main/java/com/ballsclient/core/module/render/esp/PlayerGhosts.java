package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.*;

/**
 * Translucent motion ghost after-images showing historical player positions over the past 1.5 seconds.
 */
public class PlayerGhosts extends Module {
    private final NumberSetting historySamples = new NumberSetting("Samples", "Number of trailing ghost frames", 15.0, 5.0, 30.0, 1.0);
    private final NumberSetting ghostAlpha = new NumberSetting("GhostAlpha", "Transparency alpha for ghost silhouettes", 0.35, 0.1, 0.9, 0.05);
    private final ColorSetting ghostColor = new ColorSetting("GhostColor", "Color tint of ghost models", 0x80FFFFFF);

    public static class GhostFrame {
        public final double x, y, z;
        public final float yaw, pitch;
        public final long timestamp;

        public GhostFrame(double x, double y, double z, float yaw, float pitch, long timestamp) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.timestamp = timestamp;
        }
    }

    private final Map<String, Deque<GhostFrame>> playerHistories = new HashMap<>();

    public PlayerGhosts() {
        super("player-ghosts", "Player Ghosts", "Translucent after-image trails visualizing recent player movement vectors", Category.RENDER);
        addSettings(historySamples, ghostAlpha, ghostColor);
    }

    public synchronized void recordPlayerPosition(String playerId, double x, double y, double z, float yaw, float pitch, long now) {
        Deque<GhostFrame> queue = playerHistories.computeIfAbsent(playerId, k -> new ArrayDeque<>());
        queue.addLast(new GhostFrame(x, y, z, yaw, pitch, now));
        int max = historySamples.getIntValue();
        while (queue.size() > max) {
            queue.pollFirst();
        }
    }

    public synchronized List<GhostFrame> getGhostFrames(String playerId) {
        Deque<GhostFrame> queue = playerHistories.get(playerId);
        return queue != null ? new ArrayList<>(queue) : Collections.emptyList();
    }

    public double getGhostAlpha() { return ghostAlpha.getValue(); }
    public int getGhostColor() { return ghostColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D player after-images
    }
}
