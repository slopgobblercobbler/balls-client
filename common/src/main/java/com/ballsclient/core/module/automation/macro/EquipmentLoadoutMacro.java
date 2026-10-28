package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

import java.util.List;

/**
 * EquipmentLoadoutMacro — Single-key macro executing atomic armor equip, weapon swap, and offhand shield layout.
 */
public class EquipmentLoadoutMacro extends Module {
    public record LoadoutPlan(String helmet, String chestplate, String leggings, String boots, String mainHand, String offHand) {}

    private final ModeSetting loadoutType = new ModeSetting("LoadoutType", "Active equipment configuration preset", "Combat", "Combat", "Mining", "Building", "ElytraFlight");

    public EquipmentLoadoutMacro() {
        super("equipment-loadout-macro", "Equipment Loadout Macro", "Single-key macro executing atomic armor equip, weapon swap, and offhand shield layout", Category.AUTOMATION);
        addSettings(loadoutType);
    }

    public ModeSetting getLoadoutType() {
        return loadoutType;
    }

    public LoadoutPlan getPlanForCurrentMode() {
        String mode = loadoutType.getValue();
        return switch (mode) {
            case "Mining" -> new LoadoutPlan("minecraft:netherite_helmet", "minecraft:netherite_chestplate", "minecraft:netherite_leggings", "minecraft:netherite_boots", "minecraft:netherite_pickaxe", "minecraft:torch");
            case "Building" -> new LoadoutPlan("minecraft:diamond_helmet", "minecraft:diamond_chestplate", "minecraft:diamond_leggings", "minecraft:diamond_boots", "minecraft:scaffolding", "minecraft:firework_rocket");
            case "ElytraFlight" -> new LoadoutPlan("minecraft:netherite_helmet", "minecraft:elytra", "minecraft:netherite_leggings", "minecraft:netherite_boots", "minecraft:firework_rocket", "minecraft:totem_of_undying");
            default -> new LoadoutPlan("minecraft:netherite_helmet", "minecraft:netherite_chestplate", "minecraft:netherite_leggings", "minecraft:netherite_boots", "minecraft:netherite_sword", "minecraft:shield");
        };
    }
}
