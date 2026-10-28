package com.ballsclient.fabric1201;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import com.ballsclient.core.BallsClientCore;
import com.clientmod.core.adapter.PlatformAdapterRegistry;
import com.clientmod.fabric1201.adapter.Fabric1201Adapter;

@Environment(EnvType.CLIENT)
public class BallsClientFabric1201 implements ClientModInitializer {
    public static final String KEY_CATEGORY = "key.category.ballsclient";

    public static KeyBinding ZOOM_KEY;
    public static KeyBinding FULLBRIGHT_KEY;
    public static KeyBinding MENU_KEY;
    public static KeyBinding SORT_KEY;

    @Override
    public void onInitializeClient() {
        BallsClientCore.LOGGER.info("Initializing BallsClient for Fabric 1.20.1...");

        // 1. Initialize Common Core Domain Logic
        BallsClientCore.init();

        // 2. Register Platform Adapter for 1.20.1 (Legacy NBT BlockEntityTag)
        PlatformAdapterRegistry.setAdapter(new Fabric1201Adapter());

        // 3. Register Keybindings
        registerKeybindings();

        // 4. Register Client Tick Events
        registerTickEvents();

        BallsClientCore.LOGGER.info("BallsClient Fabric 1.20.1 initialization complete with {} modules.",
                BallsClientCore.getModuleManager().getModules().size());
    }

    private void registerKeybindings() {
        ZOOM_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ballsclient.zoom",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            KEY_CATEGORY
        ));

        FULLBRIGHT_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ballsclient.fullbright",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            KEY_CATEGORY
        ));

        MENU_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ballsclient.menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KEY_CATEGORY
        ));

        SORT_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ballsclient.sort",
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

            // Update auto-sprint state machine
            boolean forwardPressed = client.options.forwardKey.isPressed();
            BallsClientCore.getAutoSprintController().onTick(forwardPressed);

            // Execute scheduled macro queue
            BallsClientCore.getMacroEngine().onTick();
        });
    }
}
