package com.ballsclient.fabric263.mixin;

import com.ballsclient.fabric263.gui.ScreenHelper;
import com.ballsclient.fabric263.gui.clickgui.ModernClickGuiScreen;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @Shadow
    public abstract double xpos();

    @Shadow
    public abstract double ypos();

    private static Method buttonMethod;
    private static boolean buttonMethodResolved = false;

    private static int extractButton(Object buttonInfo) {
        if (buttonInfo == null) return 0;
        if (!buttonMethodResolved) {
            try {
                buttonMethod = buttonInfo.getClass().getMethod("button");
            } catch (Throwable ignored) {}
            buttonMethodResolved = true;
        }
        if (buttonMethod != null) {
            try {
                return ((Number) buttonMethod.invoke(buttonInfo)).intValue();
            } catch (Throwable ignored) {}
        }
        return 0;
    }

    /**
     * Intercepts mouse clicks and releases for ModernClickGuiScreen.
     * action: 1 = GLFW_PRESS, 0 = GLFW_RELEASE.
     */
    @Inject(
        method = "onButton(JLnet/minecraft/client/input/MouseButtonInfo;I)V",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 0
    )
    private void onButton(long windowHandle, @Coerce Object buttonInfo, int action, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client == null) return;

        Screen screen = ScreenHelper.getCurrentScreen(client);
        if (screen instanceof ModernClickGuiScreen gui) {
            Window window = client.getWindow();
            if (window == null) return;

            int scaledWidth = window.getGuiScaledWidth();
            int scaledHeight = window.getGuiScaledHeight();
            int screenWidth = window.getScreenWidth();
            int screenHeight = window.getScreenHeight();

            if (screenWidth <= 0 || screenHeight <= 0) return;

            double mouseX = this.xpos() * (double) scaledWidth / (double) screenWidth;
            double mouseY = this.ypos() * (double) scaledHeight / (double) screenHeight;
            int button = extractButton(buttonInfo);

            if (action == 1) { // PRESS
                if (gui.mouseClicked(mouseX, mouseY, button)) {
                    ci.cancel();
                }
            } else if (action == 0) { // RELEASE
                if (gui.mouseReleased(mouseX, mouseY, button)) {
                    ci.cancel();
                }
            }
        }
    }

    /**
     * Intercepts mouse movement while dragging panels or adjusting sliders.
     */
    @Inject(
        method = "onMove(JDDDD)V",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 0
    )
    private void onMove(long windowHandle, double x, double y, double deltaX, double deltaY, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client == null) return;

        Screen screen = ScreenHelper.getCurrentScreen(client);
        if (screen instanceof ModernClickGuiScreen gui) {
            Window window = client.getWindow();
            if (window == null) return;

            int scaledWidth = window.getGuiScaledWidth();
            int scaledHeight = window.getGuiScaledHeight();
            int screenWidth = window.getScreenWidth();
            int screenHeight = window.getScreenHeight();

            if (screenWidth <= 0 || screenHeight <= 0) return;

            double mouseX = x * (double) scaledWidth / (double) screenWidth;
            double mouseY = y * (double) scaledHeight / (double) screenHeight;

            gui.mouseDragged(mouseX, mouseY, 0, deltaX, deltaY);
        }
    }
}
