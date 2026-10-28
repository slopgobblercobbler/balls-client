package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Weapon Switch Buffer — Suppresses attack packets during the 1-tick cooldown immediately following item swap
 */
public class WeaponSwitchBuffer extends Module {
    private final NumberSetting bufferTicks = new NumberSetting("BufferTicks", "Immunity buffer window in ticks following swap", 1.0, 1.0, 3.0, 1.0);
    private final BooleanSetting cancelHitPacket = new BooleanSetting("CancelHitPacket", "Drop attack packet if clicked during buffer", true);

    public WeaponSwitchBuffer() {
        super("weapon-switch-buffer", "Weapon Switch Buffer", "Suppresses attack packets during the 1-tick cooldown immediately following item swap", Category.COMBAT);
        addSettings(bufferTicks, cancelHitPacket);
    }

    public double getBufferTicks() {
        return bufferTicks.getValue();
    }

    public boolean isCancelHitPacket() {
        return cancelHitPacket.getValue();
    }

    public boolean isSwitchBuffered(long ticksSinceSwitch, int bufferTicks) {
        return ticksSinceSwitch < bufferTicks;
    }
}
