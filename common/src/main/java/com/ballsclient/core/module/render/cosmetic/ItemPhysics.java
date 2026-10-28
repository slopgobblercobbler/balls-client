package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Realistic 3D rotational physics for dropped items, laying flat on surfaces and tumbling on collision.
 */
public class ItemPhysics extends Module {
    private final NumberSetting rotationSpeed = new NumberSetting("RotationSpeed", "Item tumble rotational velocity multiplier", 1.5, 0.1, 5.0, 0.1);
    private final BooleanSetting flatLay = new BooleanSetting("FlatLay", "Items lay completely flat on horizontal block surfaces", true);

    public ItemPhysics() {
        super("item-physics", "Item Physics", "3D physics for dropped ground items with surface resting and tumbling", Category.RENDER);
        addSettings(rotationSpeed, flatLay);
    }

    public double getRotationSpeed() { return rotationSpeed.getValue(); }
    public boolean isFlatLay() { return flatLay.getValue(); }
}
