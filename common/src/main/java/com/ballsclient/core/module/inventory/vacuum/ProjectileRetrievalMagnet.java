package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * ProjectileRetrievalMagnet — Computes pickup trajectories for ground arrows and thrown tridents.
 */
public class ProjectileRetrievalMagnet extends Module {
    private final BooleanSetting retrieveArrows = new BooleanSetting("RetrieveArrows", "Attract stuck ground arrows", true);
    private final BooleanSetting retrieveTridents = new BooleanSetting("RetrieveTridents", "Attract thrown loyalty/ground tridents", true);

    public ProjectileRetrievalMagnet() {
        super("projectile-retrieval-magnet", "Projectile Retrieval Magnet", "Computes trajectories for ground arrows and tridents", Category.INVENTORY);
        addSettings(retrieveArrows, retrieveTridents);
    }

    public BooleanSetting getRetrieveArrows() {
        return retrieveArrows;
    }

    public BooleanSetting getRetrieveTridents() {
        return retrieveTridents;
    }

    public static boolean isRetrievalCandidate(boolean inGround, boolean isPlayerArrow) {
        return inGround && isPlayerArrow;
    }
}
