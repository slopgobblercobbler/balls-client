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
 * Pulsating glow shader highlight on high-priority mineral clusters with subterranean wall bypass.
 */
public class OreHighlighter extends Module {
    private final NumberSetting pulseRate = new NumberSetting("PulseRate", "Pulsating shimmer frequency", 1.5, 0.5, 4.0, 0.5);
    private final BooleanSetting showCount = new BooleanSetting("ShowCount", "Show vein cluster size number", true);
    private final ColorSetting glowColor = new ColorSetting("GlowColor", "Cluster glow aura color", 0xFF00FFFF);

    public static class OreCluster {
        public final int originX, originY, originZ;
        public final int count;
        public final String oreType;

        public OreCluster(int x, int y, int z, int count, String oreType) {
            this.originX = x;
            this.originY = y;
            this.originZ = z;
            this.count = count;
            this.oreType = oreType != null ? oreType : "Diamond";
        }
    }

    private final List<OreCluster> clusters = new ArrayList<>();

    public OreHighlighter() {
        super("ore-highlighter", "Ore Highlighter", "Luminous pulsating glow highlighting high-priority ore clusters", Category.RENDER);
        addSettings(pulseRate, showCount, glowColor);
    }

    public synchronized void updateClusters(List<OreCluster> list) {
        clusters.clear();
        if (list != null) {
            clusters.addAll(list);
        }
    }

    public synchronized List<OreCluster> getClusters() {
        return Collections.unmodifiableList(new ArrayList<>(clusters));
    }

    public double getPulseRate() { return pulseRate.getValue(); }
    public boolean isShowCount() { return showCount.getValue(); }
    public int getGlowColor() { return glowColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render glowing mineral clusters
    }
}
