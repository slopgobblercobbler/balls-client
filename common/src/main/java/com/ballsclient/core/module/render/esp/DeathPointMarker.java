package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Automatically places a 3D beacon marker and distance label at the player's last death location.
 */
public class DeathPointMarker extends Module {
    private final BooleanSetting autoClearOnPickup = new BooleanSetting("AutoClear", "Clear marker when player retrieves their items (within 3 blocks)", true);
    private final ColorSetting markerColor = new ColorSetting("MarkerColor", "Marker beacon color (Crimson)", 0xFFFF0055);

    private double deathX = 0, deathY = 0, deathZ = 0;
    private int lostLevels = 0;
    private boolean hasDeathPoint = false;
    private long deathTimestamp = 0;

    public DeathPointMarker() {
        super("death-point-marker", "Death Point Marker", "Places a 3D beacon and distance tag at your previous death location", Category.RENDER);
        addSettings(autoClearOnPickup, markerColor);
    }

    public void setDeathLocation(double x, double y, double z, int levels, long nowMillis) {
        this.deathX = x;
        this.deathY = y;
        this.deathZ = z;
        this.lostLevels = levels;
        this.deathTimestamp = nowMillis;
        this.hasDeathPoint = true;
    }

    public void checkPickup(double playerX, double playerY, double playerZ) {
        if (!hasDeathPoint || !autoClearOnPickup.getValue()) return;
        double dx = playerX - deathX;
        double dy = playerY - deathY;
        double dz = playerZ - deathZ;
        if (dx * dx + dy * dy + dz * dz <= 9.0) { // 3 blocks
            hasDeathPoint = false;
        }
    }

    public void clear() {
        hasDeathPoint = false;
    }

    public boolean hasDeathPoint() { return hasDeathPoint; }
    public double getDeathX() { return deathX; }
    public double getDeathY() { return deathY; }
    public double getDeathZ() { return deathZ; }
    public int getLostLevels() { return lostLevels; }
    public long getDeathTimestamp() { return deathTimestamp; }
    public int getMarkerColor() { return markerColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render death marker beacon
    }
}
