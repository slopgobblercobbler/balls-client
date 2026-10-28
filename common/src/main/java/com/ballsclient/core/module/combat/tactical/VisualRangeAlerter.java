package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;
import java.util.Set;

/**
 * Visual Range Alerter — Audio-visual alert when enemy players enter or exit client render distance
 */
public class VisualRangeAlerter extends Module {
    private final BooleanSetting playAlertSound = new BooleanSetting("PlayAlertSound", "Play ping upon new player spotted", true);
    private final BooleanSetting logToChat = new BooleanSetting("LogToChat", "Print name and coordinates to client chat", true);

    public VisualRangeAlerter() {
        super("visual-range-alerter", "Visual Range Alerter", "Audio-visual alert when enemy players enter or exit client render distance", Category.COMBAT);
        addSettings(playAlertSound, logToChat);
    }

    public boolean isPlayAlertSound() {
        return playAlertSound.getValue();
    }

    public boolean isLogToChat() {
        return logToChat.getValue();
    }

    public boolean isNewPlayerSpotted(Set<String> knownPlayers, String spottedPlayer) {
        return knownPlayers.add(spottedPlayer);
    }
}
