package com.ballsclient.core.module;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.event.KeyEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.setting.Setting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Dynamic registry, category query engine, keybind dispatcher, and atomic JSON persistence manager.
 */
public class ModuleManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleManager.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final EventBus eventBus;
    private final Path configPath;
    private final Map<String, Module> modules = new ConcurrentHashMap<>();
    private final Map<Category, CopyOnWriteArrayList<Module>> modulesByCategory = new EnumMap<>(Category.class);

    public ModuleManager(EventBus eventBus) {
        this(eventBus, Paths.get("config", "ballsclient", "modules.json"));
    }

    public ModuleManager(EventBus eventBus, Path configPath) {
        this.eventBus = Objects.requireNonNull(eventBus, "EventBus cannot be null");
        this.configPath = configPath != null ? configPath : Paths.get("config", "ballsclient", "modules.json");

        for (Category cat : Category.values()) {
            modulesByCategory.put(cat, new CopyOnWriteArrayList<>());
        }

        // Register keybind dispatcher listener
        this.eventBus.register(new KeybindHandler());
    }

    public void register(Module module) {
        if (module == null) {
            return;
        }
        String id = module.getId().toLowerCase(Locale.ROOT);
        if (modules.containsKey(id)) {
            LOGGER.warn("Duplicate module registration skipped: {}", id);
            return;
        }
        modules.put(id, module);
        modulesByCategory.get(module.getCategory()).add(module);
    }

    public void registerAll(Module... toRegister) {
        if (toRegister != null) {
            for (Module m : toRegister) {
                register(m);
            }
        }
    }

    public void registerAll(Collection<Module> toRegister) {
        if (toRegister != null) {
            for (Module m : toRegister) {
                register(m);
            }
        }
    }

    public Module getModule(String id) {
        if (id == null) {
            return null;
        }
        return modules.get(id.toLowerCase(Locale.ROOT));
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> moduleClass) {
        if (moduleClass == null) {
            return null;
        }
        for (Module m : modules.values()) {
            if (moduleClass.isInstance(m)) {
                return (T) m;
            }
        }
        return null;
    }

    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    public List<Module> getModulesByCategory(Category category) {
        if (category == null) {
            return Collections.emptyList();
        }
        CopyOnWriteArrayList<Module> list = modulesByCategory.get(category);
        return list != null ? Collections.unmodifiableList(list) : Collections.emptyList();
    }

    public Path getConfigPath() {
        return configPath;
    }

    /**
     * Registers all default modules across all categories.
     */
    public void registerDefaultModules() {
        try {
            Class<?> catalogClass = Class.forName("com.ballsclient.core.module.render.RenderModuleCatalog");
            catalogClass.getMethod("registerAll", ModuleManager.class).invoke(null, this);
        } catch (Exception e) {
            LOGGER.error("Failed to load RenderModuleCatalog: {}", e.getMessage(), e);
        }
    }

    /**
     * Atomically serializes all module states and settings to modules.json.
     */
    public synchronized void saveConfig() {
        if (configPath == null) {
            return;
        }
        try {
            JsonObject root = new JsonObject();
            JsonObject modulesObj = new JsonObject();

            for (Module m : modules.values()) {
                JsonObject modJson = new JsonObject();
                modJson.addProperty("enabled", m.isEnabled());

                JsonObject settingsJson = new JsonObject();
                for (Setting<?> s : m.getSettings()) {
                    settingsJson.add(s.getName(), s.toJson());
                }
                modJson.add("settings", settingsJson);

                modulesObj.add(m.getId(), modJson);
            }

            root.add("modules", modulesObj);

            Path parent = configPath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            Path tempFile = configPath.resolveSibling(configPath.getFileName() + ".tmp");
            try (Writer writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }

            try {
                Files.move(tempFile, configPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tempFile, configPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save modules configuration to {}: {}", configPath, e.getMessage(), e);
        }
    }

    /**
     * Loads module states and settings from modules.json.
     */
    public synchronized void loadConfig() {
        if (configPath == null || !Files.exists(configPath)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null || !root.has("modules")) {
                return;
            }

            JsonObject modulesObj = root.getAsJsonObject("modules");
            if (modulesObj == null) {
                return;
            }

            for (Map.Entry<String, JsonElement> entry : modulesObj.entrySet()) {
                String id = entry.getKey();
                Module m = getModule(id);
                if (m == null || !entry.getValue().isJsonObject()) {
                    continue;
                }

                JsonObject modJson = entry.getValue().getAsJsonObject();
                if (modJson.has("settings") && modJson.get("settings").isJsonObject()) {
                    JsonObject settingsJson = modJson.getAsJsonObject("settings");
                    for (Setting<?> s : m.getSettings()) {
                        if (settingsJson.has(s.getName())) {
                            try {
                                s.fromJson(settingsJson.get(s.getName()));
                            } catch (Exception e) {
                                LOGGER.warn("Failed to load setting {} in module {}: {}",
                                        s.getName(), id, e.getMessage());
                            }
                        }
                    }
                }

                if (modJson.has("enabled")) {
                    try {
                        m.setEnabled(modJson.get("enabled").getAsBoolean());
                    } catch (Exception e) {
                        LOGGER.warn("Failed to set enabled state for module {}: {}", id, e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load modules configuration from {}: {}", configPath, e.getMessage(), e);
        }
    }

    private class KeybindHandler {
        @Subscribe
        public void onKey(KeyEvent event) {
            if (event.isPress()) {
                int key = event.getKeyCode();
                for (Module m : modules.values()) {
                    if (m.getKeybind().matches(key)) {
                        m.toggle();
                    }
                }
            }
        }
    }
}
