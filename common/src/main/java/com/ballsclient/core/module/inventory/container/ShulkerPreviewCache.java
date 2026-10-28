package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * ShulkerPreviewCache — Generates and caches 27-slot hover tooltips and NBT contents for shulker boxes.
 */
public class ShulkerPreviewCache extends Module {
    private final NumberSetting maxCacheSize = new NumberSetting("MaxCacheSize", "Maximum cached shulker tooltips", 128.0, 16.0, 512.0, 16.0);
    private final BooleanSetting colorCodeShulkers = new BooleanSetting("ColorCodeShulkers", "Color-code shulker boxes based on dye or contents", true);

    public ShulkerPreviewCache() {
        super("shulker-preview-cache", "Shulker Preview Cache", "Generates and caches 27-slot hover tooltips and NBT contents for shulker boxes", Category.INVENTORY);
        addSettings(maxCacheSize, colorCodeShulkers);
    }

    public NumberSetting getMaxCacheSize() {
        return maxCacheSize;
    }

    public BooleanSetting getColorCodeShulkers() {
        return colorCodeShulkers;
    }

    public static String computeShulkerHash(List<ItemSlot> contents) {
        if (contents == null) return "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            for (ItemSlot slot : contents) {
                if (slot != null && !slot.isEmpty()) {
                    String line = slot.slotIndex() + ":" + slot.itemId() + ":" + slot.count() + ":" + (slot.displayName() != null ? slot.displayName() : "");
                    digest.update(line.getBytes(StandardCharsets.UTF_8));
                }
            }
            byte[] hash = digest.digest();
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            return String.valueOf(contents.hashCode());
        }
    }

    public static <K, V> Map<K, V> createCache(int capacity) {
        return new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }
}
