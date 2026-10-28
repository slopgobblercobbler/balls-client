package com.ballsclient.core.module.telemetry.packet;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PacketSequenceTracker — Audits sequential continuity of packet IDs and server sequence numbers
 * to detect dropped, skipped, or out-of-order packets.
 */
public class PacketSequenceTracker extends Module {
    private final NumberSetting maxDisorderGap = new NumberSetting("MaxDisorderGap", "Maximum acceptable packet sequence gap", 3.0, 1.0, 20.0, 1.0);
    private final BooleanSetting alertOnLoss = new BooleanSetting("AlertOnLoss", "Alerts when packets are dropped or missing", true);
    private final BooleanSetting trackAckOnly = new BooleanSetting("TrackAckOnly", "Restricts tracking to acknowledgment sequence packets", false);

    public PacketSequenceTracker() {
        super("packet-sequence-tracker", "Packet Sequence Tracker", "Audits sequential continuity of packet IDs and server sequence numbers to detect dropped, skipped, or out-of-order packets.", Category.NETWORK);
        addSettings(maxDisorderGap, alertOnLoss, trackAckOnly);
    }

    public NumberSetting getMaxDisorderGap() {
        return maxDisorderGap;
    }

    public BooleanSetting getAlertOnLoss() {
        return alertOnLoss;
    }

    public BooleanSetting getTrackAckOnly() {
        return trackAckOnly;
    }
}
