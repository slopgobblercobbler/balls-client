package com.clientmod.fabric1201.adapter;

import com.clientmod.core.adapter.IGuiRenderer;
import com.clientmod.core.adapter.IItemDataAccessor;
import com.clientmod.core.adapter.IPlatformAdapter;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.DrawContext;

public class Fabric1201Adapter implements IPlatformAdapter {
    private final Fabric1201ItemDataAccessor itemDataAccessor = new Fabric1201ItemDataAccessor();
    private final Fabric1201GuiRenderer guiRenderer = new Fabric1201GuiRenderer();

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
        return "1.20.1";
    }
}
