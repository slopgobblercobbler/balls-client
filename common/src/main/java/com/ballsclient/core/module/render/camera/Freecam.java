package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Detaches the client camera into noclip flight mode while player body remains stationary in world.
 */
public class Freecam extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", "Flight speed in blocks/sec", 1.0, 0.1, 5.0, 0.1);
    private final BooleanSetting showRealPlayer = new BooleanSetting("ShowRealPlayer", "Render client player body at original position", true);
    private final BooleanSetting disablePackets = new BooleanSetting("DisablePackets", "Cancel outgoing movement packets during freecam", true);

    private double cameraX, cameraY, cameraZ;
    private float cameraYaw, cameraPitch;

    public Freecam() {
        super("freecam", "Freecam", "Spectator-mode noclip camera detachment from player body", Category.RENDER);
        addSettings(speed, showRealPlayer, disablePackets);
    }

    public void setCameraPosition(double x, double y, double z, float yaw, float pitch) {
        this.cameraX = x;
        this.cameraY = y;
        this.cameraZ = z;
        this.cameraYaw = yaw;
        this.cameraPitch = pitch;
    }

    public double getSpeed() { return speed.getValue(); }
    public boolean isShowRealPlayer() { return showRealPlayer.getValue(); }
    public boolean isDisablePackets() { return disablePackets.getValue(); }
    public double getCameraX() { return cameraX; }
    public double getCameraY() { return cameraY; }
    public double getCameraZ() { return cameraZ; }
    public float getCameraYaw() { return cameraYaw; }
    public float getCameraPitch() { return cameraPitch; }
}
