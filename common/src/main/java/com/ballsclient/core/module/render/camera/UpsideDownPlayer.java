package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Renders player models upside down (Dinnerbone Easter Egg effect) purely client-side.
 */
public class UpsideDownPlayer extends Module {
    private final BooleanSetting selfOnly = new BooleanSetting("SelfOnly", "Only flip own player model", true);
    private final BooleanSetting allPlayers = new BooleanSetting("AllPlayers", "Flip all surrounding players", false);

    public UpsideDownPlayer() {
        super("upside-down-player", "Upside Down Player", "Renders player models upside down (Dinnerbone cosmetic effect)", Category.RENDER);
        addSettings(selfOnly, allPlayers);
    }

    public boolean isSelfOnly() { return selfOnly.getValue(); }
    public boolean isAllPlayers() { return allPlayers.getValue(); }
}
