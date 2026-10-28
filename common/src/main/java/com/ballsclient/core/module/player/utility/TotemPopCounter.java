package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.Map;

/**
 * TotemPopCounter — Tracks and logs totem activation events for self and visible players.
 */
public class TotemPopCounter extends Module {
    private final BooleanSetting broadcastChat = new BooleanSetting("BroadcastChat", "Print player totem pop count in client chat", true);
    private final BooleanSetting trackEnemies = new BooleanSetting("TrackEnemies", "Monitor opponent totem consumption", true);
    private final BooleanSetting trackSelf = new BooleanSetting("TrackSelf", "Monitor local player totem pop count", true);

    public TotemPopCounter() {
        super("totem-pop-counter", "Totem Pop Counter", "Tracks and logs totem activation events for self and visible players", Category.PLAYER);
        addSettings(broadcastChat, trackEnemies, trackSelf);
    }

    public BooleanSetting getBroadcastChat() {
        return broadcastChat;
    }

    public BooleanSetting getTrackEnemies() {
        return trackEnemies;
    }

    public BooleanSetting getTrackSelf() {
        return trackSelf;
    }

    public static int incrementPops(Map<String, Integer> registry, String playerName) {
        if (registry == null || playerName == null) return 0;
        int current = registry.getOrDefault(playerName, 0);
        int next = current + 1;
        registry.put(playerName, next);
        return next;
    }
}
