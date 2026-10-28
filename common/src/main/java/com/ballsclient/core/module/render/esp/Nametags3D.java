package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Scaled billboard nametags rendered above players with health, ping, and equipment.
 */
public class Nametags3D extends Module {
    private final BooleanSetting showEquipment = new BooleanSetting("Equipment", "Display armor icons and held enchanted weapons", true);
    private final BooleanSetting showPing = new BooleanSetting("Ping", "Display target connection ping", true);
    private final NumberSetting scale = new NumberSetting("Scale", "Nametag rendering scale factor", 1.0, 0.5, 3.0, 0.1);

    public static class NametagEntity {
        public final String name;
        public final double x, y, z;
        public final float health;
        public final int ping;
        public final String mainHandItem;

        public NametagEntity(String name, double x, double y, double z, float health, int ping, String mainHandItem) {
            this.name = name != null ? name : "Player";
            this.x = x;
            this.y = y;
            this.z = z;
            this.health = health;
            this.ping = ping;
            this.mainHandItem = mainHandItem != null ? mainHandItem : "Air";
        }

        public String formatTag(boolean includePing) {
            if (includePing) {
                return String.format("%s [%.1f HP] [%dms]", name, health, ping);
            }
            return String.format("%s [%.1f HP]", name, health);
        }
    }

    private final List<NametagEntity> renderedTags = new ArrayList<>();

    public Nametags3D() {
        super("nametags-3d", "Nametags 3D", "Scaled billboard nametags showing health, ping, and gear", Category.RENDER);
        addSettings(showEquipment, showPing, scale);
    }

    public synchronized void updateTags(List<NametagEntity> list) {
        renderedTags.clear();
        if (list != null) {
            renderedTags.addAll(list);
        }
    }

    public synchronized List<NametagEntity> getRenderedTags() {
        return Collections.unmodifiableList(new ArrayList<>(renderedTags));
    }

    public boolean isShowEquipment() { return showEquipment.getValue(); }
    public boolean isShowPing() { return showPing.getValue(); }
    public double getScale() { return scale.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D billboard tags
    }
}
