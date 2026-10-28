package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 3D particle bursts and floating pop counters when players activate an Undying Totem.
 */
public class TotemPopVisualizer extends Module {
    private final BooleanSetting playPopSound = new BooleanSetting("PopSound", "Play tactile audio confirmation cue on enemy totem pop", true);
    private final NumberSetting particleCount = new NumberSetting("Particles", "Quantity of emerald sparkles spawned", 50.0, 10.0, 200.0, 10.0);
    private final NumberSetting scale = new NumberSetting("Scale", "3D totem icon scale", 1.5, 0.5, 3.0, 0.1);

    public static class TotemPopEvent {
        public final String playerName;
        public final int totalPops;
        public final double x, y, z;
        public final long timestamp;

        public TotemPopEvent(String playerName, int totalPops, double x, double y, double z, long timestamp) {
            this.playerName = playerName != null ? playerName : "Player";
            this.totalPops = totalPops;
            this.x = x;
            this.y = y;
            this.z = z;
            this.timestamp = timestamp;
        }

        public float getAlpha(long now) {
            long age = now - timestamp;
            if (age >= 1500L) return 0.0f;
            return 1.0f - (age / 1500.0f);
        }
    }

    private final List<TotemPopEvent> activePops = new ArrayList<>();

    public TotemPopVisualizer() {
        super("totem-pop-visualizer", "Totem Pop Visualizer", "Renders 3D particle bursts and pop count trackers when totems trigger", Category.RENDER);
        addSettings(playPopSound, particleCount, scale);
    }

    public synchronized void registerPop(String playerName, int totalPops, double x, double y, double z, long now) {
        activePops.add(new TotemPopEvent(playerName, totalPops, x, y, z, now));
    }

    public synchronized void prunePops(long now) {
        activePops.removeIf(p -> p.getAlpha(now) <= 0.0f);
    }

    public synchronized List<TotemPopEvent> getActivePops() {
        return Collections.unmodifiableList(new ArrayList<>(activePops));
    }

    public boolean isPlayPopSound() { return playPopSound.getValue(); }
    public int getParticleCount() { return particleCount.getIntValue(); }
    public double getScale() { return scale.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D totem pop effects
    }
}
