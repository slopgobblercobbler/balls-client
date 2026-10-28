package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Expanding 3D spherical sonar pulses radiating outward to illuminate hollow caverns and entities.
 */
public class SonarEchoLocator extends Module {
    private final NumberSetting pulseIntervalSeconds = new NumberSetting("Interval", "Seconds between automatic sonar pings", 3.0, 1.0, 10.0, 0.5);
    private final NumberSetting pulseSpeed = new NumberSetting("Speed", "Expansion speed in blocks per second", 20.0, 5.0, 50.0, 5.0);
    private final ColorSetting pulseColor = new ColorSetting("PulseColor", "Sonar wave ring color (Cyan)", 0x9000E5FF);

    public static class SonarPulse {
        public final double originX, originY, originZ;
        public final long startTimeMillis;

        public SonarPulse(double originX, double originY, double originZ, long startTimeMillis) {
            this.originX = originX;
            this.originY = originY;
            this.originZ = originZ;
            this.startTimeMillis = startTimeMillis;
        }

        public double getRadius(long nowMillis, double speedBps) {
            double elapsedSeconds = (nowMillis - startTimeMillis) / 1000.0;
            return elapsedSeconds * speedBps;
        }
    }

    private final List<SonarPulse> activePulses = new ArrayList<>();
    private long lastPingTime = 0;

    public SonarEchoLocator() {
        super("sonar-echo-locator", "Sonar Echo Locator", "Expanding 3D spherical sonar pulses illuminating caves and mobs", Category.RENDER);
        addSettings(pulseIntervalSeconds, pulseSpeed, pulseColor);
    }

    public synchronized void triggerPulse(double x, double y, double z, long nowMillis) {
        activePulses.add(new SonarPulse(x, y, z, nowMillis));
        lastPingTime = nowMillis;
    }

    public synchronized void updatePulses(long nowMillis) {
        double maxRadius = 100.0;
        double speed = pulseSpeed.getValue();
        activePulses.removeIf(p -> p.getRadius(nowMillis, speed) > maxRadius);
    }

    public synchronized List<SonarPulse> getActivePulses() {
        return Collections.unmodifiableList(new ArrayList<>(activePulses));
    }

    public double getPulseSpeed() { return pulseSpeed.getValue(); }
    public int getPulseColor() { return pulseColor.getValue(); }
    public long getLastPingTime() { return lastPingTime; }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D expanding sonar spheres
    }
}
