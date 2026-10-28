package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Shield Charge Indicator — Displays opponent shield stun recovery timer countdown
 */
public class ShieldChargeIndicator extends Module {
    private final BooleanSetting renderProgressBar = new BooleanSetting("RenderProgressBar", "Render 2D bar above opponent", true);
    private final BooleanSetting notifyRecovery = new BooleanSetting("NotifyRecovery", "Ding when opponent shield recovers", false);

    public ShieldChargeIndicator() {
        super("shield-charge-indicator", "Shield Charge Indicator", "Displays opponent shield stun recovery timer countdown", Category.COMBAT);
        addSettings(renderProgressBar, notifyRecovery);
    }

    public boolean isRenderProgressBar() {
        return renderProgressBar.getValue();
    }

    public boolean isNotifyRecovery() {
        return notifyRecovery.getValue();
    }

    public double calculateShieldStunProgress(int remainingTicks, int totalStunTicks) {
        return Math.max(0.0, Math.min(1.0, 1.0 - (double) remainingTicks / totalStunTicks));
    }
}
