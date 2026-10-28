package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BedMiningHelper — Automates blast-shielded bed detonation sequences for ancient debris.
 */
public class BedMiningHelper extends Module {
    private final NumberSetting blastShieldOffset = new NumberSetting("BlastShieldOffset", "Stance offset distance behind blast shield", 1.0, 0.5, 2.0, 0.1);
    private final BooleanSetting autoPlaceObsidian = new BooleanSetting("AutoPlaceObsidian", "Automatically place blast shield block before bed", true);
    private final BooleanSetting autoSneakTrigger = new BooleanSetting("AutoSneakTrigger", "Enforce sneaking while clicking bed head", true);

    public BedMiningHelper() {
        super("bed-mining-helper", "Bed Mining Helper", "Automates blast-shielded bed detonation sequences for ancient debris", Category.PLAYER);
        addSettings(blastShieldOffset, autoPlaceObsidian, autoSneakTrigger);
    }

    public NumberSetting getBlastShieldOffset() {
        return blastShieldOffset;
    }

    public BooleanSetting getAutoPlaceObsidian() {
        return autoPlaceObsidian;
    }

    public BooleanSetting getAutoSneakTrigger() {
        return autoSneakTrigger;
    }

    public static boolean isSafeBedDetonationStance(double px, double py, double pz, double bedX, double bedY, double bedZ, boolean hasShieldBlock) {
        if (!hasShieldBlock) return false;
        double dx = px - bedX;
        double dz = pz - bedZ;
        double dist = Math.sqrt(dx * dx + dz * dz);
        // Player should stand between 3.5 and 5.0 blocks away with line of sight blocked
        return dist >= 3.0 && dist <= 5.5 && py <= bedY + 0.5;
    }
}
