package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * SafeContainerOpen — Checks hostile mob proximity and danger fields before opening container GUI.
 */
public class SafeContainerOpen extends Module {
    private final NumberSetting hostileRadius = new NumberSetting("HostileRadius", "Hostile danger scan perimeter in blocks", 5.0, 2.0, 16.0, 1.0);
    private final BooleanSetting checkCreeperFuse = new BooleanSetting("CheckCreeperFuse", "Block container open if ignited creeper in range", true);
    private final BooleanSetting notifyDanger = new BooleanSetting("NotifyDanger", "Display safety alert when container interaction cancelled", true);

    public SafeContainerOpen() {
        super("safe-container-open", "Safe Container Open", "Checks hostile mob proximity and danger fields before opening container GUI", Category.PLAYER);
        addSettings(hostileRadius, checkCreeperFuse, notifyDanger);
    }

    public NumberSetting getHostileRadius() {
        return hostileRadius;
    }

    public BooleanSetting getCheckCreeperFuse() {
        return checkCreeperFuse;
    }

    public BooleanSetting getNotifyDanger() {
        return notifyDanger;
    }

    public static boolean isContainerOpenSafe(double nearestHostileDistance, boolean creeperIgnited, double safetyRadius) {
        if (creeperIgnited) return false;
        return nearestHostileDistance > safetyRadius;
    }
}
