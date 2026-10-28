package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * SculkSensorFrequencyHud — Identifies vibration resonance frequency tiers (1-15) for calibrated sculk sensors.
 */
public class SculkSensorFrequencyHud extends Module {
    private final BooleanSetting showEventName = new BooleanSetting("ShowEventName", "Displays English event name next to frequency tier number", true);

    public SculkSensorFrequencyHud() {
        super("sculk-sensor-frequency-hud", "Sculk Sensor Frequency HUD", "Identifies vibration resonance frequency tiers (1-15) for calibrated sculk sensors", Category.AUTOMATION);
        addSettings(showEventName);
    }

    public BooleanSetting getShowEventName() {
        return showEventName;
    }

    public static String getEventForFrequency(int frequency) {
        return switch (frequency) {
            case 1 -> "Movement (Step/Walk)";
            case 2 -> "Flap / Swimming";
            case 3 -> "Projectile Shoot";
            case 4 -> "Hit / Damage";
            case 5 -> "Entity Interacted";
            case 6 -> "Item Used / Drank";
            case 7 -> "Block Change / Piston";
            case 8 -> "Block Destroyed";
            case 9 -> "Block Placed";
            case 10 -> "Entity Place / Dispenser";
            case 11 -> "Teleport / Consume";
            case 12 -> "Block Open / Close";
            case 13 -> "Shear / Fluid Pick";
            case 14 -> "Explosion / Lightning";
            case 15 -> "Entity Die / Roar";
            default -> "Unknown Frequency";
        };
    }
}
