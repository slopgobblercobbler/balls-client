package com.clientmod.fabric1211;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import com.clientmod.core.ClientModCore;
import com.clientmod.core.adapter.PlatformAdapterRegistry;
import com.clientmod.fabric1211.adapter.Fabric1211Adapter;

@Environment(EnvType.CLIENT)
public class ClientModFabric1211 implements ClientModInitializer {
    public static final String KEY_CATEGORY = "key.category.clientmod";

    public static KeyBinding ZOOM_KEY;
    public static KeyBinding FULLBRIGHT_KEY;
    public static KeyBinding MENU_KEY;
    public static KeyBinding SORT_KEY;

    @Override
    public void onInitializeClient() {
        ClientModCore.LOGGER.info("Initializing ClientMod for Fabric 1.21.1...");

        // 1. Initialize Common Core Domain Logic
        ClientModCore.init();

        // 2. Register Platform Adapter for 1.21.1 (Modern Data Components)
        PlatformAdapterRegistry.setAdapter(new Fabric1211Adapter());

        // 3. Register Keybindings
        registerKeybindings();

        // 4. Register Client Tick Events
        registerTickEvents();

        ClientModCore.LOGGER.info("ClientMod Fabric 1.21.1 initialization complete.");
    }

    private void registerKeybindings() {
        ZOOM_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.clientmod.zoom",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            KEY_CATEGORY
        ));

        FULLBRIGHT_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.clientmod.fullbright",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            KEY_CATEGORY
        ));

        MENU_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.clientmod.menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KEY_CATEGORY
        ));

        SORT_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.clientmod.sort",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            KEY_CATEGORY
        ));
    }

    private void registerTickEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                return;
            }

            boolean forwardPressed = client.options.forwardKey.isPressed();
            ClientModCore.getAutoSprintController().onTick(forwardPressed);
            ClientModCore.getMacroEngine().onTick();
        });
    }
}
