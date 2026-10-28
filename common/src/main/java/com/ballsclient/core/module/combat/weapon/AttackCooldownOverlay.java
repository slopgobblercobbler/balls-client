package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Attack Cooldown Overlay — Custom crosshair graphic rendering circular or bar attack cooldown recovery
 */
public class AttackCooldownOverlay extends Module {
    private final ModeSetting overlayStyle = new ModeSetting("OverlayStyle", "Visual geometry of the cooldown indicator", "Circle", "Circle", "HorizontalBar", "VerticalBar");
    private final NumberSetting overlayRadius = new NumberSetting("OverlayRadius", "Radius of the circular cooldown HUD ring", 16.0, 8.0, 32.0, 1.0);

    public AttackCooldownOverlay() {
        super("attack-cooldown-overlay", "Attack Cooldown Overlay", "Custom crosshair graphic rendering circular or bar attack cooldown recovery", Category.COMBAT);
        addSettings(overlayStyle, overlayRadius);
    }

    public String getOverlayStyle() {
        return overlayStyle.getValue();
    }

    public double getOverlayRadius() {
        return overlayRadius.getValue();
    }

    public boolean isReadyToAttack(double charge) {
        return charge >= 0.99;
    }
}
