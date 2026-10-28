package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * LightningStrikeRadar — Thunder sound triangulation and strike coordinate predictor for charged creepers.
 */
public class LightningStrikeRadar extends Module {
    private final NumberSetting trackingRadius = new NumberSetting("TrackingRadius", "Detection radius for strikes", 500.0, 100.0, 2000.0, 50.0);
    private final BooleanSetting chatAlert = new BooleanSetting("ChatAlert", "Broadcast coordinates in chat", true);
    private final BooleanSetting pingChargedCreeper = new BooleanSetting("PingChargedCreeper", "Ping creepers within strike shockwave", true);

    public LightningStrikeRadar() {
        super("lightning-strike-radar", "Lightning Strike Radar", "Thunder sound triangulation and strike coordinate predictor for charged creepers", Category.PLAYER);
        addSettings(trackingRadius, chatAlert, pingChargedCreeper);
    }

    public NumberSetting getTrackingRadius() {
        return trackingRadius;
    }

    public BooleanSetting getChatAlert() {
        return chatAlert;
    }

    public BooleanSetting getPingChargedCreeper() {
        return pingChargedCreeper;
    }

    public static double getDistanceToStrike(double px, double pz, double strikeX, double strikeZ) {
        double dx = strikeX - px;
        double dz = strikeZ - pz;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static boolean isCreeperElectrified(double creeperX, double creeperZ, double strikeX, double strikeZ) {
        // Lightning charges creepers within 3.0 blocks horizontally
        double dx = creeperX - strikeX;
        double dz = creeperZ - strikeZ;
        return Math.sqrt(dx * dx + dz * dz) <= 3.0;
    }
}
