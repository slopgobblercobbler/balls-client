package com.ballsclient.core.module.inventory;

/**
 * Headless immutable representation of an inventory slot.
 */
public record ItemSlot(
        int slotIndex,
        String itemId,
        int count,
        int maxStackSize,
        int durability,
        int maxDurability,
        String displayName
) {
    public ItemSlot(int slotIndex, String itemId, int count) {
        this(slotIndex, itemId, count, 64, 0, 0, null);
    }

    public ItemSlot(int slotIndex, String itemId, int count, int durability, int maxDurability) {
        this(slotIndex, itemId, count, 64, durability, maxDurability, null);
    }

    public ItemSlot(int slotIndex, String itemId, int count, String displayName) {
        this(slotIndex, itemId, count, 64, 0, 0, displayName);
    }

    public boolean isEmpty() {
        return itemId == null || itemId.isEmpty() || count <= 0;
    }
}
