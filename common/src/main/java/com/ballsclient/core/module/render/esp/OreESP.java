package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 3D highlighting on subterranean ore veins (Diamonds, Ancient Debris, Gold).
 */
public class OreESP extends Module {
    private final BooleanSetting diamonds = new BooleanSetting("Diamonds", "Highlight diamond ore veins (Cyan)", true);
    private final BooleanSetting ancientDebris = new BooleanSetting("AncientDebris", "Highlight ancient debris (Crimson)", true);
    private final BooleanSetting gold = new BooleanSetting("Gold", "Highlight gold ore (Yellow)", true);
    private final NumberSetting searchRadius = new NumberSetting("SearchRadius", "Radius of block search cube", 16.0, 4.0, 32.0, 2.0);

    public enum OreType {
        DIAMOND(0xFF00FFFF),
        ANCIENT_DEBRIS(0xFF8B0000),
        GOLD(0xFFFFD700);

        public final int color;
        OreType(int color) { this.color = color; }
    }

    public static class OreBlock {
        public final int x, y, z;
        public final OreType type;

        public OreBlock(int x, int y, int z, OreType type) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.type = type;
        }
    }

    private final List<OreBlock> detectedOres = new ArrayList<>();

    public OreESP() {
        super("ore-esp", "Ore ESP", "Highlights high-value mineral veins through terrain", Category.RENDER);
        addSettings(diamonds, ancientDebris, gold, searchRadius);
    }

    public synchronized void updateOres(List<OreBlock> ores) {
        detectedOres.clear();
        if (ores != null) {
            for (OreBlock o : ores) {
                if (o.type == OreType.DIAMOND && !diamonds.getValue()) continue;
                if (o.type == OreType.ANCIENT_DEBRIS && !ancientDebris.getValue()) continue;
                if (o.type == OreType.GOLD && !gold.getValue()) continue;
                detectedOres.add(o);
            }
        }
    }

    public synchronized List<OreBlock> getDetectedOres() {
        return Collections.unmodifiableList(new ArrayList<>(detectedOres));
    }

    public int getSearchRadius() { return searchRadius.getIntValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D ore bounding wireframes
    }
}
