package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * Client-side weather override with custom rain and snow color palettes.
 */
public class WeatherColorChanger extends Module {
    private final ModeSetting weather = new ModeSetting("Weather", "Visual weather state", "CLEAR", "CLEAR", "RAIN", "SNOW");
    private final ColorSetting rainColor = new ColorSetting("RainColor", "Color of falling rain streaks", 0xFF3300AA);

    public WeatherColorChanger() {
        super("weather-color-changer", "Weather Color Changer", "Controls client-side precipitation and rain/snow tinting", Category.RENDER);
        addSettings(weather, rainColor);
    }

    public String getWeatherState() { return weather.getValue(); }
    public int getRainColor() { return rainColor.getValue(); }
}
