package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Buffers outbound movement packets client-side for burst teleport.
 */
public class Blink extends Module {
    private final NumberSetting maxPackets = new NumberSetting("MaxPackets", "Maximum outbound position packets buffered before flush", 60.0, 10.0, 200.0, 5.0);
    private final BooleanSetting visualizeGhost = new BooleanSetting("VisualizeGhost", "Render ghost dummy at last server-confirmed location", true);
    private final BooleanSetting autoPulse = new BooleanSetting("AutoPulse", "Automatically flush packets in periodic micro-bursts", false);

    public Blink() {
        super("blink", "Blink", "Buffers outbound movement packets client-side for burst teleport", Category.MOVEMENT);
        addSettings(maxPackets, visualizeGhost, autoPulse);
    }

    public double getMaxPackets() { return maxPackets.getValue(); }
    public boolean isVisualizeGhost() { return visualizeGhost.getValue(); }
    public boolean isAutoPulse() { return autoPulse.getValue(); }

    public boolean canBufferMore(int bufferedCount) {
        return isEnabled() && bufferedCount < (int) getMaxPackets();
    }
}
