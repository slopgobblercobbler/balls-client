package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Displays equipped armor pieces and held weapons with real-time durability status.
 */
public class ArmorDurabilityHUD extends Module {
    private final NumberSetting warningThreshold = new NumberSetting("WarningThreshold", "Durability percentage threshold for critical warning", 20.0, 5.0, 50.0, 5.0);
    private final BooleanSetting showPercent = new BooleanSetting("ShowPercent", "Display percentage instead of raw damage uses", true);
    private final ColorSetting barColor = new ColorSetting("BarColor", "Base color for full durability bar", 0xFF00FF00);

    public static class ArmorSlotState {
        public String itemName;
        public int currentDamage;
        public int maxDamage;

        public ArmorSlotState(String itemName, int currentDamage, int maxDamage) {
            this.itemName = itemName;
            this.currentDamage = currentDamage;
            this.maxDamage = Math.max(1, maxDamage);
        }

        public double getDurabilityPercent() {
            int remaining = Math.max(0, maxDamage - currentDamage);
            return (remaining / (double) maxDamage) * 100.0;
        }

        public boolean isLow(double threshold) {
            return getDurabilityPercent() <= threshold;
        }
    }

    private final ArmorSlotState[] equippedSlots = new ArmorSlotState[4]; // Helmet, Chest, Legs, Boots

    public ArmorDurabilityHUD() {
        super("armor-durability-hud", "Armor Durability HUD", "Displays durability bars and critical warnings for armor", Category.RENDER);
        addSettings(warningThreshold, showPercent, barColor);
    }

    public void updateSlot(int index, String itemName, int currentDamage, int maxDamage) {
        if (index >= 0 && index < equippedSlots.length) {
            equippedSlots[index] = new ArmorSlotState(itemName, currentDamage, maxDamage);
        }
    }

    public ArmorSlotState getSlot(int index) {
        if (index >= 0 && index < equippedSlots.length) {
            return equippedSlots[index];
        }
        return null;
    }

    public boolean hasCriticalArmor() {
        double threshold = warningThreshold.getValue();
        for (ArmorSlotState s : equippedSlots) {
            if (s != null && s.isLow(threshold)) {
                return true;
            }
        }
        return false;
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // HUD render pass
    }
}
