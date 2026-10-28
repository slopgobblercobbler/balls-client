package com.clientmod.core.config;

/**
 * Central configuration model for ClientMod.
 * Pure Java POJO serialized/deserialized via Gson.
 */
public class ModConfig {
    // Master toggles
    public boolean enabled = true;

    // HUD & Visuals
    public boolean coordinatesHud = true;
    public boolean durabilityHud = true;
    public boolean statusEffectsHud = true;
    public boolean radarHud = true;
    public boolean fullbright = false;
    public double zoomFactor = 0.25;

    // Inventory
    public boolean autoSort = true;
    public String sortOrder = "ALPHABETICAL";
    public boolean shulkerPreview = true;
    public boolean quickTransfer = true;

    // Movement
    public boolean autoSprint = false;
    public boolean autoJump = false;

    // Telemetry
    public boolean showFps = true;
    public boolean showTps = true;
    public boolean showMemory = true;

    public ModConfig() {}

    public ModConfig copy() {
        ModConfig clone = new ModConfig();
        clone.enabled = this.enabled;
        clone.coordinatesHud = this.coordinatesHud;
        clone.durabilityHud = this.durabilityHud;
        clone.statusEffectsHud = this.statusEffectsHud;
        clone.radarHud = this.radarHud;
        clone.fullbright = this.fullbright;
        clone.zoomFactor = this.zoomFactor;
        clone.autoSort = this.autoSort;
        clone.sortOrder = this.sortOrder;
        clone.shulkerPreview = this.shulkerPreview;
        clone.quickTransfer = this.quickTransfer;
        clone.autoSprint = this.autoSprint;
        clone.autoJump = this.autoJump;
        clone.showFps = this.showFps;
        clone.showTps = this.showTps;
        clone.showMemory = this.showMemory;
        return clone;
    }
}
