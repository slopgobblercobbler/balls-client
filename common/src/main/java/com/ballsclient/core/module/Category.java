package com.ballsclient.core.module;

/**
 * Functional domains for client modules.
 */
public enum Category {
    RENDER("Render", "Visual overlays, ESPs, HUDs, shaders, and camera utilities"),
    MOVEMENT("Movement", "Locomotion, physics, elytra, and traversal utilities"),
    COMBAT("Combat", "Tactical, targeting, weapon, and defense assistants"),
    PLAYER("Player", "World interaction, automated mining, farming, and block placement"),
    INVENTORY("Inventory", "Storage management, sorting, shulker viewing, and container transfers"),
    AUTOMATION("Automation", "Schematic holograms, macro chording, and scripted sequences"),
    NETWORK("Network", "Packet profiling, latency telemetry, and TPS monitoring");

    private final String displayName;
    private final String description;

    Category(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
