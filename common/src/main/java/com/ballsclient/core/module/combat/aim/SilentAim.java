package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Silent Aim — Redirects outbound attack packets toward target without altering client camera pitch/yaw visually
 */
public class SilentAim extends Module {
    private final NumberSetting maxAngleOffset = new NumberSetting("MaxAngleOffset", "Maximum angle offset to spoof attack packets", 35.0, 5.0, 90.0, 1.0);
    private final BooleanSetting packetSmoothing = new BooleanSetting("PacketSmoothing", "Smooth packet rotation deltas", true);

    public SilentAim() {
        super("silent-aim", "Silent Aim", "Redirects outbound attack packets toward target without altering client camera pitch/yaw visually", Category.COMBAT);
        addSettings(maxAngleOffset, packetSmoothing);
    }

    public double getMaxAngleOffset() {
        return maxAngleOffset.getValue();
    }

    public boolean isPacketSmoothing() {
        return packetSmoothing.getValue();
    }

    public boolean shouldSpoofAngles(double angleDiff, double maxAngle) {
        return angleDiff <= maxAngle && angleDiff >= 0.0;
    }
}
