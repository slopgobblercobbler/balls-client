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
 * 3D parabolic trajectory prediction arcs for airborne arrows and projectiles.
 */
public class ArrowTracers extends Module {
    private final BooleanSetting predictLanding = new BooleanSetting("PredictLanding", "Calculate and draw impact landing marker", true);
    private final ColorSetting arcColor = new ColorSetting("ArcColor", "Projectile arc line color (Orange)", 0xFFFF8800);
    private final NumberSetting maxDistance = new NumberSetting("MaxDistance", "Maximum trajectory simulation range", 50.0, 10.0, 100.0, 5.0);

    public static class ProjectileArc {
        public final List<double[]> points = new ArrayList<>();
        public final double[] impactPoint;

        public ProjectileArc(List<double[]> points, double[] impactPoint) {
            if (points != null) this.points.addAll(points);
            this.impactPoint = impactPoint;
        }
    }

    private final List<ProjectileArc> activeArcs = new ArrayList<>();

    public ArrowTracers() {
        super("arrow-tracers", "Arrow Tracers", "3D parabolic trajectory prediction arcs for incoming arrows", Category.RENDER);
        addSettings(predictLanding, arcColor, maxDistance);
    }

    public synchronized void updateArcs(List<ProjectileArc> arcs) {
        activeArcs.clear();
        if (arcs != null) {
            activeArcs.addAll(arcs);
        }
    }

    public synchronized List<ProjectileArc> getActiveArcs() {
        return Collections.unmodifiableList(new ArrayList<>(activeArcs));
    }

    public boolean isPredictLanding() { return predictLanding.getValue(); }
    public int getArcColor() { return arcColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D parabolic curves
    }
}
