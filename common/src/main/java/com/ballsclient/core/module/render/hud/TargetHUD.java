package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * Combat target overlay rendering health percentage, armor, and entity status.
 */
public class TargetHUD extends Module {
    private final ModeSetting layout = new ModeSetting("Layout", "Target HUD display style", "COMPACT", "COMPACT", "DETAILED");
    private final ColorSetting healthGradient = new ColorSetting("HealthGradient", "Health bar gradient tint", 0xFFFF2222);
    private final BooleanSetting showArmor = new BooleanSetting("ShowArmor", "Render target equipped armor pieces", true);

    private String targetName = "None";
    private float targetHealth = 20.0f;
    private float targetMaxHealth = 20.0f;
    private float animatedHealth = 20.0f;

    public TargetHUD() {
        super("target-hud", "Target HUD", "Displays combat target health and equipment stats", Category.RENDER);
        addSettings(layout, healthGradient, showArmor);
    }

    public void setTarget(String name, float health, float maxHealth) {
        this.targetName = name != null ? name : "None";
        this.targetHealth = Math.max(0.0f, health);
        this.targetMaxHealth = Math.max(1.0f, maxHealth);
    }

    public void updateAnimation(float delta) {
        float diff = targetHealth - animatedHealth;
        animatedHealth += diff * Math.min(1.0f, delta * 8.0f);
    }

    public float getHealthPercentage() {
        return Math.min(100.0f, Math.max(0.0f, (targetHealth / targetMaxHealth) * 100.0f));
    }

    public String getTargetName() { return targetName; }
    public float getTargetHealth() { return targetHealth; }
    public float getTargetMaxHealth() { return targetMaxHealth; }
    public float getAnimatedHealth() { return animatedHealth; }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Target HUD render
    }
}
