package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * NetherWartHarvester — Scans soul sand beds, harvests stage 3 nether wart, and sequences seed replanting.
 */
public class NetherWartHarvester extends Module {
    private final BooleanSetting replant = new BooleanSetting("Replant", "Automatically sows nether wart seed after breaking", true);
    private final NumberSetting delayTicks = new NumberSetting("DelayTicks", "Tick delay between break and replant packet", 1.0, 0.0, 5.0, 1.0);

    public NetherWartHarvester() {
        super("nether-wart-harvester", "Nether Wart Harvester", "Scans soul sand beds, harvests stage 3 nether wart, and sequences seed replanting", Category.AUTOMATION);
        addSettings(replant, delayTicks);
    }

    public BooleanSetting getReplant() {
        return replant;
    }

    public NumberSetting getDelayTicks() {
        return delayTicks;
    }

    public static boolean isMatureNetherWart(int age) {
        return age >= 3;
    }
}
