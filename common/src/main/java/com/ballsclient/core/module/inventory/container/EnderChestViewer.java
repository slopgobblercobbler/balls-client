package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * EnderChestViewer — Persistent offline cache and remote viewer for 27-slot player ender chest contents.
 */
public class EnderChestViewer extends Module {
    private final BooleanSetting persistAcrossSessions = new BooleanSetting("PersistAcrossSessions", "Persist ender chest contents across server disconnects", true);
    private static final List<ItemSlot> CACHED_SLOTS = new ArrayList<>();

    public EnderChestViewer() {
        super("ender-chest-viewer", "Ender Chest Viewer", "Persistent offline cache and remote viewer for 27-slot player ender chest contents", Category.INVENTORY);
        addSettings(persistAcrossSessions);
    }

    public BooleanSetting getPersistAcrossSessions() {
        return persistAcrossSessions;
    }

    public static synchronized void updateCache(List<ItemSlot> slots) {
        CACHED_SLOTS.clear();
        if (slots != null) {
            CACHED_SLOTS.addAll(slots);
        }
    }

    public static synchronized List<ItemSlot> queryCachedSlots() {
        return Collections.unmodifiableList(new ArrayList<>(CACHED_SLOTS));
    }
}
