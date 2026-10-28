package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Counts and classifies rendered entities in camera frustum versus loaded entities.
 */
public class EntityCounterHUD extends Module {
    private final BooleanSetting splitHostilePassive = new BooleanSetting("SplitTypes", "Differentiate hostile mobs from passive animals", true);
    private final BooleanSetting showClumpWarning = new BooleanSetting("ClumpWarning", "Show warning when > 50 entities are concentrated nearby", true);

    private int totalLoaded = 0;
    private int inFrustum = 0;
    private int hostileCount = 0;
    private int passiveCount = 0;
    private int playerCount = 0;

    public EntityCounterHUD() {
        super("entity-counter-hud", "Entity Counter HUD", "Categorized counter of rendered vs loaded entities", Category.RENDER);
        addSettings(splitHostilePassive, showClumpWarning);
    }

    public void updateCounts(int total, int frustum, int hostile, int passive, int players) {
        this.totalLoaded = total;
        this.inFrustum = frustum;
        this.hostileCount = hostile;
        this.passiveCount = passive;
        this.playerCount = players;
    }

    public boolean hasEntityClump() {
        return showClumpWarning.getValue() && inFrustum >= 50;
    }

    public String formatDisplayText() {
        if (splitHostilePassive.getValue()) {
            return String.format("Entities: %d/%d (H:%d P:%d Pl:%d)", inFrustum, totalLoaded, hostileCount, passiveCount, playerCount);
        } else {
            return String.format("Entities: %d/%d", inFrustum, totalLoaded);
        }
    }

    public int getTotalLoaded() { return totalLoaded; }
    public int getInFrustum() { return inFrustum; }
    public int getHostileCount() { return hostileCount; }
    public int getPassiveCount() { return passiveCount; }
    public int getPlayerCount() { return playerCount; }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render entity counter
    }
}
