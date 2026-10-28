package com.clientmod.core.adapter;

import java.util.List;

/**
 * Platform adapter interface for inspecting item stack metadata
 * across version changes (legacy NBT vs Data Components).
 */
public interface IItemDataAccessor<TStack> {
    List<TStack> getContainerContents(TStack stack);
    int getDamage(TStack stack);
    int getMaxDamage(TStack stack);
    boolean isDamageable(TStack stack);
}
