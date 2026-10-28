package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Highlights safe 1x1 blast holes for Crystal PvP (Bedrock = Green, Obsidian = Yellow).
 */
public class HoleESP extends Module {
    private final BooleanSetting bedrockOnly = new BooleanSetting("BedrockOnly", "Only highlight unbreakable bedrock holes", false);
    private final NumberSetting searchDistance = new NumberSetting("SearchDistance", "Scanning distance in blocks", 6.0, 2.0, 16.0, 1.0);
    private final ColorSetting safeColor = new ColorSetting("SafeColor", "Bedrock hole color (Green)", 0xFF00FF00);

    public enum HoleType {
        BEDROCK(0xFF00FF00),
        OBSIDIAN(0xFFFFFF00),
        DOUBLE(0xFF00FFFF);

        public final int color;
        HoleType(int color) { this.color = color; }
    }

    public static class CombatHole {
        public final int x, y, z;
        public final HoleType type;

        public CombatHole(int x, int y, int z, HoleType type) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.type = type;
        }
    }

    private final List<CombatHole> detectedHoles = new ArrayList<>();

    public HoleESP() {
        super("hole-esp", "Hole ESP", "Highlights defensive blast protection holes for crystal combat", Category.RENDER);
        addSettings(bedrockOnly, searchDistance, safeColor);
    }

    public synchronized void updateHoles(List<CombatHole> holes) {
        detectedHoles.clear();
        if (holes != null) {
            for (CombatHole h : holes) {
                if (bedrockOnly.getValue() && h.type != HoleType.BEDROCK) continue;
                detectedHoles.add(h);
            }
        }
    }

    public synchronized List<CombatHole> getDetectedHoles() {
        return Collections.unmodifiableList(new ArrayList<>(detectedHoles));
    }

    public int getSearchDistance() { return searchDistance.getIntValue(); }
    public int getSafeColor() { return safeColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D combat hole boxes
    }
}
