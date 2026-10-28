package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Tactical hit marker overlay animating cross ticks upon landing successful attacks.
 */
public class HitMarkerOverlay extends Module {
    private final ColorSetting markerColor = new ColorSetting("MarkerColor", "Color of regular hit indicator", 0xFFFFFFFF);
    private final ColorSetting critColor = new ColorSetting("CritColor", "Color of critical strike hit indicator", 0xFFFF2222);
    private final NumberSetting fadeDurationMs = new NumberSetting("FadeDurationMs", "Animation duration of hit marker in milliseconds", 200.0, 50.0, 500.0, 25.0);

    private long lastHitTime = 0;
    private boolean lastHitCritical = false;

    public HitMarkerOverlay() {
        super("hit-marker-overlay", "Hit Marker Overlay", "Displays tactile cross lines when striking entities", Category.RENDER);
        addSettings(markerColor, critColor, fadeDurationMs);
    }

    public void triggerHit(boolean isCritical, long nowMillis) {
        this.lastHitTime = nowMillis;
        this.lastHitCritical = isCritical;
    }

    public float getHitMarkerAlpha(long nowMillis) {
        long elapsed = nowMillis - lastHitTime;
        double duration = fadeDurationMs.getValue();
        if (elapsed >= duration || elapsed < 0) {
            return 0.0f;
        }
        return (float) (1.0 - (elapsed / duration));
    }

    public int getActiveMarkerColor(long nowMillis) {
        float alpha = getHitMarkerAlpha(nowMillis);
        if (alpha <= 0.0f) return 0;
        int base = lastHitCritical ? critColor.getValue() : markerColor.getValue();
        int a = (int) (alpha * 255.0f);
        return (a << 24) | (base & 0x00FFFFFF);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render hit marker lines
    }
}
