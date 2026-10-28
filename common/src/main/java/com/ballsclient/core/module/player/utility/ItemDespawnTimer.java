package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ItemDespawnTimer — Tracks 6000-tick (5-minute) despawn countdown for nearby dropped items.
 */
public class ItemDespawnTimer extends Module {
    private final NumberSetting warningThresholdSeconds = new NumberSetting("WarningThresholdSeconds", "Seconds remaining before item despawns", 30.0, 10.0, 120.0, 5.0);
    private final BooleanSetting highlightValuable = new BooleanSetting("HighlightValuable", "Highlight diamond and netherite gear timer in gold", true);
    private final ColorSetting expiringColor = new ColorSetting("ExpiringColor", "Color of expiring item label", 0xFFFF5555);

    public ItemDespawnTimer() {
        super("item-despawn-timer", "Item Despawn Timer", "Tracks 6000-tick (5-minute) despawn countdown for nearby dropped items", Category.PLAYER);
        addSettings(warningThresholdSeconds, highlightValuable, expiringColor);
    }

    public NumberSetting getWarningThresholdSeconds() {
        return warningThresholdSeconds;
    }

    public BooleanSetting getHighlightValuable() {
        return highlightValuable;
    }

    public ColorSetting getExpiringColor() {
        return expiringColor;
    }

    public static double getRemainingSeconds(int ageTicks, int maxLifetimeTicks) {
        return Math.max(0.0, (maxLifetimeTicks - ageTicks) / 20.0);
    }
}
