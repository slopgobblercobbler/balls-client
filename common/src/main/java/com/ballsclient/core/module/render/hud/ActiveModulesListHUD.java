package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Animated arraylist displaying all currently enabled client modules with Chroma Wave gradient.
 */
public class ActiveModulesListHUD extends Module {
    private final ModeSetting sort = new ModeSetting("Sort", "Sorting criteria for arraylist", "LENGTH", "LENGTH", "ALPHABETICAL");
    private final ColorSetting chromaWave = new ColorSetting("ChromaWave", "Chroma wave color gradient", 0xFFFF00FF, true, 1.5);
    private final NumberSetting waveSpeed = new NumberSetting("WaveSpeed", "Speed of rainbow color wave", 1.0, 0.1, 5.0, 0.1);

    public ActiveModulesListHUD() {
        super("active-modules-list-hud", "Active Modules List HUD", "Animated list of enabled modules sorted by length or name", Category.RENDER);
        addSettings(sort, chromaWave, waveSpeed);
    }

    public List<String> sortActiveModules(List<Module> activeModules) {
        List<String> names = new ArrayList<>();
        if (activeModules != null) {
            for (Module m : activeModules) {
                if (m.isEnabled()) {
                    names.add(m.getName());
                }
            }
        }
        if (sort.is("LENGTH")) {
            names.sort(Comparator.comparingInt(String::length).reversed());
        } else {
            names.sort(String.CASE_INSENSITIVE_ORDER);
        }
        return Collections.unmodifiableList(names);
    }

    public int getWaveColor(int index, long timeMillis) {
        if (!chromaWave.isChroma()) return chromaWave.getValue();
        float hue = (float) (((timeMillis * waveSpeed.getValue() * 0.001) + (index * 0.05)) % 1.0);
        return ColorSetting.hsbToRgb(hue, 0.8f, 1.0f);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render arraylist
    }
}
