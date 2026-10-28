package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * MiningExhaustionReducer — Monitors block breaking exhaustion (+0.005/block) to advise optimal pacing.
 */
public class MiningExhaustionReducer extends Module {
    private final NumberSetting exhaustionWarningThreshold = new NumberSetting("ExhaustionWarningThreshold", "Food exhaustion limit to alert player", 3.5, 1.0, 4.0, 0.1);
    private final BooleanSetting throttleRateWhenExhausted = new BooleanSetting("ThrottleRateWhenExhausted", "Slow mining when exhaustion is critical", false);

    public MiningExhaustionReducer() {
        super("mining-exhaustion-reducer", "Mining Exhaustion Reducer", "Monitors block breaking exhaustion (+0.005/block) to advise optimal pacing", Category.PLAYER);
        addSettings(exhaustionWarningThreshold, throttleRateWhenExhausted);
    }

    public NumberSetting getExhaustionWarningThreshold() {
        return exhaustionWarningThreshold;
    }

    public BooleanSetting getThrottleRateWhenExhausted() {
        return throttleRateWhenExhausted;
    }

    public static double computeAccumulatedExhaustion(int blocksBroken, double exhaustionPerBlock) {
        return blocksBroken * exhaustionPerBlock;
    }
}
