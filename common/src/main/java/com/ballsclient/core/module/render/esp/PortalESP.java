package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Glowing dimensional portal aura highlighting Nether, End, and Gateway portals.
 */
public class PortalESP extends Module {
    private final BooleanSetting netherPortals = new BooleanSetting("NetherPortals", "Highlight purple Nether portals", true);
    private final BooleanSetting endPortals = new BooleanSetting("EndPortals", "Highlight End Portal frames and Gateways", true);
    private final ColorSetting netherColor = new ColorSetting("NetherColor", "Nether portal glow color", 0xFF9900FF);

    public static class PortalBlock {
        public final int x, y, z;
        public final String dimension;

        public PortalBlock(int x, int y, int z, String dimension) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.dimension = dimension != null ? dimension : "NETHER";
        }
    }

    private final List<PortalBlock> portals = new ArrayList<>();

    public PortalESP() {
        super("portal-esp", "Portal ESP", "Dimensional aura outlining Nether and End portals", Category.RENDER);
        addSettings(netherPortals, endPortals, netherColor);
    }

    public synchronized void updatePortals(List<PortalBlock> list) {
        portals.clear();
        if (list != null) {
            for (PortalBlock p : list) {
                if ("NETHER".equalsIgnoreCase(p.dimension) && !netherPortals.getValue()) continue;
                if ("END".equalsIgnoreCase(p.dimension) && !endPortals.getValue()) continue;
                portals.add(p);
            }
        }
    }

    public synchronized List<PortalBlock> getPortals() {
        return Collections.unmodifiableList(new ArrayList<>(portals));
    }

    public int getNetherColor() { return netherColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D portal wireframes
    }
}
