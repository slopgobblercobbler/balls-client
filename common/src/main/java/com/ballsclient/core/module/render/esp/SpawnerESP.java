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
 * 3D wireframe bounding boxes and entity type tags on monster spawners through terrain.
 */
public class SpawnerESP extends Module {
    private final BooleanSetting showMobName = new BooleanSetting("ShowMobName", "Display entity spawned (e.g. Blaze, Skeleton)", true);
    private final ColorSetting spawnerColor = new ColorSetting("SpawnerColor", "Color of spawner bounding box (Magenta)", 0xFFFF55FF);

    public static class SpawnerBlock {
        public final int x, y, z;
        public final String mobType;

        public SpawnerBlock(int x, int y, int z, String mobType) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.mobType = mobType != null ? mobType : "Unknown";
        }
    }

    private final List<SpawnerBlock> spawners = new ArrayList<>();

    public SpawnerESP() {
        super("spawner-esp", "Spawner ESP", "Highlights dungeon and fortress mob spawners through walls", Category.RENDER);
        addSettings(showMobName, spawnerColor);
    }

    public synchronized void updateSpawners(List<SpawnerBlock> list) {
        spawners.clear();
        if (list != null) {
            spawners.addAll(list);
        }
    }

    public synchronized List<SpawnerBlock> getSpawners() {
        return Collections.unmodifiableList(new ArrayList<>(spawners));
    }

    public boolean isShowMobName() { return showMobName.getValue(); }
    public int getSpawnerColor() { return spawnerColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render spawner boxes
    }
}
