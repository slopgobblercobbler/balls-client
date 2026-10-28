package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * TreeCapitatorHelper — Detects tree log trunks and sequences top-to-bottom / bottom-to-top felling.
 */
public class TreeCapitatorHelper extends Module {
    private final ModeSetting breakOrder = new ModeSetting("BreakOrder", "Log excavation sequence direction", "TopToBottom", "TopToBottom", "BottomToTop");
    private final NumberSetting maxLogs = new NumberSetting("MaxLogs", "Maximum log count to harvest in single tree", 128.0, 16.0, 256.0, 16.0);
    private final BooleanSetting breakLeaves = new BooleanSetting("BreakLeaves", "Break connected decaying leaves", false);

    public TreeCapitatorHelper() {
        super("tree-capitator-helper", "Tree Capitator Helper", "Detects tree log trunks and sequences top-to-bottom / bottom-to-top felling", Category.PLAYER);
        addSettings(breakOrder, maxLogs, breakLeaves);
    }

    public ModeSetting getBreakOrder() {
        return breakOrder;
    }

    public NumberSetting getMaxLogs() {
        return maxLogs;
    }

    public BooleanSetting getBreakLeaves() {
        return breakLeaves;
    }

    public static int compareLogHeight(int y1, int y2, String breakOrder) {
        if ("BottomToTop".equalsIgnoreCase(breakOrder)) {
            return Integer.compare(y1, y2);
        }
        // TopToBottom
        return Integer.compare(y2, y1);
    }
}
