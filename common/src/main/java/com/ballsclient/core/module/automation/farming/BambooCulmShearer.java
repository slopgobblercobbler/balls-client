package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BambooCulmShearer — Trims towering bamboo stalks down to 1 block above root for automated fuel harvest.
 */
public class BambooCulmShearer extends Module {
    private final NumberSetting cutHeightAboveRoot = new NumberSetting("CutHeightAboveRoot", "Vertical offset above root block where break action occurs", 1.0, 1.0, 4.0, 1.0);

    public BambooCulmShearer() {
        super("bamboo-culm-shearer", "Bamboo Culm Shearer", "Trims towering bamboo stalks down to 1 block above root for automated fuel harvest", Category.AUTOMATION);
        addSettings(cutHeightAboveRoot);
    }

    public NumberSetting getCutHeightAboveRoot() {
        return cutHeightAboveRoot;
    }

    public int getCutY(int rootY) {
        return rootY + (int) Math.round(cutHeightAboveRoot.getValue());
    }
}
