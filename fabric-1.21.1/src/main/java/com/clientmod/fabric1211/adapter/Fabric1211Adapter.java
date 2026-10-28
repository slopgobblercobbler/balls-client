package com.clientmod.fabric1211.adapter;

import com.clientmod.core.adapter.IGuiRenderer;
import com.clientmod.core.adapter.IItemDataAccessor;
import com.clientmod.core.adapter.IPlatformAdapter;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.DrawContext;

public class Fabric1211Adapter implements IPlatformAdapter {
    private final Fabric1211ItemDataAccessor itemDataAccessor = new Fabric1211ItemDataAccessor();
    private final Fabric1211GuiRenderer guiRenderer = new Fabric1211GuiRenderer();

    @Override
    public IItemDataAccessor<ItemStack> getItemDataAccessor() {
        return itemDataAccessor;
    }

    @Override
    public IGuiRenderer<DrawContext, ItemStack> getGuiRenderer() {
        return guiRenderer;
    }

    @Override
    public String getMinecraftVersion() {
        return "1.21.1";
    }
}
