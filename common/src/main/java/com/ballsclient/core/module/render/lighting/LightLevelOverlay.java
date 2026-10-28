package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Renders floor numbers or crosses indicating hostile mob spawn suitability.
 */
public class LightLevelOverlay extends Module {
    private final ModeSetting display = new ModeSetting("Display", "Marker visual mode", "NUMBER", "NUMBER", "CROSS");
    private final BooleanSetting aggressiveMobsOnly = new BooleanSetting("HostilesOnly", "Only mark blocks where light is <= 0 (hostile spawnable)", true);

    public static class LightMarker {
        public final int x, y, z;
        public final int blockLight;

        public LightMarker(int x, int y, int z, int blockLight) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.blockLight = blockLight;
        }

        public boolean isDangerous() {
            return blockLight <= 0;
        }

        public int getColor() {
            if (blockLight <= 0) return 0xFFFF2222; // Red
            if (blockLight < 7) return 0xFFFFFF00;  // Yellow
            return 0xFF00FF00;                     // Green
        }
    }

    private final List<LightMarker> markers = new ArrayList<>();

    public LightLevelOverlay() {
        super("light-level-overlay", "Light Level Overlay", "Displays mob-spawn light level numbers on floor blocks", Category.RENDER);
        addSettings(display, aggressiveMobsOnly);
    }

    public synchronized void updateMarkers(List<LightMarker> list) {
        markers.clear();
        if (list != null) {
            for (LightMarker m : list) {
                if (aggressiveMobsOnly.getValue() && !m.isDangerous()) continue;
                markers.add(m);
            }
        }
    }

    public synchronized List<LightMarker> getMarkers() {
        return Collections.unmodifiableList(new ArrayList<>(markers));
    }

    public String getDisplayMode() { return display.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D light numbers on blocks
    }
}
