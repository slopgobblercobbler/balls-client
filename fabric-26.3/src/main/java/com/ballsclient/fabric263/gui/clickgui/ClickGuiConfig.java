package com.ballsclient.fabric263.gui.clickgui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Persistence manager for ClickGUI category panel coordinates and states.
 * Stores configuration in config/ballsclient/clickgui.json.
 */
public final class ClickGuiConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, PanelState> PANEL_STATES = new HashMap<>();
    private static boolean loaded = false;

    public static class PanelState {
        public int x;
        public int y;
        public boolean collapsed;

        public PanelState(int x, int y, boolean collapsed) {
            this.x = x;
            this.y = y;
            this.collapsed = collapsed;
        }
    }

    private ClickGuiConfig() {}

    private static Path getConfigFile() {
        try {
            return FabricLoader.getInstance().getConfigDir().resolve("ballsclient").resolve("clickgui.json");
        } catch (Throwable ignored) {
            return Path.of("config", "ballsclient", "clickgui.json");
        }
    }

    public static synchronized void load() {
        PANEL_STATES.clear();
        Path path = getConfigFile();
        File file = path.toFile();

        if (file.exists() && file.isFile()) {
            try (FileReader reader = new FileReader(file)) {
                JsonElement root = JsonParser.parseReader(reader);
                if (root != null && root.isJsonObject()) {
                    JsonObject obj = root.getAsJsonObject();
                    if (obj.has("panels") && obj.get("panels").isJsonObject()) {
                        JsonObject panels = obj.getAsJsonObject("panels");
                        for (Map.Entry<String, JsonElement> entry : panels.entrySet()) {
                            if (entry.getValue().isJsonObject()) {
                                JsonObject p = entry.getValue().getAsJsonObject();
                                int x = p.has("x") ? p.get("x").getAsInt() : 20;
                                int y = p.has("y") ? p.get("y").getAsInt() : 30;
                                boolean collapsed = p.has("collapsed") && p.get("collapsed").getAsBoolean();
                                PANEL_STATES.put(entry.getKey(), new PanelState(x, y, collapsed));
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                System.err.println("[BallsClient] Failed to load clickgui.json: " + t.getMessage());
            }
        }
        loaded = true;
    }

    public static synchronized void save() {
        Path path = getConfigFile();
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            JsonObject root = new JsonObject();
            JsonObject panelsObj = new JsonObject();

            for (Map.Entry<String, PanelState> entry : PANEL_STATES.entrySet()) {
                JsonObject p = new JsonObject();
                p.addProperty("x", entry.getValue().x);
                p.addProperty("y", entry.getValue().y);
                p.addProperty("collapsed", entry.getValue().collapsed);
                panelsObj.add(entry.getKey(), p);
            }

            root.add("panels", panelsObj);

            try (FileWriter writer = new FileWriter(path.toFile())) {
                GSON.toJson(root, writer);
            }
        } catch (Throwable t) {
            System.err.println("[BallsClient] Failed to save clickgui.json: " + t.getMessage());
        }
    }

    public static synchronized PanelState getPanelState(String categoryName) {
        if (!loaded) {
            load();
        }
        return PANEL_STATES.get(categoryName);
    }

    public static synchronized void setPanelState(String categoryName, int x, int y, boolean collapsed) {
        PANEL_STATES.put(categoryName, new PanelState(x, y, collapsed));
    }

    public static synchronized Map<String, PanelState> getAllStates() {
        if (!loaded) {
            load();
        }
        return new HashMap<>(PANEL_STATES);
    }
}
