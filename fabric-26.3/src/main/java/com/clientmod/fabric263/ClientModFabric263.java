package com.clientmod.fabric263;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import com.clientmod.core.ClientModCore;
import com.clientmod.core.adapter.PlatformAdapterRegistry;
import com.clientmod.fabric263.adapter.Fabric263Adapter;

@Environment(EnvType.CLIENT)
public class ClientModFabric263 implements ClientModInitializer {
    public static final String KEY_CATEGORY = "key.category.clientmod";

    public static KeyMapping ZOOM_KEY;
    public static KeyMapping FULLBRIGHT_KEY;
    public static KeyMapping MENU_KEY;
    public static KeyMapping SORT_KEY;

    @Override
    public void onInitializeClient() {
        ClientModCore.LOGGER.info("Initializing ClientMod for Fabric 26.3 (Java 25, Mojang Mappings)...");

        // 1. Initialize Common Core Domain Logic
        ClientModCore.init();

        // 2. Register Platform Adapter for 26.3 (Java 25 Data Components & Mojang Mappings)
        PlatformAdapterRegistry.setAdapter(new Fabric263Adapter());

        // 3. Register Keybindings
        registerKeybindings();

        // 4. Register Client Tick Events
        registerTickEvents();

        ClientModCore.LOGGER.info("ClientMod Fabric 26.3 initialization complete.");
    }

    private void registerKeybindings() {
        ZOOM_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.clientmod.zoom",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            KEY_CATEGORY
        ));

        FULLBRIGHT_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.clientmod.fullbright",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            KEY_CATEGORY
        ));

        MENU_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.clientmod.menu",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KEY_CATEGORY
        ));

        SORT_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.clientmod.sort",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            KEY_CATEGORY
        ));
    }

    private void registerTickEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                return;
            }

            boolean forwardPressed = client.options.keyUp.isDown();
            ClientModCore.getAutoSprintController().onTick(forwardPressed);
            ClientModCore.getMacroEngine().onTick();
        });
    }
}
