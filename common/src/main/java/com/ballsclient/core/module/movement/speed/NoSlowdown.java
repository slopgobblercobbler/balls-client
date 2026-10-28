package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Cancels slowness when eating, aiming bows, blocking, or sneaking.
 */
public class NoSlowdown extends Module {
    private final BooleanSetting items = new BooleanSetting("Items", "Cancel item usage slowness penalty (eating, bows, shields)", true);
    private final BooleanSetting sneak = new BooleanSetting("Sneak", "Cancel crouching speed penalty", false);
    private final BooleanSetting soulSand = new BooleanSetting("SoulSand", "Cancel soul sand speed reduction", true);
    private final BooleanSetting honey = new BooleanSetting("Honey", "Cancel honey block walking slowdown", true);

    public NoSlowdown() {
        super("no-slowdown", "No Slowdown", "Cancels slowness when eating, aiming bows, blocking, or sneaking", Category.MOVEMENT);
        addSettings(items, sneak, soulSand, honey);
    }

    public boolean isItems() { return items.getValue(); }
    public boolean isSneak() { return sneak.getValue(); }
    public boolean isSoulSand() { return soulSand.getValue(); }
    public boolean isHoney() { return honey.getValue(); }

    public boolean shouldCancelItemSlowdown() { return isEnabled() && isItems(); }
    public boolean shouldCancelSneakSlowdown() { return isEnabled() && isSneak(); }
}
