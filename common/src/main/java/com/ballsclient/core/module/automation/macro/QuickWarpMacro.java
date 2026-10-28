package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * QuickWarpMacro — One-touch keybind macro to execute dimension warp sequences and teleport confirmations.
 */
public class QuickWarpMacro extends Module {
    private final ModeSetting warpName = new ModeSetting("WarpName", "Target warp destination", "home", "home", "spawn", "wild", "shop");
    private final BooleanSetting autoConfirmGui = new BooleanSetting("AutoConfirmGui", "Automatically clicks confirm slot in server GUI", true);

    public QuickWarpMacro() {
        super("quick-warp-macro", "Quick Warp Macro", "One-touch keybind macro to execute dimension warp sequences and teleport confirmations", Category.AUTOMATION);
        addSettings(warpName, autoConfirmGui);
    }

    public ModeSetting getWarpName() {
        return warpName;
    }

    public BooleanSetting getAutoConfirmGui() {
        return autoConfirmGui;
    }

    public String getCommandString() {
        return "/warp " + warpName.getValue();
    }
}
