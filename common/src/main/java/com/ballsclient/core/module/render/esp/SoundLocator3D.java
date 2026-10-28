package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 3D directional expanding sound rings indicating footsteps, block breaks, and chest openings.
 */
public class SoundLocator3D extends Module {
    private final BooleanSetting footsteps = new BooleanSetting("Footsteps", "Render rings for entity walking/running", true);
    private final BooleanSetting chests = new BooleanSetting("Chests", "Render rings for chest opening/closing", true);
    private final ColorSetting soundColor = new ColorSetting("SoundColor", "Wave ring color", 0xFFFFFF00);

    public static class SoundRing {
        public final double x, y, z;
        public final String soundType;
        public final long timestamp;

        public SoundRing(double x, double y, double z, String soundType, long timestamp) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.soundType = soundType != null ? soundType : "General";
            this.timestamp = timestamp;
        }

        public float getRadius(long now) {
            float elapsed = (now - timestamp) / 1000.0f;
            return Math.min(3.0f, elapsed * 3.0f);
        }

        public float getAlpha(long now) {
            float elapsed = (now - timestamp) / 1000.0f;
            if (elapsed >= 1.0f) return 0.0f;
            return 1.0f - elapsed;
        }
    }

    private final List<SoundRing> soundRings = new ArrayList<>();

    public SoundLocator3D() {
        super("sound-locator-3d", "Sound Locator 3D", "3D directional sound wave rings pinpointing footstep and chest audio sources", Category.RENDER);
        addSettings(footsteps, chests, soundColor);
    }

    public synchronized void recordSound(double x, double y, double z, String type, long now) {
        if ("FOOTSTEP".equalsIgnoreCase(type) && !footsteps.getValue()) return;
        if ("CHEST".equalsIgnoreCase(type) && !chests.getValue()) return;
        soundRings.add(new SoundRing(x, y, z, type, now));
    }

    public synchronized void pruneSounds(long now) {
        soundRings.removeIf(r -> r.getAlpha(now) <= 0.0f);
    }

    public synchronized List<SoundRing> getSoundRings() {
        return Collections.unmodifiableList(new ArrayList<>(soundRings));
    }

    public int getSoundColor() { return soundColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D sound waves
    }
}
