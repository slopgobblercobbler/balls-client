package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Client-side visual time of day override (Eternal Day, Midnight, Sunset, or Custom).
 */
public class TimeChanger extends Module {
    private final ModeSetting preset = new ModeSetting("Preset", "Time preset configuration", "DAY", "DAY", "NIGHT", "SUNSET", "CUSTOM");
    private final NumberSetting customTime = new NumberSetting("CustomTime", "Tick time when preset is CUSTOM", 6000.0, 0.0, 24000.0, 500.0);

    public TimeChanger() {
        super("time-changer", "Time Changer", "Client-side celestial time of day override without affecting server ticks", Category.RENDER);
        addSettings(preset, customTime);
    }

    public long getEffectiveWorldTime() {
        if (preset.is("DAY")) return 6000L;
        if (preset.is("NIGHT")) return 18000L;
        if (preset.is("SUNSET")) return 12500L;
        return customTime.getIntValue();
    }

    public String getPreset() { return preset.getValue(); }
}
