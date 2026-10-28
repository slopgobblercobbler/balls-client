package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * MeteorologicalClock — Predicts rain and thunder cycle transitions using world time modulo.
 */
public class MeteorologicalClock extends Module {
    private final BooleanSetting alertRainImminent = new BooleanSetting("AlertRainImminent", "Alert when rain timer drops below threshold", true);
    private final BooleanSetting alertThunderImminent = new BooleanSetting("AlertThunderImminent", "Alert when thunder storm approaches", true);
    private final NumberSetting warningMinutes = new NumberSetting("WarningMinutes", "Advance warning window in real minutes", 5.0, 1.0, 15.0, 1.0);

    public MeteorologicalClock() {
        super("meteorological-clock", "Meteorological Clock", "Predicts rain and thunder cycle transitions using world time modulo", Category.PLAYER);
        addSettings(alertRainImminent, alertThunderImminent, warningMinutes);
    }

    public BooleanSetting getAlertRainImminent() {
        return alertRainImminent;
    }

    public BooleanSetting getAlertThunderImminent() {
        return alertThunderImminent;
    }

    public NumberSetting getWarningMinutes() {
        return warningMinutes;
    }

    public static double getMinutesUntilWeatherChange(long clearTicks, long rainTicks) {
        long remainingTicks = clearTicks > 0 ? clearTicks : rainTicks;
        return (remainingTicks / 20.0) / 60.0;
    }

    public static boolean isWeatherHazardous(boolean isRaining, boolean isThundering) {
        return isThundering || isRaining;
    }
}
