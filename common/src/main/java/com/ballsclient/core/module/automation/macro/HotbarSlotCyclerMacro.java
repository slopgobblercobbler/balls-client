package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * HotbarSlotCyclerMacro — Cycles hotbar selection through predefined patterns on configurable timer.
 */
public class HotbarSlotCyclerMacro extends Module {
    private final NumberSetting cycleDelayTicks = new NumberSetting("CycleDelayTicks", "Tick delay between slot transitions", 10.0, 1.0, 100.0, 1.0);
    private final ModeSetting cyclePattern = new ModeSetting("CyclePattern", "Hotbar traversal pattern", "Ascending_1_9", "Ascending_1_9", "PingPong", "CustomSlots");

    private int activeSlot = 0;
    private boolean pingPongForward = true;

    public HotbarSlotCyclerMacro() {
        super("hotbar-slot-cycler-macro", "Hotbar Slot Cycler Macro", "Cycles hotbar selection through predefined patterns (e.g. 1 -> 2 -> 3) on configurable timer", Category.AUTOMATION);
        addSettings(cycleDelayTicks, cyclePattern);
    }

    public NumberSetting getCycleDelayTicks() {
        return cycleDelayTicks;
    }

    public ModeSetting getCyclePattern() {
        return cyclePattern;
    }

    public int getActiveSlot() {
        return activeSlot;
    }

    public int computeNextSlot() {
        String mode = cyclePattern.getValue();
        if ("PingPong".equalsIgnoreCase(mode)) {
            if (pingPongForward) {
                activeSlot++;
                if (activeSlot >= 8) {
                    activeSlot = 8;
                    pingPongForward = false;
                }
            } else {
                activeSlot--;
                if (activeSlot <= 0) {
                    activeSlot = 0;
                    pingPongForward = true;
                }
            }
        } else {
            activeSlot = (activeSlot + 1) % 9;
        }
        return activeSlot;
    }
}
