package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

import java.util.Random;

/**
 * AfkActivityCycler — Performs random micro-movements, jumps, and head pans at pseudo-random intervals to reset idle timers.
 */
public class AfkActivityCycler extends Module {
    public enum AfkAction {
        JUMP,
        SNEAK_TOGGLE,
        HEAD_PAN_LEFT,
        HEAD_PAN_RIGHT,
        SWING_ARM
    }

    private final NumberSetting minIntervalSeconds = new NumberSetting("MinIntervalSeconds", "Minimum randomized idle period in seconds", 45.0, 10.0, 300.0, 5.0);
    private final NumberSetting maxIntervalSeconds = new NumberSetting("MaxIntervalSeconds", "Maximum randomized idle period in seconds", 90.0, 20.0, 600.0, 10.0);

    private final Random random = new Random();
    private long nextActionTime = 0L;

    public AfkActivityCycler() {
        super("afk-activity-cycler", "AFK Activity Cycler", "Performs random micro-movements, jumps, and head pans at pseudo-random intervals", Category.AUTOMATION);
        addSettings(minIntervalSeconds, maxIntervalSeconds);
    }

    public NumberSetting getMinIntervalSeconds() {
        return minIntervalSeconds;
    }

    public NumberSetting getMaxIntervalSeconds() {
        return maxIntervalSeconds;
    }

    public void scheduleNext(long currentTime) {
        double min = minIntervalSeconds.getValue();
        double max = Math.max(min, maxIntervalSeconds.getValue());
        double interval = min + random.nextDouble() * (max - min);
        nextActionTime = currentTime + (long) (interval * 1000.0);
    }

    public boolean isDue(long currentTime) {
        return currentTime >= nextActionTime;
    }

    public AfkAction selectNextAction() {
        AfkAction[] actions = AfkAction.values();
        return actions[random.nextInt(actions.length)];
    }
}
