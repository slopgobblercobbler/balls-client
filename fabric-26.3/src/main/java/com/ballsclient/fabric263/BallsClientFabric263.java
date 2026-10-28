package com.ballsclient.fabric263;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import com.ballsclient.core.BallsClientCore;
import com.clientmod.core.adapter.PlatformAdapterRegistry;
import com.clientmod.fabric263.adapter.Fabric263Adapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

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

        // 1. Initialize Common Core Domain Logic
        try {
            BallsClientCore.init();
        } catch (Throwable t) {
            BallsClientCore.LOGGER.error("Error during BallsClientCore initialization: " + t.getMessage(), t);
        }

        // 2. Register Platform Adapter for 26.3
        try {
            PlatformAdapterRegistry.setAdapter(new Fabric263Adapter());
        } catch (Throwable t) {
            BallsClientCore.LOGGER.error("Error setting Fabric263Adapter: " + t.getMessage(), t);
        }

        // 3. Register Keybindings (defensively via reflection to support 26.3 KeyMappingHelper)
        try {
            registerKeybindings();
        } catch (Throwable t) {
            BallsClientCore.LOGGER.warn("Failed to register keybindings: " + t.getMessage(), t);
        }

        // 4. Register Client Tick Events
        try {
            registerTickEvents();
        } catch (Throwable t) {
            BallsClientCore.LOGGER.warn("Failed to register tick events: " + t.getMessage(), t);
        }

        int moduleCount = (BallsClientCore.getModuleManager() != null) ? BallsClientCore.getModuleManager().getModules().size() : 0;
        BallsClientCore.LOGGER.info("BallsClient Fabric 26.3 initialization complete with {} modules.", moduleCount);
    }

    private void registerKeybindings() {
        try {
            ZOOM_KEY = new KeyMapping(
                "key.ballsclient.zoom",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                KEY_CATEGORY
            );

            FULLBRIGHT_KEY = new KeyMapping(
                "key.ballsclient.fullbright",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                KEY_CATEGORY
            );

            MENU_KEY = new KeyMapping(
                "key.ballsclient.menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KEY_CATEGORY
            );

            SORT_KEY = new KeyMapping(
                "key.ballsclient.sort",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY
            );

            registerSingleKey(ZOOM_KEY);
            registerSingleKey(FULLBRIGHT_KEY);
            registerSingleKey(MENU_KEY);
            registerSingleKey(SORT_KEY);
            BallsClientCore.LOGGER.info("Registered BallsClient 26.3 key mappings successfully.");
        } catch (Throwable t) {
            BallsClientCore.LOGGER.warn("Error creating key mappings: " + t.getMessage(), t);
        }
    }

    private static void registerSingleKey(KeyMapping mapping) {
        if (mapping == null) return;

        // Method 1: Modern Fabric API 26.3 (KeyMappingHelper.registerKeyMapping)
        try {
            Class<?> kmh = Class.forName("net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper");
            Method m = kmh.getMethod("registerKeyMapping", KeyMapping.class);
            m.invoke(null, mapping);
            return;
        } catch (Throwable ignored) {}

        // Method 2: Legacy Fabric API (KeyBindingHelper)
        try {
            Class<?> kbh = Class.forName("net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper");
            for (Method m : kbh.getMethods()) {
                if (m.getName().equals("registerKeyBinding") && m.getParameterCount() == 1) {
                    m.invoke(null, mapping);
                    return;
                }
            }
        } catch (Throwable ignored) {}

        // Method 3: Direct Options.keyMappings reflection append
        try {
            Minecraft client = Minecraft.getInstance();
            if (client != null && client.options != null) {
                for (Field f : client.options.getClass().getDeclaredFields()) {
                    if (KeyMapping[].class.isAssignableFrom(f.getType())) {
                        f.setAccessible(true);
                        KeyMapping[] oldArr = (KeyMapping[]) f.get(client.options);
                        if (oldArr != null) {
                            KeyMapping[] newArr = Arrays.copyOf(oldArr, oldArr.length + 1);
                            newArr[oldArr.length] = mapping;
                            f.set(client.options, newArr);
                            return;
                        }
                    }
                }
            }
        } catch (Throwable ignored) {}
    }

    private void registerTickEvents() {
        try {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                try {
                    if (client == null || client.player == null) {
                        return;
                    }

                    boolean forwardPressed = false;
                    if (client.options != null && client.options.keyUp != null) {
                        forwardPressed = client.options.keyUp.isDown();
                    }
                    if (BallsClientCore.getAutoSprintController() != null) {
                        BallsClientCore.getAutoSprintController().onTick(forwardPressed);
                    }
                    if (BallsClientCore.getMacroEngine() != null) {
                        BallsClientCore.getMacroEngine().onTick();
                    }
                } catch (Throwable ignored) {}
            });
        } catch (Throwable t) {
            BallsClientCore.LOGGER.warn("Could not register ClientTickEvents: " + t.getMessage());
        }
    }
}
