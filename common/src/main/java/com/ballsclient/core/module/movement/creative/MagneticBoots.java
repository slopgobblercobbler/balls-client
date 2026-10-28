package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Locks player to iron/metallic ceilings allowing upside-down walking.
 */
public class MagneticBoots extends Module {
    private final NumberSetting attractionForce = new NumberSetting("AttractionForce", "Inverted gravitational attraction force", 0.40, 0.1, 1.0, 0.05);
    private final BooleanSetting ceilingWalk = new BooleanSetting("CeilingWalk", "Enable inverted walking controls on ceiling", true);

    public MagneticBoots() {
        super("magnetic-boots", "Magnetic Boots", "Locks player to iron/metallic ceilings allowing upside-down walking", Category.MOVEMENT);
        addSettings(attractionForce, ceilingWalk);
    }

    public double getAttractionForce() { return attractionForce.getValue(); }
    public boolean isCeilingWalk() { return ceilingWalk.getValue(); }

    public double calculateAttraction(double distToCeiling, boolean isMetallicCeiling) {
        if (!isEnabled()) return -0.08;
        if (!isMetallicCeiling || distToCeiling > 1.5) {
            return -0.08;
        }
        return getAttractionForce();
    }
}
