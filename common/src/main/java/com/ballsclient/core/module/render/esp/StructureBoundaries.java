package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Visualizes 3D generation bounding boxes of world structures (Nether Fortresses, Ocean Monuments, Villages).
 */
public class StructureBoundaries extends Module {
    private final BooleanSetting showBoundingBox = new BooleanSetting("ShowBox", "Draw 3D outer perimeter bounding box", true);
    private final ColorSetting color = new ColorSetting("Color", "Boundary line wireframe color", 0xFFFFAA00);

    public static class StructureRegion {
        public final String name;
        public final int minX, minY, minZ;
        public final int maxX, maxY, maxZ;

        public StructureRegion(String name, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
            this.name = name != null ? name : "Structure";
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
        }

        public boolean contains(int x, int y, int z) {
            return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
        }
    }

    private final List<StructureRegion> structures = new ArrayList<>();

    public StructureBoundaries() {
        super("structure-boundaries", "Structure Boundaries", "3D bounding boxes highlighting world-generated structure sectors", Category.RENDER);
        addSettings(showBoundingBox, color);
    }

    public synchronized void updateStructures(List<StructureRegion> list) {
        structures.clear();
        if (list != null) {
            structures.addAll(list);
        }
    }

    public synchronized List<StructureRegion> getStructures() {
        return Collections.unmodifiableList(new ArrayList<>(structures));
    }

    public boolean isShowBoundingBox() { return showBoundingBox.getValue(); }
    public int getColor() { return color.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render structure boxes
    }
}
