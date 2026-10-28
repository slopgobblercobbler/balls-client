package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * DeathCoordinateLogger — Logs 3D coordinates, dimension, timestamp, and inventory state on player demise.
 */
public class DeathCoordinateLogger extends Module {
    private final BooleanSetting chatAlert = new BooleanSetting("ChatAlert", "Print death position into client chat", true);
    private final BooleanSetting saveToFile = new BooleanSetting("SaveToFile", "Append death entry to client death log file", true);
    private final BooleanSetting displayDimension = new BooleanSetting("DisplayDimension", "Include dimension name in logged coordinates", true);

    public DeathCoordinateLogger() {
        super("death-coordinate-logger", "Death Coordinate Logger", "Logs 3D coordinates, dimension, timestamp, and inventory state on player demise", Category.PLAYER);
        addSettings(chatAlert, saveToFile, displayDimension);
    }

    public BooleanSetting getChatAlert() {
        return chatAlert;
    }

    public BooleanSetting getSaveToFile() {
        return saveToFile;
    }

    public BooleanSetting getDisplayDimension() {
        return displayDimension;
    }

    public static String formatDeathMessage(String player, double x, double y, double z, String dimension) {
        return String.format("[%s] Died at X: %.1f, Y: %.1f, Z: %.1f in %s", player, x, y, z, dimension);
    }
}
