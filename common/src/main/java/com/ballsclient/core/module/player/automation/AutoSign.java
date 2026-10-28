package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * AutoSign — Automatically populates newly placed signs from pre-configured text presets.
 */
public class AutoSign extends Module {
    private final BooleanSetting autoFormat = new BooleanSetting("AutoFormat", "Apply color formatting codes to sign lines", true);
    private final BooleanSetting instantClose = new BooleanSetting("InstantClose", "Skip sign editing GUI immediately", true);

    public AutoSign() {
        super("auto-sign", "Auto Sign", "Automatically populates newly placed signs from pre-configured text presets", Category.PLAYER);
        addSettings(autoFormat, instantClose);
    }

    public BooleanSetting getAutoFormat() {
        return autoFormat;
    }

    public BooleanSetting getInstantClose() {
        return instantClose;
    }

    public static String formatSignLine(String line, boolean allowFormatting) {
        if (line == null) return "";
        if (allowFormatting) {
            return line.replace('&', '\u00A7');
        }
        return line;
    }
}
