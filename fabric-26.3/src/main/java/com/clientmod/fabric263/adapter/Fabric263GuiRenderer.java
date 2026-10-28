package com.clientmod.fabric263.adapter;

import com.clientmod.core.adapter.IGuiRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class Fabric263GuiRenderer implements IGuiRenderer<GuiGraphics, ItemStack> {
    @Override
    public void drawString(GuiGraphics context, String text, int x, int y, int color, boolean shadow) {
        if (context == null || text == null) return;
        Minecraft client = Minecraft.getInstance();
        if (client.font != null) {
            context.drawString(client.font, text, x, y, color, shadow);
        }
    }

    @Override
    public void drawItem(GuiGraphics context, ItemStack stack, int x, int y) {
        if (context == null || stack == null || stack.isEmpty()) return;
        context.renderItem(stack, x, y);
    }

    @Override
    public void fill(GuiGraphics context, int x1, int y1, int x2, int y2, int color) {
        if (context == null) return;
        context.fill(x1, y1, x2, y2, color);
    }
}
