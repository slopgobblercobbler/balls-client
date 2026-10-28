package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Axe Stunner — Auto-swaps to axe when target raises shield, executing a 100-tick shield stun
 */
public class AxeStunner extends Module {
    private final NumberSetting swapBackDelayMs = new NumberSetting("SwapBackDelayMs", "Delay before switching back to previous weapon", 150.0, 50.0, 500.0, 10.0);
    private final BooleanSetting checkCharge = new BooleanSetting("CheckCharge", "Ensure axe has 100% attack charge before striking", true);

    public AxeStunner() {
        super("axe-stunner", "Axe Stunner", "Auto-swaps to axe when target raises shield, executing a 100-tick shield stun", Category.COMBAT);
        addSettings(swapBackDelayMs, checkCharge);
    }

    public double getSwapBackDelayMs() {
        return swapBackDelayMs.getValue();
    }

    public boolean isCheckCharge() {
        return checkCharge.getValue();
    }

    public boolean shouldExecuteAxeStun(boolean targetBlocking, boolean axeEquipped, double attackCharge) {
        return targetBlocking && axeEquipped && attackCharge >= 0.95;
    }
}
