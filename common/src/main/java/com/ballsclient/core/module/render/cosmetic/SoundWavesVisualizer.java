package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Circular expanding audio rings positioned on screen edges displaying directional game sounds.
 */
public class SoundWavesVisualizer extends Module {
    private final NumberSetting maxRings = new NumberSetting("MaxRings", "Maximum active audio wave rings on screen", 8.0, 1.0, 20.0, 1.0);
    private final ColorSetting ringColor = new ColorSetting("RingColor", "Screen edge audio wave color", 0x8800FFFF);

    public static class ScreenSoundWave {
        public final float screenX, screenY;
        public final long timestamp;

        public ScreenSoundWave(float x, float y, long timestamp) {
            this.screenX = x;
            this.screenY = y;
            this.timestamp = timestamp;
        }

        public float getRadius(long now) {
            return (now - timestamp) * 0.05f;
        }

        public float getAlpha(long now) {
            float elapsed = (now - timestamp) / 1000.0f;
            if (elapsed >= 1.0f) return 0.0f;
            return 1.0f - elapsed;
        }
    }

    private final List<ScreenSoundWave> waves = new ArrayList<>();

    public SoundWavesVisualizer() {
        super("sound-waves-visualizer", "Sound Waves Visualizer", "Screen-edge audio wave rings indicating directional sound cues", Category.RENDER);
        addSettings(maxRings, ringColor);
    }

    public synchronized void addSoundWave(float screenX, float screenY, long now) {
        waves.add(new ScreenSoundWave(screenX, screenY, now));
        while (waves.size() > maxRings.getIntValue()) {
            waves.remove(0);
        }
    }

    public synchronized void pruneWaves(long now) {
        waves.removeIf(w -> w.getAlpha(now) <= 0.0f);
    }

    public synchronized List<ScreenSoundWave> getWaves() {
        return Collections.unmodifiableList(new ArrayList<>(waves));
    }

    public int getRingColor() { return ringColor.getValue(); }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render 2D screen edge sound waves
    }
}
