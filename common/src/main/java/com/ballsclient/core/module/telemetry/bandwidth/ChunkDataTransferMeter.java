package com.ballsclient.core.module.telemetry.bandwidth;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ChunkDataTransferMeter — Isolates and profiles bandwidth consumption specifically consumed
 * by chunk batch data packets and block updates.
 */
public class ChunkDataTransferMeter extends Module {
    private final BooleanSetting includeLightData = new BooleanSetting("IncludeLightData", "Includes lighting update packets in chunk bandwidth calculation", true);
    private final BooleanSetting showPercentageOfTotal = new BooleanSetting("ShowPercentageOfTotal", "Displays chunk data fraction relative to all inbound traffic", true);
    private final NumberSetting alertLargeChunkKb = new NumberSetting("AlertLargeChunkKb", "Threshold size for individual chunk packets triggering warning in KB", 256.0, 64.0, 1024.0, 64.0);

    public ChunkDataTransferMeter() {
        super("chunk-data-transfer-meter", "Chunk Data Transfer Meter", "Isolates and profiles bandwidth consumption specifically consumed by chunk batch data packets and block updates.", Category.NETWORK);
        addSettings(includeLightData, showPercentageOfTotal, alertLargeChunkKb);
    }

    public BooleanSetting getIncludeLightData() {
        return includeLightData;
    }

    public BooleanSetting getShowPercentageOfTotal() {
        return showPercentageOfTotal;
    }

    public NumberSetting getAlertLargeChunkKb() {
        return alertLargeChunkKb;
    }
}
