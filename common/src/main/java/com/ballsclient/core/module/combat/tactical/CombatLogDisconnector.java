package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Combat Log Disconnector — Emergency combat failsafe alerting or disconnecting when survival chances reach zero
 */
public class CombatLogDisconnector extends Module {
    private final NumberSetting criticalHealth = new NumberSetting("CriticalHealth", "Health threshold for emergency egress", 3.0, 1.0, 6.0, 0.5);
    private final BooleanSetting disconnectOnPop = new BooleanSetting("DisconnectOnPop", "Disconnect if all totems are exhausted", false);

    public CombatLogDisconnector() {
        super("combat-log-disconnector", "Combat Log Disconnector", "Emergency combat failsafe alerting or disconnecting when survival chances reach zero", Category.COMBAT);
        addSettings(criticalHealth, disconnectOnPop);
    }

    public double getCriticalHealth() {
        return criticalHealth.getValue();
    }

    public boolean isDisconnectOnPop() {
        return disconnectOnPop.getValue();
    }

    public boolean shouldEmergencyEgress(double hp, double critThreshold, int remainingTotems, boolean discOnZeroTotems) {
        if (hp <= critThreshold && remainingTotems == 0) return true;
        if (discOnZeroTotems && remainingTotems == 0) return true;
        return false;
    }
}
