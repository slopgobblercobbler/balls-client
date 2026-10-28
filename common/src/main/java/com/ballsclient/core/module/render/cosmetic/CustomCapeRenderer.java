package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Custom animated client-side capes with waving cloth physics simulations.
 */
public class CustomCapeRenderer extends Module {
    private final ModeSetting capeStyle = new ModeSetting("Style", "Cape simulation mode", "PHYSICS", "ANIMATED", "STATIC", "PHYSICS");
    private final NumberSetting waveSpeed = new NumberSetting("WaveSpeed", "Waving cloth flutter speed", 1.0, 0.1, 3.0, 0.1);

    public CustomCapeRenderer() {
        super("custom-cape-renderer", "Custom Cape Renderer", "Custom animated and physics-driven waving cosmetic capes", Category.RENDER);
        addSettings(capeStyle, waveSpeed);
    }

    public String getCapeStyle() { return capeStyle.getValue(); }
    public double getWaveSpeed() { return waveSpeed.getValue(); }
}
