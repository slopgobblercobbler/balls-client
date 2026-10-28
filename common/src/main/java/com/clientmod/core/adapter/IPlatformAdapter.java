package com.clientmod.core.adapter;

/**
 * Root Platform Adapter aggregating version-specific bridges.
 */
public interface IPlatformAdapter {
    IItemDataAccessor<?> getItemDataAccessor();
    IGuiRenderer<?, ?> getGuiRenderer();
    String getMinecraftVersion();
}
