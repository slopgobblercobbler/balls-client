package com.ballsclient.core.module.telemetry.packet;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * PacketFilter — Selectively silences or passes specific packet types based on class name whitelist
 * or blacklist patterns.
 */
public class PacketFilter extends Module {
    private final ModeSetting filterMode = new ModeSetting("FilterMode", "Packet filtering mode", "BLACKLIST", "WHITELIST", "BLACKLIST");
    private final BooleanSetting ignoreKeepAlive = new BooleanSetting("IgnoreKeepAlive", "Bypasses keep-alive packets from filtering", true);
    private final BooleanSetting muteChatPackets = new BooleanSetting("MuteChatPackets", "Silences system and player chat packets", false);

    public PacketFilter() {
        super("packet-filter", "Packet Filter", "Selectively silences or passes specific packet types based on class name whitelist or blacklist patterns.", Category.NETWORK);
        addSettings(filterMode, ignoreKeepAlive, muteChatPackets);
    }

    public ModeSetting getFilterMode() {
        return filterMode;
    }

    public BooleanSetting getIgnoreKeepAlive() {
        return ignoreKeepAlive;
    }

    public BooleanSetting getMuteChatPackets() {
        return muteChatPackets;
    }
}
