package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Player tab list overlay tweaker with numerical ping and custom layout.
 */
public class TabListTweaker extends Module {
    private final BooleanSetting showPing = new BooleanSetting("ShowPing", "Show numerical millisecond ping next to players", true);
    private final NumberSetting customColumns = new NumberSetting("Columns", "Custom player list column count", 4.0, 1.0, 6.0, 1.0);
    private final NumberSetting bgOpacity = new NumberSetting("BgOpacity", "Background box opacity", 0.5, 0.0, 1.0, 0.05);

    private int totalPlayers = 0;
    private int maxPlayers = 100;

    public TabListTweaker() {
        super("tab-list-tweaker", "Tab List Tweaker", "Enhances multiplayer player list with numerical ping and custom columns", Category.RENDER);
        addSettings(showPing, customColumns, bgOpacity);
    }

    public void setPlayerCounts(int total, int max) {
        this.totalPlayers = total;
        this.maxPlayers = max;
    }

    public int getTotalPlayers() { return totalPlayers; }
    public int getMaxPlayers() { return maxPlayers; }
    public boolean isShowPing() { return showPing.getValue(); }
    public int getColumns() { return customColumns.getIntValue(); }
    public double getBgOpacity() { return bgOpacity.getValue(); }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Tab list render hook
    }
}
