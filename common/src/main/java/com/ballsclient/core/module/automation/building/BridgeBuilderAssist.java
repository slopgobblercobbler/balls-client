package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * BridgeBuilderAssist — Detects block edge under player and triggers sneak-placement for fast godbridging.
 */
public class BridgeBuilderAssist extends Module {
    private final ModeSetting bridgeMode = new ModeSetting("BridgeMode", "Placement bridging technique", "FastBridge", "FastBridge", "GodBridge", "SafeWalkAssist");
    private final BooleanSetting autoPitch = new BooleanSetting("AutoPitch", "Adjusts camera pitch to target block face edge", true);

    public BridgeBuilderAssist() {
        super("bridge-builder-assist", "Bridge Builder Assist", "Detects block edge under player and triggers sneak-placement for fast godbridging", Category.AUTOMATION);
        addSettings(bridgeMode, autoPitch);
    }

    public ModeSetting getBridgeMode() {
        return bridgeMode;
    }

    public BooleanSetting getAutoPitch() {
        return autoPitch;
    }

    public static boolean shouldTriggerPlacement(double distToEdge, boolean movingBackwards, double threshold) {
        return movingBackwards && distToEdge <= threshold;
    }
}
