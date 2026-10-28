package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoMount — Automatically mounts rideable entities (horses, boats, minecarts) in range.
 */
public class AutoMount extends Module {
    private final BooleanSetting horses = new BooleanSetting("Horses", "Mount horses, donkeys, mules, and llamas", true);
    private final BooleanSetting boats = new BooleanSetting("Boats", "Mount nearby empty boats", true);
    private final BooleanSetting minecarts = new BooleanSetting("Minecarts", "Mount nearby minecarts", true);
    private final NumberSetting maxDistance = new NumberSetting("MaxDistance", "Maximum interaction reach to mount", 3.5, 1.0, 5.0, 0.5);

    public AutoMount() {
        super("auto-mount", "Auto Mount", "Automatically mounts rideable entities (horses, boats, minecarts) in range", Category.PLAYER);
        addSettings(horses, boats, minecarts, maxDistance);
    }

    public BooleanSetting getHorses() {
        return horses;
    }

    public BooleanSetting getBoats() {
        return boats;
    }

    public BooleanSetting getMinecarts() {
        return minecarts;
    }

    public NumberSetting getMaxDistance() {
        return maxDistance;
    }

    public static boolean canMountEntity(String entityType, double distance, double maxDist, boolean allowHorses, boolean allowBoats, boolean allowMinecarts) {
        if (distance > maxDist || entityType == null) return false;
        String typeLower = entityType.toLowerCase();
        if (allowHorses && (typeLower.contains("horse") || typeLower.contains("donkey") || typeLower.contains("mule") || typeLower.contains("llama") || typeLower.contains("pig") || typeLower.contains("strider"))) {
            return true;
        }
        if (allowBoats && (typeLower.contains("boat") || typeLower.contains("raft"))) {
            return true;
        }
        if (allowMinecarts && typeLower.contains("minecart")) {
            return true;
        }
        return false;
    }
}
