package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Scoreboard sidebar HUD customizer with red number toggle and scalable canvas.
 */
public class ScoreboardHUD extends Module {
    private final BooleanSetting hideRedNumbers = new BooleanSetting("HideRedNumbers", "Hide vanilla red score values on right edge", true);
    private final NumberSetting scale = new NumberSetting("Scale", "Scoreboard rendering scale", 1.0, 0.5, 2.0, 0.1);
    private final NumberSetting bgOpacity = new NumberSetting("BgOpacity", "Background fill opacity", 0.4, 0.0, 1.0, 0.05);

    private String objectiveTitle = "Sidebar";
    private final List<String> lines = new ArrayList<>();

    public ScoreboardHUD() {
        super("scoreboard-hud", "Scoreboard HUD", "Draggable and scalable scoreboard sidebar overlay", Category.RENDER);
        addSettings(hideRedNumbers, scale, bgOpacity);
    }

    public synchronized void updateScoreboard(String title, List<String> newLines) {
        this.objectiveTitle = title != null ? title : "";
        this.lines.clear();
        if (newLines != null) {
            this.lines.addAll(newLines);
        }
    }

    public synchronized String getObjectiveTitle() {
        return objectiveTitle;
    }

    public synchronized List<String> getLines() {
        return Collections.unmodifiableList(new ArrayList<>(lines));
    }

    public boolean shouldHideRedNumbers() {
        return hideRedNumbers.getValue();
    }

    public double getScale() {
        return scale.getValue();
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render custom scoreboard
    }
}
