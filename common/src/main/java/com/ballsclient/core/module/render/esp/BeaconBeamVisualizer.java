package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 3D infinite vertical beacon ray projector with custom tinting and effect buff nametags.
 */
public class BeaconBeamVisualizer extends Module {
    private final NumberSetting beamAlpha = new NumberSetting("BeamAlpha", "Beacon light column opacity", 0.7, 0.1, 1.0, 0.05);
    private final BooleanSetting showEffects = new BooleanSetting("ShowEffects", "Display primary/secondary buff tags on beam base", true);
    private final ColorSetting customColor = new ColorSetting("CustomColor", "Override color for beacon ray (Cyan)", 0xFF00FFFF);

    public static class BeaconEntity {
        public final int x, y, z;
        public final int tier;
        public final String primaryEffect;

        public BeaconEntity(int x, int y, int z, int tier, String primaryEffect) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.tier = tier;
            this.primaryEffect = primaryEffect != null ? primaryEffect : "Speed";
        }
    }

    private final List<BeaconEntity> activeBeacons = new ArrayList<>();

    public BeaconBeamVisualizer() {
        super("beacon-beam-visualizer", "Beacon Beam Visualizer", "Renders vertical beacon pillars with custom tints and effect tags", Category.RENDER);
        addSettings(beamAlpha, showEffects, customColor);
    }

    public synchronized void updateBeacons(List<BeaconEntity> list) {
        activeBeacons.clear();
        if (list != null) {
            activeBeacons.addAll(list);
        }
    }

    public synchronized List<BeaconEntity> getActiveBeacons() {
        return Collections.unmodifiableList(new ArrayList<>(activeBeacons));
    }

    public double getBeamAlpha() { return beamAlpha.getValue(); }
    public boolean isShowEffects() { return showEffects.getValue(); }
    public int getCustomColor() { return customColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render beacon rays
    }
}
