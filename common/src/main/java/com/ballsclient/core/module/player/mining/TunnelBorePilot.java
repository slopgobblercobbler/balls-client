package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * TunnelBorePilot — Position alignment and obstruction detector for slime-piston tunnel bores.
 */
public class TunnelBorePilot extends Module {
    private final NumberSetting monitoringDistance = new NumberSetting("MonitoringDistance", "Obstruction forward scanning radius in blocks", 12.0, 5.0, 30.0, 1.0);
    private final BooleanSetting detectUnexplodedTnt = new BooleanSetting("DetectUnexplodedTnt", "Warn of unexploded TNT entities ahead of bore", true);
    private final BooleanSetting detectObstructions = new BooleanSetting("DetectObstructions", "Identify immovable blast-resistant blocks", true);

    public TunnelBorePilot() {
        super("tunnel-bore-pilot", "Tunnel Bore Pilot", "Position alignment and obstruction detector for slime-piston tunnel bores", Category.PLAYER);
        addSettings(monitoringDistance, detectUnexplodedTnt, detectObstructions);
    }

    public NumberSetting getMonitoringDistance() {
        return monitoringDistance;
    }

    public BooleanSetting getDetectUnexplodedTnt() {
        return detectUnexplodedTnt;
    }

    public BooleanSetting getDetectObstructions() {
        return detectObstructions;
    }

    public static boolean isObstructionPresent(String blockId) {
        if (blockId == null) return false;
        String idLower = blockId.toLowerCase();
        return idLower.contains("obsidian") || idLower.contains("ancient_debris") || idLower.contains("bedrock") || idLower.contains("crying_obsidian");
    }
}
