package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * SignTextAutoPaste — Automatically populates newly placed signs from preconfigured templates.
 */
public class SignTextAutoPaste extends Module {
    private final ModeSetting preset = new ModeSetting("Preset", "Sign text preset configuration", "ShopBuy", "Custom", "ShopBuy", "ShopSell", "Warning");
    private final BooleanSetting skipDialog = new BooleanSetting("SkipDialog", "Instantly close sign editor GUI without prompt", true);

    public SignTextAutoPaste() {
        super("sign-text-auto-paste", "Sign Text Auto Paste", "Automatically populates newly placed signs from preconfigured templates", Category.PLAYER);
        addSettings(preset, skipDialog);
    }

    public ModeSetting getPreset() {
        return preset;
    }

    public BooleanSetting getSkipDialog() {
        return skipDialog;
    }

    public static String[] getSignLines(String preset, String line1, String line2, String line3, String line4) {
        if ("ShopBuy".equalsIgnoreCase(preset)) {
            return new String[]{"[BUY]", "Diamond", "1 for $100", "Right Click"};
        }
        if ("ShopSell".equalsIgnoreCase(preset)) {
            return new String[]{"[SELL]", "Diamond", "1 for $50", "Left Click"};
        }
        if ("Warning".equalsIgnoreCase(preset)) {
            return new String[]{"!!! DANGER !!!", "Private Land", "Trespassers", "Will Be Shot"};
        }
        return new String[]{
                line1 != null ? line1 : "",
                line2 != null ? line2 : "",
                line3 != null ? line3 : "",
                line4 != null ? line4 : ""
        };
    }
}
