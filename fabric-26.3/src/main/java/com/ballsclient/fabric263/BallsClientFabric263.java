package com.ballsclient.fabric263;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import com.ballsclient.core.BallsClientCore;
import com.clientmod.core.adapter.PlatformAdapterRegistry;
import com.clientmod.fabric263.adapter.Fabric263Adapter;

@Environment(EnvType.CLIENT)
public class BallsClientFabric263 implements ClientModInitializer {
    public static final String KEY_CATEGORY = "key.category.ballsclient";

    public static KeyMapping ZOOM_KEY;
    public static KeyMapping FULLBRIGHT_KEY;
    public static KeyMapping MENU_KEY;
    public static KeyMapping SORT_KEY;

    @Override
    public void onInitializeClient() {
        BallsClientCore.LOGGER.info("Initializing BallsClient for Fabric 26.3 (Java 25, Mojang Mappings)...");

        // 1. Initialize Common Core Domain Logic (registers all 620+ modules)
        BallsClientCore.init();

        // 2. Register Platform Adapter for 26.3
        PlatformAdapterRegistry.setAdapter(new Fabric263Adapter());

        // 3. Register Keybindings
        registerKeybindings();

        // 4. Register Client Tick Events
        registerTickEvents();

        BallsClientCore.LOGGER.info("BallsClient Fabric 26.3 initialization complete with {} modules.",
                BallsClientCore.getModuleManager().getModules().size());
    }

    private void registerKeybindings() {
        ZOOM_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.ballsclient.zoom",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            KEY_CATEGORY
        ));

        FULLBRIGHT_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.ballsclient.fullbright",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            KEY_CATEGORY
        ));

        MENU_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.ballsclient.menu",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KEY_CATEGORY
        ));

        SORT_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.ballsclient.sort",
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
            BallsClientCore.getAutoSprintController().onTick(forwardPressed);
            BallsClientCore.getMacroEngine().onTick();
        });
    }
}
