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
 * Color-coded 3D wireframe boxes on storage containers (Chests, Shulkers, Ender Chests, Barrels).
 */
public class StorageESP extends Module {
    private final BooleanSetting chests = new BooleanSetting("Chests", "Highlight standard wooden chests (Gold)", true);
    private final BooleanSetting shulkers = new BooleanSetting("Shulkers", "Highlight shulker boxes (Magenta)", true);
    private final BooleanSetting enderChests = new BooleanSetting("EnderChests", "Highlight ender chests (Purple)", true);
    private final NumberSetting alpha = new NumberSetting("Alpha", "Translucency alpha factor", 0.6, 0.1, 1.0, 0.05);

    public enum StorageType {
        CHEST(0xFFFFD700),
        SHULKER(0xFFFF00FF),
        ENDER_CHEST(0xFF9900FF),
        BARREL(0xFF8B4513);

        public final int defaultColor;
        StorageType(int color) { this.defaultColor = color; }
    }

    public static class StorageBlock {
        public final int x, y, z;
        public final StorageType type;

        public StorageBlock(int x, int y, int z, StorageType type) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.type = type;
        }
    }

    private final List<StorageBlock> visibleStorages = new ArrayList<>();

    public StorageESP() {
        super("storage-esp", "Storage ESP", "Color-coded bounding boxes on chests, shulker boxes, and containers", Category.RENDER);
        addSettings(chests, shulkers, enderChests, alpha);
    }

    public synchronized void updateStorageList(List<StorageBlock> list) {
        visibleStorages.clear();
        if (list != null) {
            for (StorageBlock s : list) {
                if (s.type == StorageType.CHEST && !chests.getValue()) continue;
                if (s.type == StorageType.SHULKER && !shulkers.getValue()) continue;
                if (s.type == StorageType.ENDER_CHEST && !enderChests.getValue()) continue;
                visibleStorages.add(s);
            }
        }
    }

    public synchronized List<StorageBlock> getVisibleStorages() {
        return Collections.unmodifiableList(new ArrayList<>(visibleStorages));
    }

    public int getStorageColorWithAlpha(StorageType type) {
        int a = (int) (alpha.getValue() * 255.0);
        return (a << 24) | (type.defaultColor & 0x00FFFFFF);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // 3D wireframe render pass
    }
}
