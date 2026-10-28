package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 2D Circular radar displaying nearby entities categorized by relationship and distance.
 */
public class SpatialRadar2D extends Module {
    private final NumberSetting radius = new NumberSetting("Radius", "Pixel radius of radar circle", 60.0, 30.0, 150.0, 5.0);
    private final NumberSetting maxDistance = new NumberSetting("MaxDistance", "Maximum detection range in blocks", 64.0, 16.0, 256.0, 8.0);
    private final BooleanSetting showPassive = new BooleanSetting("ShowPassive", "Show passive animals and non-hostile mobs", true);

    public enum EntityType {
        PLAYER,
        HOSTILE,
        PASSIVE,
        ITEM
    }

    public static class RadarBlip {
        public final double relX;
        public final double relZ;
        public final EntityType type;
        public final String label;

        public RadarBlip(double relX, double relZ, EntityType type, String label) {
            this.relX = relX;
            this.relZ = relZ;
            this.type = type;
            this.label = label;
        }

        public double distance() {
            return Math.sqrt(relX * relX + relZ * relZ);
        }
    }

    private final List<RadarBlip> blips = new ArrayList<>();

    public SpatialRadar2D() {
        super("spatial-radar-2d", "Spatial Radar 2D", "Circular mini-radar displaying nearby entities", Category.RENDER);
        addSettings(radius, maxDistance, showPassive);
    }

    public synchronized void updateBlips(List<RadarBlip> newBlips) {
        blips.clear();
        if (newBlips != null) {
            blips.addAll(newBlips);
        }
    }

    public synchronized List<RadarBlip> getVisibleBlips() {
        double maxDist = maxDistance.getValue();
        boolean passives = showPassive.getValue();
        List<RadarBlip> visible = new ArrayList<>();
        for (RadarBlip b : blips) {
            if (b.distance() <= maxDist && (passives || b.type != EntityType.PASSIVE)) {
                visible.add(b);
            }
        }
        return Collections.unmodifiableList(visible);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Radar render
    }
}
