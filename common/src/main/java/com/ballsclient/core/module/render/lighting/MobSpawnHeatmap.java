package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * 3D gradient heatmap plane drawn over floor blocks showing mob spawning probability.
 */
public class MobSpawnHeatmap extends Module {
    private final NumberSetting planeAlpha = new NumberSetting("PlaneAlpha", "Heatmap plane opacity", 0.4, 0.1, 1.0, 0.05);
    private final ColorSetting dangerousColor = new ColorSetting("DangerousColor", "Color for 100% spawnable blocks", 0xFFFF2222);

    public MobSpawnHeatmap() {
        super("mob-spawn-heatmap", "Mob Spawn Heatmap", "3D gradient heatmap plane visualizing monster spawn likelihood", Category.RENDER);
        addSettings(planeAlpha, dangerousColor);
    }

    public double getPlaneAlpha() { return planeAlpha.getValue(); }
    public int getDangerousColor() { return dangerousColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render heatmap planes
    }
}
