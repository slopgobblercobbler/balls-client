package com.ballsclient.core.module.telemetry.diagnostics;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * DisconnectReasonLogger — Intercepts and archives exact server disconnect and kick text,
 * parsing formatted chat components and error traces.
 */
public class DisconnectReasonLogger extends Module {
    private final BooleanSetting saveToDisk = new BooleanSetting("SaveToDisk", "Appends disconnect records to local disk file", true);
    private final BooleanSetting autoCopyToClipboard = new BooleanSetting("AutoCopyToClipboard", "Automatically copies kick message to system clipboard", false);
    private final BooleanSetting sanitizeFormatting = new BooleanSetting("SanitizeFormatting", "Strips Minecraft legacy color formatting codes from log", true);

    public DisconnectReasonLogger() {
        super("disconnect-reason-logger", "Disconnect Reason Logger", "Intercepts and archives exact server disconnect and kick text, parsing formatted chat components and error traces.", Category.NETWORK);
        addSettings(saveToDisk, autoCopyToClipboard, sanitizeFormatting);
    }

    public BooleanSetting getSaveToDisk() {
        return saveToDisk;
    }

    public BooleanSetting getAutoCopyToClipboard() {
        return autoCopyToClipboard;
    }

    public BooleanSetting getSanitizeFormatting() {
        return sanitizeFormatting;
    }
}
