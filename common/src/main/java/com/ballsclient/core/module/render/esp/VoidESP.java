package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Highlights exposed void holes in the End and Nether bedrock roof/floor with warning boxes.
 */
public class VoidESP extends Module {
    private final NumberSetting searchRadius = new NumberSetting("SearchRadius", "Block radius to scan for void holes", 24.0, 5.0, 64.0, 1.0);
    private final ColorSetting warningColor = new ColorSetting("WarningColor", "Warning outline color for void gap", 0xFFFF0000);
    private final BooleanSetting netherRoof = new BooleanSetting("NetherRoof", "Scan Nether bedrock ceiling (Y=127) for breaks", true);

    public static class VoidHole {
        public final int x, y, z;
        public VoidHole(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private final List<VoidHole> detectedHoles = new ArrayList<>();

    public VoidESP() {
        super("void-esp", "Void ESP", "Highlights lethal void falls and broken bedrock holes", Category.RENDER);
        addSettings(searchRadius, warningColor, netherRoof);
    }

    public synchronized void updateHoles(List<VoidHole> holes) {
        detectedHoles.clear();
        if (holes != null) {
            detectedHoles.addAll(holes);
        }
    }

    public synchronized List<VoidHole> getDetectedHoles() {
        return Collections.unmodifiableList(new ArrayList<>(detectedHoles));
    }

    public int getSearchRadius() { return searchRadius.getIntValue(); }
    public int getWarningColor() { return warningColor.getValue(); }
    public boolean isNetherRoofEnabled() { return netherRoof.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render void warning boxes
    }
}
