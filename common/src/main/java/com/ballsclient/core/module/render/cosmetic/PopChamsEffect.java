package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Translucent lingering player model clone positioned at the exact coordinates of a popped totem.
 */
public class PopChamsEffect extends Module {
    private final NumberSetting durationMs = new NumberSetting("DurationMs", "Persistence of ghost model in milliseconds", 800.0, 200.0, 2000.0, 100.0);
    private final ColorSetting fadeColor = new ColorSetting("FadeColor", "Initial totem pop silhouette color", 0x88FFCC00);

    public static class PopChamInstance {
        public final double x, y, z;
        public final float yaw, pitch;
        public final long timestamp;

        public PopChamInstance(double x, double y, double z, float yaw, float pitch, long timestamp) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.timestamp = timestamp;
        }

        public float getAlpha(long now, double maxDuration) {
            long elapsed = now - timestamp;
            if (elapsed >= maxDuration || elapsed < 0) return 0.0f;
            return (float) (1.0 - (elapsed / maxDuration));
        }
    }

    private final List<PopChamInstance> instances = new ArrayList<>();

    public PopChamsEffect() {
        super("pop-chams-effect", "Pop Chams Effect", "Lingering translucent player silhouette at totem pop coordinates", Category.RENDER);
        addSettings(durationMs, fadeColor);
    }

    public synchronized void spawnPopCham(double x, double y, double z, float yaw, float pitch, long now) {
        instances.add(new PopChamInstance(x, y, z, yaw, pitch, now));
    }

    public synchronized void pruneInstances(long now) {
        double max = durationMs.getValue();
        instances.removeIf(i -> i.getAlpha(now, max) <= 0.0f);
    }

    public synchronized List<PopChamInstance> getInstances() {
        return Collections.unmodifiableList(new ArrayList<>(instances));
    }

    public double getDurationMs() { return durationMs.getValue(); }
    public int getFadeColor() { return fadeColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D pop chams
    }
}
