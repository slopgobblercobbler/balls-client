package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * PistonTimingProfiler — Displays piston extension (2 ticks), retraction, and push limit (12 blocks) warnings.
 */
public class PistonTimingProfiler extends Module {
    private final BooleanSetting checkPushLimit = new BooleanSetting("CheckPushLimit", "Flags clusters exceeding 12 movable blocks", true);

    public PistonTimingProfiler() {
        super("piston-timing-profiler", "Piston Timing Profiler", "Displays piston extension (2 ticks), retraction, and push limit (12 blocks) warnings", Category.AUTOMATION);
        addSettings(checkPushLimit);
    }

    public BooleanSetting getCheckPushLimit() {
        return checkPushLimit;
    }

    public static boolean exceedsPushLimit(int blockCount) {
        return blockCount > 12;
    }

    public static int getExtensionTicks() {
        return 2;
    }
}
