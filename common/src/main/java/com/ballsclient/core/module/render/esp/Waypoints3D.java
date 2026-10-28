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
 * 3D world beacon beams and floating distance nametags for saved locations.
 */
public class Waypoints3D extends Module {
    private final BooleanSetting showBeams = new BooleanSetting("ShowBeams", "Render vertical beacon pillars reaching sky limit", true);
    private final BooleanSetting showDistance = new BooleanSetting("ShowDistance", "Display metric distance to waypoint", true);
    private final NumberSetting beamWidth = new NumberSetting("BeamWidth", "Pixel diameter of beacon pillar", 1.0, 0.1, 5.0, 0.1);

    public static class Waypoint {
        public final String name;
        public final double x, y, z;
        public final int colorRgb;

        public Waypoint(String name, double x, double y, double z, int colorRgb) {
            this.name = name != null ? name : "Waypoint";
            this.x = x;
            this.y = y;
            this.z = z;
            this.colorRgb = colorRgb;
        }

        public double distanceTo(double px, double py, double pz) {
            double dx = px - x;
            double dy = py - y;
            double dz = pz - z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    private final List<Waypoint> waypoints = new ArrayList<>();

    public Waypoints3D() {
        super("waypoints-3d", "Waypoints 3D", "Pillars of light and distance markers for saved locations", Category.RENDER);
        addSettings(showBeams, showDistance, beamWidth);
    }

    public synchronized void addWaypoint(Waypoint wp) {
        if (wp != null) waypoints.add(wp);
    }

    public synchronized void removeWaypoint(String name) {
        waypoints.removeIf(w -> w.name.equalsIgnoreCase(name));
    }

    public synchronized List<Waypoint> getWaypoints() {
        return Collections.unmodifiableList(new ArrayList<>(waypoints));
    }

    public boolean isShowBeams() { return showBeams.getValue(); }
    public boolean isShowDistance() { return showDistance.getValue(); }
    public double getBeamWidth() { return beamWidth.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D beacon beams
    }
}
