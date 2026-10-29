package com.ballsclient.fabric263.mixin;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.event.KeyEvent;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    private static Method keyMethod;
    private static Method keycodeMethod;
    private static Method modifiersMethod;
    private static boolean accessorsInitialized;

    private static void initAccessors(Class<?> clazz) {
        if (!accessorsInitialized) {
            try {
                keyMethod = clazz.getMethod("key");
            } catch (Throwable ignored) {}
            try {
                keycodeMethod = clazz.getMethod("keycode");
            } catch (Throwable ignored) {}
            try {
                modifiersMethod = clazz.getMethod("modifiers");
            } catch (Throwable ignored) {}
            accessorsInitialized = true;
        }
    }

    @Inject(method = "keyPress(JILnet/minecraft/client/input/KeyEvent;)V", at = @At("HEAD"), cancellable = true)
    private void onKeyPress(long window, int action, @Coerce Object keyEvent, CallbackInfo ci) {
        try {
            if (BallsClientCore.getEventBus() != null && keyEvent != null) {
                initAccessors(keyEvent.getClass());

                int keyCode = 0;
                int scanCode = 0;
                int modifiers = 0;

                try {
                    if (keyMethod != null) {
                        keyCode = ((Number) keyMethod.invoke(keyEvent)).intValue();
                    }
                } catch (Throwable ignored) {}

                try {
                    if (keycodeMethod != null) {
                        scanCode = ((Number) keycodeMethod.invoke(keyEvent)).intValue();
                    }
                } catch (Throwable ignored) {}

                try {
                    if (modifiersMethod != null) {
                        modifiers = ((Number) modifiersMethod.invoke(keyEvent)).intValue();
                    }
                } catch (Throwable ignored) {}

                KeyEvent event = new KeyEvent(keyCode, scanCode, action, modifiers);
                BallsClientCore.getEventBus().post(event);
                if (event.isCancelled()) {
                    ci.cancel();
                }
            }
        } catch (Throwable t) {
            BallsClientCore.LOGGER.error("Error dispatching KeyEvent in KeyboardHandlerMixin: " + t.getMessage(), t);
        }
    }
}
