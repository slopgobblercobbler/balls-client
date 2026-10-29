package com.ballsclient.fabric263.mixin;

import com.ballsclient.fabric263.gui.clickgui.ClickGuiCanvas;
import com.ballsclient.fabric263.gui.clickgui.ModernClickGuiScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenEventsMixin {

    /**
     * Intercepts 26.3 Screen rendering and dispatches into ClickGuiCanvas.
     */
    @Inject(
        method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 0
    )
    private void onExtractRenderState(@Coerce Object extractor, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if ((Object) this instanceof ModernClickGuiScreen gui) {
            gui.render(ClickGuiCanvas.of(extractor), mouseX, mouseY, delta);
            ci.cancel();
        }
    }

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
    private void onExtractBackground(@Coerce Object extractor, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if ((Object) this instanceof ModernClickGuiScreen) {
            ci.cancel();
        }
    }

    /**
     * Intercepts 26.3 key events for inline keybind listeners and ESC/RightShift closing.
     */
    @Inject(
        method = "keyPressed(Lnet/minecraft/client/input/KeyEvent;)Z",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 0
    )
    private void onKeyPressed(@Coerce Object keyEvent, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ModernClickGuiScreen gui) {
            if (gui.keyPressed(keyEvent)) {
                cir.setReturnValue(true);
            }
        }
    }
}
