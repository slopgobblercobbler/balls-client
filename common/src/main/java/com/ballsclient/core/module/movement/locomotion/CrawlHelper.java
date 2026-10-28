package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Automates entry into 1-block crawling space with stance spoofing.
 */
public class CrawlHelper extends Module {
    private final BooleanSetting autoTrigger = new BooleanSetting("AutoTrigger", "Automatically activate crawling when 1-block gap ahead", true);
    private final NumberSetting speedBonus = new NumberSetting("SpeedBonus", "Crawling traversal speed scaling multiplier", 1.2, 1.0, 2.0, 0.05);

    public CrawlHelper() {
        super("crawl-helper", "Crawl Helper", "Automates entry into 1-block crawling space with stance spoofing", Category.MOVEMENT);
        addSettings(autoTrigger, speedBonus);
    }

    public boolean isAutoTrigger() { return autoTrigger.getValue(); }
    public double getSpeedBonus() { return speedBonus.getValue(); }

    public boolean shouldCrawl(double ceilingHeight) {
        return isEnabled() && isAutoTrigger() && ceilingHeight <= 1.5;
    }

    public double getSpeedMultiplier() {
        return isEnabled() ? getSpeedBonus() : 1.0;
    }
}
