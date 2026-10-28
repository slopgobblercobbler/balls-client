package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;
import java.util.ArrayList;
import java.util.List;

/**
 * Projectile Prediction — Ballistic trajectory solver calculating gravitational drop, air resistance, and target intercept point
 */
public class ProjectilePrediction extends Module {
    private final ModeSetting projectileType = new ModeSetting("ProjectileType", "Type of projectile to simulate", "Arrow", "Arrow", "Snowball", "EnderPearl", "SplashPotion");
    private final NumberSetting maxSimulationTicks = new NumberSetting("MaxSimulationTicks", "Maximum simulation ticks ahead", 60.0, 10.0, 120.0, 1.0);
    private final BooleanSetting renderPath = new BooleanSetting("RenderPath", "Show 3D trajectory line", true);
    private final BooleanSetting impactMarker = new BooleanSetting("ImpactMarker", "Render landing impact crosshair", true);

    public ProjectilePrediction() {
        super("projectile-prediction", "Projectile Prediction", "Ballistic trajectory solver calculating gravitational drop, air resistance, and target intercept point", Category.COMBAT);
        addSettings(projectileType, maxSimulationTicks, renderPath, impactMarker);
    }

    public String getProjectileType() {
        return projectileType.getValue();
    }

    public double getMaxSimulationTicks() {
        return maxSimulationTicks.getValue();
    }

    public boolean isRenderPath() {
        return renderPath.getValue();
    }

    public boolean isImpactMarker() {
        return impactMarker.getValue();
    }

    public static class TrajectoryPoint {
        public final double x, y, z, vx, vy, vz;
        public TrajectoryPoint(double x, double y, double z, double vx, double vy, double vz) {
            this.x = x; this.y = y; this.z = z; this.vx = vx; this.vy = vy; this.vz = vz;
        }
    }

    public List<TrajectoryPoint> simulateTrajectory(double x0, double y0, double z0, double pitchDeg, double yawDeg, double v0, double gravity, double drag, int ticks) {
        List<TrajectoryPoint> path = new ArrayList<>(ticks);
        double pitchRad = Math.toRadians(pitchDeg);
        double yawRad = Math.toRadians(yawDeg);
        double vx = -Math.sin(yawRad) * Math.cos(pitchRad) * v0;
        double vy = -Math.sin(pitchRad) * v0;
        double vz = Math.cos(yawRad) * Math.cos(pitchRad) * v0;
        double x = x0, y = y0, z = z0;
        for (int t = 0; t < ticks; t++) {
            path.add(new TrajectoryPoint(x, y, z, vx, vy, vz));
            x += vx; y += vy; z += vz;
            vx *= drag;
            vy = (vy - gravity) * drag;
            vz *= drag;
        }
        return path;
    }
}
