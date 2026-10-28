package com.ballsclient.core.module.telemetry.diagnostics;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * ServerBrandIdentifier — Intercepts custom brand payload packets to identify and display
 * backend server software (Paper, Purpur, Velocity, Fabric, Vanilla).
 */
public class ServerBrandIdentifier extends Module {
    private final BooleanSetting showInF3 = new BooleanSetting("ShowInF3", "Injects server brand info into Minecraft F3 debug screen", true);
    private final BooleanSetting showInTab = new BooleanSetting("ShowInTab", "Displays server brand tag in player list tab overlay", false);
    private final BooleanSetting logBrandChanges = new BooleanSetting("LogBrandChanges", "Logs proxy brand changes across server network hops", true);

    public ServerBrandIdentifier() {
        super("server-brand-identifier", "Server Brand Identifier", "Intercepts custom brand payload packets to identify and display backend server software (Paper, Purpur, Velocity, Fabric, Vanilla).", Category.NETWORK);
        addSettings(showInF3, showInTab, logBrandChanges);
    }

    public BooleanSetting getShowInF3() {
        return showInF3;
    }

    public BooleanSetting getShowInTab() {
        return showInTab;
    }

    public BooleanSetting getLogBrandChanges() {
        return logBrandChanges;
    }
}
