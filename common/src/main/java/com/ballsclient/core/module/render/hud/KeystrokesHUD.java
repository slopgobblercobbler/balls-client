package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.KeyEvent;
import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * On-screen display for WASD movement keys, jump bar, and LMB/RMB CPS click counters.
 */
public class KeystrokesHUD extends Module {
    private final ColorSetting pressedColor = new ColorSetting("PressedColor", "Color overlay when key is pressed", 0x80FFFFFF);
    private final BooleanSetting showCps = new BooleanSetting("ShowCPS", "Show clicks-per-second on mouse buttons", true);
    private final NumberSetting keySize = new NumberSetting("KeySize", "Size of key boxes in pixels", 24.0, 16.0, 48.0, 2.0);

    private boolean keyW;
    private boolean keyA;
    private boolean keyS;
    private boolean keyD;
    private boolean keySpace;

    private final Deque<Long> lmbClicks = new ArrayDeque<>();
    private final Deque<Long> rmbClicks = new ArrayDeque<>();

    public KeystrokesHUD() {
        super("keystrokes-hud", "Keystrokes HUD", "Visual keyboard and CPS click monitor", Category.RENDER);
        addSettings(pressedColor, showCps, keySize);
    }

    public void registerLmbClick(long timeMillis) {
        lmbClicks.addLast(timeMillis);
        pruneClicks(lmbClicks, timeMillis);
    }

    public void registerRmbClick(long timeMillis) {
        rmbClicks.addLast(timeMillis);
        pruneClicks(rmbClicks, timeMillis);
    }

    public int getLmbCps(long nowMillis) {
        pruneClicks(lmbClicks, nowMillis);
        return lmbClicks.size();
    }

    public int getRmbCps(long nowMillis) {
        pruneClicks(rmbClicks, nowMillis);
        return rmbClicks.size();
    }

    private void pruneClicks(Deque<Long> queue, long nowMillis) {
        while (!queue.isEmpty() && nowMillis - queue.peekFirst() > 1000L) {
            queue.pollFirst();
        }
    }

    public void setKeyState(boolean w, boolean a, boolean s, boolean d, boolean space) {
        this.keyW = w;
        this.keyA = a;
        this.keyS = s;
        this.keyD = d;
        this.keySpace = space;
    }

    public boolean isKeyW() { return keyW; }
    public boolean isKeyA() { return keyA; }
    public boolean isKeyS() { return keyS; }
    public boolean isKeyD() { return keyD; }
    public boolean isKeySpace() { return keySpace; }

    @Subscribe
    public void onKey(KeyEvent event) {
        // Keyboard listening
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Keystrokes 2D render
    }
}
