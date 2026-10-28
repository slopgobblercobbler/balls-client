package com.ballsclient.core.module;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.setting.KeybindSetting;
import com.ballsclient.core.setting.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all Balls Client modular plugins.
 */
public abstract class Module {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final String id;
    private final String name;
    private final String description;
    private final Category category;
    private final KeybindSetting keybind;
    private boolean enabled;
    private final List<Setting<?>> settings = new ArrayList<>();

    public Module(String id, String name, String description, Category category) {
        this(id, name, description, category, KeybindSetting.KEY_NONE);
    }

    public Module(String id, String name, String description, Category category, int defaultKey) {
        this.id = Objects.requireNonNull(id, "Module id cannot be null").toLowerCase();
        this.name = Objects.requireNonNull(name, "Module name cannot be null");
        this.description = description != null ? description : "";
        this.category = Objects.requireNonNull(category, "Module category cannot be null");
        this.keybind = new KeybindSetting("Keybind", "Activation keybind for " + name, defaultKey);
        this.enabled = false;
        this.settings.add(this.keybind);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public KeybindSetting getKeybind() {
        return keybind;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (this.enabled) {
                try {
                    BallsClientCore.getEventBus().register(this);
                    onEnable();
                } catch (Throwable t) {
                    logger.error("Error enabling module {}: {}", name, t.getMessage(), t);
                }
            } else {
                try {
                    BallsClientCore.getEventBus().unregister(this);
                    onDisable();
                } catch (Throwable t) {
                    logger.error("Error disabling module {}: {}", name, t.getMessage(), t);
                }
            }
        }
    }

    public void toggle() {
        setEnabled(!this.enabled);
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    public void onTick() {
    }

    public void addSetting(Setting<?> setting) {
        if (setting != null && !settings.contains(setting)) {
            settings.add(setting);
        }
    }

    public void addSettings(Setting<?>... newSettings) {
        for (Setting<?> s : newSettings) {
            addSetting(s);
        }
    }

    public List<Setting<?>> getSettings() {
        return Collections.unmodifiableList(settings);
    }

    @SuppressWarnings("unchecked")
    public <T extends Setting<?>> T getSetting(String name) {
        if (name == null) {
            return null;
        }
        for (Setting<?> s : settings) {
            if (s.getName().equalsIgnoreCase(name)) {
                return (T) s;
            }
        }
        return null;
    }
}
