package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;
import java.util.Map;

/**
 * Totem Pop Counter — Tracks, visualizes, and announces totem pops for all enemy players in render distance
 */
public class TotemPopCounter extends Module {
    private final BooleanSetting chatAnnouncement = new BooleanSetting("ChatAnnouncement", "Output pop count to client chat", true);
    private final BooleanSetting renderOnTargetHud = new BooleanSetting("RenderOnTargetHud", "Display pops on Target HUD", true);

    public TotemPopCounter() {
        super("totem-pop-counter", "Totem Pop Counter", "Tracks, visualizes, and announces totem pops for all enemy players in render distance", Category.COMBAT);
        addSettings(chatAnnouncement, renderOnTargetHud);
    }

    public boolean isChatAnnouncement() {
        return chatAnnouncement.getValue();
    }

    public boolean isRenderOnTargetHud() {
        return renderOnTargetHud.getValue();
    }

    public int incrementPopCount(Map<String, Integer> popMap, String playerName) {
        int count = popMap.getOrDefault(playerName, 0) + 1;
        popMap.put(playerName, count);
        return count;
    }
}
