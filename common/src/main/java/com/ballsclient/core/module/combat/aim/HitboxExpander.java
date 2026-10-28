package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Hitbox Expander — Client-side raycast tolerance expansion for hitting high-velocity moving targets
 */
public class HitboxExpander extends Module {
    private final NumberSetting expandSize = new NumberSetting("ExpandSize", "Bounding box expansion margin in blocks", 0.15, 0.01, 0.4, 0.01);
    private final BooleanSetting onlyPlayers = new BooleanSetting("OnlyPlayers", "Expand only for player entities", true);

    public HitboxExpander() {
        super("hitbox-expander", "Hitbox Expander", "Client-side raycast tolerance expansion for hitting high-velocity moving targets", Category.COMBAT);
        addSettings(expandSize, onlyPlayers);
    }

    public double getExpandSize() {
        return expandSize.getValue();
    }

    public boolean isOnlyPlayers() {
        return onlyPlayers.getValue();
    }

    public boolean intersectsExpandedBox(double rayOrigX, double rayOrigY, double rayOrigZ, double rayDirX, double rayDirY, double rayDirZ, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, double exp) {
        double expMinX = minX - exp, expMaxX = maxX + exp;
        double expMinY = minY - exp, expMaxY = maxY + exp;
        double expMinZ = minZ - exp, expMaxZ = maxZ + exp;
        double t1 = (expMinX - rayOrigX) / (rayDirX == 0 ? 1e-9 : rayDirX);
        double t2 = (expMaxX - rayOrigX) / (rayDirX == 0 ? 1e-9 : rayDirX);
        double tmin = Math.min(t1, t2), tmax = Math.max(t1, t2);
        return tmax >= Math.max(0.0, tmin);
    }
}
