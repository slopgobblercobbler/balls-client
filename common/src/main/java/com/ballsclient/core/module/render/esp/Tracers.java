package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 3D vector tracer lines drawn from player crosshair or feet to entities.
 */
public class Tracers extends Module {
    private final ModeSetting target = new ModeSetting("Target", "Target entity filter", "PLAYERS", "PLAYERS", "HOSTILES", "ALL");
    private final ModeSetting origin = new ModeSetting("Origin", "Screen origin point for line start", "HEAD", "HEAD", "FEET");
    private final ColorSetting color = new ColorSetting("Color", "Tracer line color", 0xFFFF0000);

    public static class TracerTarget {
        public final double x, y, z;
        public final String type;

        public TracerTarget(double x, double y, double z, String type) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.type = type != null ? type : "PLAYER";
        }
    }

    private final List<TracerTarget> targets = new ArrayList<>();

    public Tracers() {
        super("tracers", "Tracers", "3D vector lines connecting player crosshair to target entities", Category.RENDER);
        addSettings(target, origin, color);
    }

    public synchronized void updateTargets(List<TracerTarget> list) {
        targets.clear();
        if (list != null) {
            targets.addAll(list);
        }
    }

    public synchronized List<TracerTarget> getTargets() {
        return Collections.unmodifiableList(new ArrayList<>(targets));
    }

    public String getTargetFilter() { return target.getValue(); }
    public String getOriginPoint() { return origin.getValue(); }
    public int getColor() { return color.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Draw 3D tracer lines
    }
}
