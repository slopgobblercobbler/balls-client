package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Server tick rate estimation monitor with exponential moving average (EMA) smoothing.
 */
public class ServerTPSHUD extends Module {
    private final BooleanSetting showEma = new BooleanSetting("ShowEMA", "Display EMA smoothed TPS estimate", true);
    private final ColorSetting goodTpsColor = new ColorSetting("GoodTPSColor", "Color when TPS is >= 19.5", 0xFF00FF00);
    private final ColorSetting lagColor = new ColorSetting("LagColor", "Color when TPS drops below 15.0", 0xFFFF2222);

    private double estimatedTps = 20.0;
    private long lastTimePacketMillis = 0;
    private long lastWorldTime = -1;

    public ServerTPSHUD() {
        super("server-tps-hud", "Server TPS HUD", "Real-time server tick rate and lag estimation", Category.RENDER);
        addSettings(showEma, goodTpsColor, lagColor);
    }

    public synchronized void recordTimePacket(long worldTime, long nowMillis) {
        if (lastWorldTime != -1 && lastTimePacketMillis > 0) {
            long timeDelta = worldTime - lastWorldTime;
            long millisDelta = nowMillis - lastTimePacketMillis;
            if (timeDelta > 0 && millisDelta > 0) {
                double instantTps = (timeDelta * 1000.0) / millisDelta;
                instantTps = Math.min(20.0, Math.max(0.0, instantTps));
                // EMA smoothing factor alpha = 0.2
                estimatedTps = (0.2 * instantTps) + (0.8 * estimatedTps);
            }
        }
        lastWorldTime = worldTime;
        lastTimePacketMillis = nowMillis;
    }

    public synchronized double getEstimatedTps() {
        return Math.min(20.0, Math.max(0.0, estimatedTps));
    }

    public int getEffectiveTextColor() {
        double tps = getEstimatedTps();
        if (tps >= 19.5) {
            return goodTpsColor.getValue();
        } else if (tps <= 15.0) {
            return lagColor.getValue();
        } else {
            return 0xFFFFAA00; // Orange for moderate lag
        }
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render TPS readout
    }
}
