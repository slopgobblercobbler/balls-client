package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * MobDropCollectorAura — Tracks mob death animations and immediately sweeps loot drops at death coordinate.
 */
public class MobDropCollectorAura extends Module {
    private final BooleanSetting anticipateDeathTicks = new BooleanSetting("AnticipateDeathTicks", "Pre-position suction vector during 20-tick death animation", true);

    public MobDropCollectorAura() {
        super("mob-drop-collector-aura", "Mob Drop Collector Aura", "Tracks mob death animations and immediately sweeps loot drops", Category.INVENTORY);
        addSettings(anticipateDeathTicks);
    }

    public BooleanSetting getAnticipateDeathTicks() {
        return anticipateDeathTicks;
    }

    public static boolean isDyingMob(int deathTimeTicks) {
        return deathTimeTicks > 0 && deathTimeTicks <= 20;
    }
}
