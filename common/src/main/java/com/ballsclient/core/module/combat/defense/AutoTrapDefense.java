package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Auto Trap Defense — Detects piston-push, anvil, or lava trap placement and deploys counter-blocks
 */
public class AutoTrapDefense extends Module {
    private final BooleanSetting antiPiston = new BooleanSetting("AntiPiston", "Place block in front of extending piston", true);
    private final BooleanSetting antiAnvil = new BooleanSetting("AntiAnvil", "Step aside if falling anvil detected above", true);

    public AutoTrapDefense() {
        super("auto-trap-defense", "Auto Trap Defense", "Detects piston-push, anvil, or lava trap placement and deploys counter-blocks", Category.COMBAT);
        addSettings(antiPiston, antiAnvil);
    }

    public boolean isAntiPiston() {
        return antiPiston.getValue();
    }

    public boolean isAntiAnvil() {
        return antiAnvil.getValue();
    }

    public boolean isAnvilFallingAbove(double anvilX, double anvilY, double anvilZ, double playerX, double playerY, double playerZ) {
        return Math.abs(anvilX - playerX) < 0.6 && Math.abs(anvilZ - playerZ) < 0.6 && anvilY > playerY;
    }
}
