package com.ballsclient.fabric263.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ScreenHelper {
    private ScreenHelper() {}

    public static Screen getCurrentScreen(Minecraft client) {
        if (client == null) return null;

        // 1. Modern 26.3: client.gui.screen() via reflection
        try {
            Field guiField = client.getClass().getField("gui");
            Object gui = guiField.get(client);
            if (gui != null) {
                Method m = gui.getClass().getMethod("screen");
                return (Screen) m.invoke(gui);
            }
        } catch (Throwable ignored) {}

        // 2. Legacy / 1.20 / 1.21: client.screen
        try {
            for (Field f : client.getClass().getFields()) {
                if (Screen.class.isAssignableFrom(f.getType())) {
                    return (Screen) f.get(client);
                }
            }
        } catch (Throwable ignored) {}

        try {
            for (Field f : client.getClass().getDeclaredFields()) {
                if (Screen.class.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    return (Screen) f.get(client);
                }
            }
        } catch (Throwable ignored) {}

        return null;
    }

    public static void setScreen(Minecraft client, Screen screen) {
        if (client == null) return;

        // 1. Modern 26.3: client.setScreenAndShow(screen)
        try {
            Method m = client.getClass().getMethod("setScreenAndShow", Screen.class);
            m.invoke(client, screen);
            return;
        } catch (Throwable ignored) {}

        // 2. Modern 26.3: client.gui.setScreen(screen)
        try {
            Field guiField = client.getClass().getField("gui");
            Object gui = guiField.get(client);
            if (gui != null) {
                Method m = gui.getClass().getMethod("setScreen", Screen.class);
                m.invoke(gui, screen);
                return;
            }
        } catch (Throwable ignored) {}

        // 3. Legacy 1.20/1.21: client.setScreen(screen)
        try {
            for (Method m : client.getClass().getMethods()) {
                if (m.getName().equals("setScreen") && m.getParameterCount() == 1 && Screen.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    m.invoke(client, screen);
                    return;
                }
            }
        } catch (Throwable ignored) {}
    }
}
