package com.clientmod.fabric1211.adapter;

import com.clientmod.core.adapter.IGuiRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public class Fabric1211GuiRenderer implements IGuiRenderer<DrawContext, ItemStack> {
    @Override
    public void drawString(DrawContext context, String text, int x, int y, int color, boolean shadow) {
        if (context == null || text == null) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.textRenderer != null) {
            context.drawText(client.textRenderer, text, x, y, color, shadow);
        }
    }

    @Override
    public void drawItem(DrawContext context, ItemStack stack, int x, int y) {
        if (context == null || stack == null || stack.isEmpty()) return;
        context.drawItem(stack, x, y);
    }

    @Override
    public void fill(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        if (context == null) return;
        context.fill(x1, y1, x2, y2, color);
    }
}
