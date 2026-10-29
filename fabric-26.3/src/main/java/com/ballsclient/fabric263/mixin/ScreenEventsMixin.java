package com.ballsclient.fabric263.mixin;

import com.ballsclient.fabric263.gui.clickgui.ModernClickGuiScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenEventsMixin {

    /**
     * Suppresses vanilla menu blur and dirt background behind ModernClickGuiScreen.
     */
    @Inject(
        method = "extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 0
    )
    private void onExtractBackground(Object extractor, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if ((Object) this instanceof ModernClickGuiScreen) {
            ci.cancel();
        }
    }
}
