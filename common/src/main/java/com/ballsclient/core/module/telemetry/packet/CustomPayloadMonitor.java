package com.ballsclient.core.module.telemetry.packet;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * CustomPayloadMonitor — Analyzes and logs plugin messaging channels (custom payload packets)
 * for proxy/server mod detection and telemetry interception.
 */
public class CustomPayloadMonitor extends Module {
    private final ModeSetting channelFilter = new ModeSetting("ChannelFilter", "Plugin messaging channel filter", "ALL", "ALL", "FABRIC_ONLY", "BUNGEE_ONLY", "MINECRAFT_CORE");
    private final BooleanSetting notifyNewChannel = new BooleanSetting("NotifyNewChannel", "Notifies when a new plugin messaging channel is registered", true);
    private final NumberSetting maxPayloadSizeKb = new NumberSetting("MaxPayloadSizeKb", "Maximum allowed payload buffer size in kilobytes", 32.0, 4.0, 256.0, 4.0);

    public CustomPayloadMonitor() {
        super("custom-payload-monitor", "Custom Payload Monitor", "Analyzes and logs plugin messaging channels (custom payload packets) for proxy/server mod detection and telemetry interception.", Category.NETWORK);
        addSettings(channelFilter, notifyNewChannel, maxPayloadSizeKb);
    }

    public ModeSetting getChannelFilter() {
        return channelFilter;
    }

    public BooleanSetting getNotifyNewChannel() {
        return notifyNewChannel;
    }

    public NumberSetting getMaxPayloadSizeKb() {
        return maxPayloadSizeKb;
    }
}
