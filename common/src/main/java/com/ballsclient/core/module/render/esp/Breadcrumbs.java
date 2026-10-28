package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * 3D trailing particle ribbon showing player's walking trajectory with smooth fading.
 */
public class Breadcrumbs extends Module {
    private final NumberSetting fadeSeconds = new NumberSetting("FadeSeconds", "Duration in seconds before trail nodes fade", 10.0, 1.0, 60.0, 1.0);
    private final NumberSetting ribbonWidth = new NumberSetting("RibbonWidth", "Width of trajectory ribbon in blocks", 0.2, 0.05, 1.0, 0.05);
    private final ColorSetting trailColor = new ColorSetting("TrailColor", "Color of trajectory path", 0xFF00AAFF);

    public static class PathNode {
        public final double x, y, z;
        public final long timestamp;

        public PathNode(double x, double y, double z, long timestamp) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.timestamp = timestamp;
        }

        public float getAlpha(long now, double maxAgeSeconds) {
            long ageMillis = now - timestamp;
            double maxAgeMillis = maxAgeSeconds * 1000.0;
            if (ageMillis >= maxAgeMillis || ageMillis < 0) return 0.0f;
            return (float) (1.0 - (ageMillis / maxAgeMillis));
        }
    }

    private final Deque<PathNode> nodes = new ArrayDeque<>();

    public Breadcrumbs() {
        super("breadcrumbs", "Breadcrumbs", "3D path ribbon showing historical movement trajectory", Category.RENDER);
        addSettings(fadeSeconds, ribbonWidth, trailColor);
    }

    public synchronized void recordPosition(double x, double y, double z, long nowMillis) {
        if (!nodes.isEmpty()) {
            PathNode last = nodes.peekLast();
            double dx = x - last.x;
            double dy = y - last.y;
            double dz = z - last.z;
            if (dx * dx + dy * dy + dz * dz < 0.01) {
                return; // Skip negligible movement
            }
        }
        nodes.addLast(new PathNode(x, y, z, nowMillis));
        pruneNodes(nowMillis);
    }

    public synchronized void pruneNodes(long nowMillis) {
        double maxAgeMillis = fadeSeconds.getValue() * 1000.0;
        while (!nodes.isEmpty() && (nowMillis - nodes.peekFirst().timestamp) > maxAgeMillis) {
            nodes.pollFirst();
        }
    }

    public synchronized List<PathNode> getNodes() {
        return Collections.unmodifiableList(new ArrayList<>(nodes));
    }

    public double getRibbonWidth() { return ribbonWidth.getValue(); }
    public int getTrailColor() { return trailColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render ribbon strip
    }
}
