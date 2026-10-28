package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Animated audio spectrum bar graph at the screen bottom bouncing to simulated audio amplitude.
 */
public class ChromaWaveformHUD extends Module {
    private final NumberSetting barCount = new NumberSetting("BarCount", "Number of equalizer spectrum bars", 32.0, 8.0, 64.0, 4.0);
    private final ColorSetting chromaColor = new ColorSetting("ChromaColor", "Equalizer rainbow gradient tint", 0xFFFF00FF, true, 2.0);
    private final NumberSetting height = new NumberSetting("Height", "Maximum bar height in pixels", 40.0, 10.0, 100.0, 5.0);

    private final float[] spectrumHeights = new float[64];

    public ChromaWaveformHUD() {
        super("chroma-waveform-hud", "Chroma Waveform HUD", "Bouncing audio equalizer spectrum bar graph with rainbow wave", Category.RENDER);
        addSettings(barCount, chromaColor, height);
    }

    public void updateSimulatedAudio(long timeMillis) {
        int count = barCount.getIntValue();
        float maxHeight = height.getFloatValue();
        for (int i = 0; i < count; i++) {
            double freq = 0.005 * (i + 1);
            double wave = Math.sin(timeMillis * freq) * Math.cos(timeMillis * 0.002 * (i * 0.5));
            spectrumHeights[i] = (float) (Math.abs(wave) * maxHeight);
        }
    }

    public float getBarHeight(int index) {
        if (index >= 0 && index < spectrumHeights.length) {
            return spectrumHeights[index];
        }
        return 0.0f;
    }

    public int getBarColor(int index, long timeMillis) {
        return chromaColor.getEffectiveRgba(timeMillis + (index * 40L));
    }

    public int getBarCount() { return barCount.getIntValue(); }
    public double getHeight() { return height.getValue(); }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render audio equalizer spectrum
    }
}
