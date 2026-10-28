package com.clientmod.fabric263.adapter;

import com.clientmod.core.adapter.IGuiRenderer;
import com.clientmod.core.adapter.IItemDataAccessor;
import com.clientmod.core.adapter.IPlatformAdapter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class Fabric263Adapter implements IPlatformAdapter {
    private final Fabric263ItemDataAccessor itemDataAccessor = new Fabric263ItemDataAccessor();
    private final Fabric263GuiRenderer guiRenderer = new Fabric263GuiRenderer();

    @Override
    public IItemDataAccessor<ItemStack> getItemDataAccessor() {
        return itemDataAccessor;
    }

    @Override
    public IGuiRenderer<GuiGraphics, ItemStack> getGuiRenderer() {
        return guiRenderer;
    }

    @Override
    public String getMinecraftVersion() {
        return "26.3";
    }
}
