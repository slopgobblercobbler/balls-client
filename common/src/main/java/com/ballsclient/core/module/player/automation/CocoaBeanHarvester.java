package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * CocoaBeanHarvester — Detects fully mature cocoa pods (age 2) on jungle logs, harvesting and replanting.
 */
public class CocoaBeanHarvester extends Module {
    private final BooleanSetting autoReplant = new BooleanSetting("AutoReplant", "Replant cocoa beans on trunk face after harvest", true);
    private final NumberSetting matureAge = new NumberSetting("MatureAge", "Cocoa pod growth age defining maturity", 2.0, 1.0, 2.0, 1.0);

    public CocoaBeanHarvester() {
        super("cocoa-bean-harvester", "Cocoa Bean Harvester", "Detects fully mature cocoa pods (age 2) on jungle logs, harvesting and replanting", Category.PLAYER);
        addSettings(autoReplant, matureAge);
    }

    public BooleanSetting getAutoReplant() {
        return autoReplant;
    }

    public NumberSetting getMatureAge() {
        return matureAge;
    }

    public static boolean isCocoaMature(int age, double matureAge) {
        return age >= (int) matureAge;
    }
}
