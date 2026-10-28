package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Accelerates rideable entities (horses, pigs, striders, camels).
 */
public class EntitySpeed extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", "Maximum ridden entity cruising speed", 1.8, 0.5, 6.0, 0.1);
    private final NumberSetting jumpPower = new NumberSetting("JumpPower", "Ridden entity jump velocity impulse", 1.2, 0.5, 3.0, 0.1);
    private final BooleanSetting flyEntities = new BooleanSetting("FlyEntities", "Enable 3D flight for ridden terrestrial mounts", false);

    public EntitySpeed() {
        super("entity-speed", "Entity Speed", "Accelerates rideable entities (horses, pigs, striders, camels)", Category.MOVEMENT);
        addSettings(speed, jumpPower, flyEntities);
    }

    public double getSpeed() { return speed.getValue(); }
    public double getJumpPower() { return jumpPower.getValue(); }
    public boolean isFlyEntities() { return flyEntities.getValue(); }

    public double[] calculateRiddenMotion(float yaw, double forward, double strafe) {
        if (!isEnabled()) return new double[]{0.0, 0.0};
        double rad = Math.toRadians(yaw);
        double vx = (-Math.sin(rad) * forward + Math.cos(rad) * strafe) * getSpeed();
        double vz = (Math.cos(rad) * forward + Math.sin(rad) * strafe) * getSpeed();
        return new double[]{vx, vz};
    }
}
