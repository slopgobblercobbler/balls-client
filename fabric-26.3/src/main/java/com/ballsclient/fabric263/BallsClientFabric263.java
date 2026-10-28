package com.ballsclient.fabric263;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import com.ballsclient.core.BallsClientCore;
import com.ballsclient.fabric263.gui.BallsClientConfigScreen;
import com.ballsclient.fabric263.gui.ScreenHelper;
import com.clientmod.core.adapter.PlatformAdapterRegistry;
import com.clientmod.fabric263.adapter.Fabric263Adapter;

import java.lang.reflect.Constructor;
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

    private static boolean menuKeyPressedLastTick = false;

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

        // 3. Register Keybindings (defensively via reflection to support both 26.3 Category and legacy String)
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
            ZOOM_KEY = createKey("key.ballsclient.zoom", GLFW.GLFW_KEY_C, KEY_CATEGORY);
            FULLBRIGHT_KEY = createKey("key.ballsclient.fullbright", GLFW.GLFW_KEY_B, KEY_CATEGORY);
            MENU_KEY = createKey("key.ballsclient.menu", GLFW.GLFW_KEY_RIGHT_SHIFT, KEY_CATEGORY);
            SORT_KEY = createKey("key.ballsclient.sort", GLFW.GLFW_KEY_R, KEY_CATEGORY);

            registerSingleKey(ZOOM_KEY);
            registerSingleKey(FULLBRIGHT_KEY);
            registerSingleKey(MENU_KEY);
            registerSingleKey(SORT_KEY);
            BallsClientCore.LOGGER.info("Registered BallsClient 26.3 key mappings successfully.");
        } catch (Throwable t) {
            BallsClientCore.LOGGER.warn("Error creating key mappings: " + t.getMessage(), t);
        }
    }

    private static KeyMapping createKey(String name, int keyCode, String categoryKey) {
        // Method A: Modern 26.3 with KeyMapping.Category
        try {
            Class<?> catClass = Class.forName("net.minecraft.client.KeyMapping$Category");
            Object cat = null;
            try {
                Field f = catClass.getField("MISC");
                cat = f.get(null);
            } catch (Throwable t) {
                Method reg = catClass.getMethod("register", String.class);
                cat = reg.invoke(null, categoryKey);
            }
            if (cat != null) {
                for (Constructor<?> ctor : KeyMapping.class.getConstructors()) {
                    Class<?>[] pt = ctor.getParameterTypes();
                    if (pt.length == 3 && pt[0] == String.class && pt[1] == int.class && catClass.isAssignableFrom(pt[2])) {
                        return (KeyMapping) ctor.newInstance(name, keyCode, cat);
                    }
                }
            }
        } catch (Throwable ignored) {}

        // Method B: Legacy 1.20 / 1.21 with InputConstants.Type.KEYSYM / KEYBOARD
        try {
            Class<?> typeClass = Class.forName("com.mojang.blaze3d.platform.InputConstants$Type");
            Object keysym = null;
            try {
                Field f = typeClass.getField("KEYSYM");
                keysym = f.get(null);
            } catch (Throwable t) {
                Field f = typeClass.getField("KEYBOARD");
                keysym = f.get(null);
            }
            if (keysym != null) {
                for (Constructor<?> ctor : KeyMapping.class.getConstructors()) {
                    Class<?>[] pt = ctor.getParameterTypes();
                    if (pt.length == 4 && pt[0] == String.class && pt[1] == typeClass && pt[2] == int.class && pt[3] == String.class) {
                        return (KeyMapping) ctor.newInstance(name, keysym, keyCode, categoryKey);
                    }
                }
            }
        } catch (Throwable ignored) {}

        // Method C: 3-arg fallback (String, int, String)
        try {
            for (Constructor<?> ctor : KeyMapping.class.getConstructors()) {
                Class<?>[] pt = ctor.getParameterTypes();
                if (pt.length == 3 && pt[0] == String.class && pt[1] == int.class && pt[2] == String.class) {
                    return (KeyMapping) ctor.newInstance(name, keyCode, categoryKey);
                }
            }
        } catch (Throwable ignored) {}

        return null;
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

    private static boolean isRightShiftDown(Minecraft client) {
        // 1. Modern 26.3 InputConstants.isKeyDown(int) with SDL KEY_RSHIFT (229)
        try {
            Method m = InputConstants.class.getMethod("isKeyDown", int.class);
            Field f = InputConstants.class.getField("KEY_RSHIFT");
            int rshift = f.getInt(null);
            if ((boolean) m.invoke(null, rshift)) {
                return true;
            }
        } catch (Throwable ignored) {}

        // 2. Legacy InputConstants.isKeyDown(long, int) with GLFW
        try {
            if (client != null && client.getWindow() != null) {
                long window = client.getWindow().getWindow();
                if (window != 0) {
                    Method m = InputConstants.class.getMethod("isKeyDown", long.class, int.class);
                    if ((boolean) m.invoke(null, window, GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                        return true;
                    }
                }
            }
        } catch (Throwable ignored) {}

        // 3. Pure GLFW fallback
        try {
            if (client != null && client.getWindow() != null) {
                long window = client.getWindow().getWindow();
                if (window != 0) {
                    if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS) {
                        return true;
                    }
                }
            }
        } catch (Throwable ignored) {}

        return false;
    }

    private void registerTickEvents() {
        try {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                try {
                    if (client == null) {
                        return;
                    }

                    // Check Right Shift / MENU_KEY
                    boolean rshiftPhysical = isRightShiftDown(client);
                    boolean menuClicked = (MENU_KEY != null && MENU_KEY.consumeClick());

                    if ((rshiftPhysical && !menuKeyPressedLastTick) || menuClicked) {
                        Screen currentScreen = ScreenHelper.getCurrentScreen(client);
                        if (currentScreen == null) {
                            ScreenHelper.setScreen(client, new BallsClientConfigScreen(null));
                        } else if (currentScreen instanceof BallsClientConfigScreen) {
                            ScreenHelper.setScreen(client, null);
                        }
                    }
                    menuKeyPressedLastTick = rshiftPhysical;

                    if (client.player == null) {
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
