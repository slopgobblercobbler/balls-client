package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Restores classic Minecraft 1.7 / 1.8 sword block-hit and punch motions on modern versions.
 */
public class OldCombatAnimations extends Module {
    private final BooleanSetting blockHitAnimation = new BooleanSetting("BlockHit", "Restore classic 1.7 sword block-hit animation", true);
    private final BooleanSetting punchAnimation = new BooleanSetting("PunchMotion", "Restore legacy fast item swinging punch motion", true);

    public OldCombatAnimations() {
        super("old-combat-animations", "Old Combat Animations", "Restores classic 1.7/1.8 sword blocking and punch animation dynamics", Category.RENDER);
        addSettings(blockHitAnimation, punchAnimation);
    }

    public boolean isBlockHitAnimation() { return blockHitAnimation.getValue(); }
    public boolean isPunchAnimation() { return punchAnimation.getValue(); }
}
