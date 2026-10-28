package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Anti Friend Attack — Cancels outgoing attack packets directed at players registered in friends list
 */
public class AntiFriendAttack extends Module {
    private final BooleanSetting cancelMelee = new BooleanSetting("CancelMelee", "Cancel melee strikes on friends", true);
    private final BooleanSetting cancelProjectiles = new BooleanSetting("CancelProjectiles", "Cancel bow/crossbow/trident shots aimed at friends", true);

    public AntiFriendAttack() {
        super("anti-friend-attack", "Anti Friend Attack", "Cancels outgoing attack packets directed at players registered in friends list", Category.COMBAT);
        addSettings(cancelMelee, cancelProjectiles);
    }

    public boolean isCancelMelee() {
        return cancelMelee.getValue();
    }

    public boolean isCancelProjectiles() {
        return cancelProjectiles.getValue();
    }

    public boolean shouldCancelAttack(boolean isFriend, boolean cancelMelee, boolean isMelee) {
        return isFriend && (isMelee ? cancelMelee : true);
    }
}
