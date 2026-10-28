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
 * RPG-style floating damage numbers rising and fading above damaged entities.
 */
public class DamageIndicatorParticles extends Module {
    private final BooleanSetting showCritsSpecial = new BooleanSetting("CritsSpecial", "Gold numbers and exclamation mark on critical strikes", true);
    private final NumberSetting riseSpeed = new NumberSetting("RiseSpeed", "Upward floating velocity", 0.05, 0.01, 0.2, 0.01);
    private final NumberSetting scale = new NumberSetting("Scale", "Number font scale", 1.0, 0.5, 2.5, 0.1);

    public static class DamageNumber {
        public double x, y, z;
        public final float damage;
        public final boolean isCrit;
        public final long spawnTime;

        public DamageNumber(double x, double y, double z, float damage, boolean isCrit, long spawnTime) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.damage = damage;
            this.isCrit = isCrit;
            this.spawnTime = spawnTime;
        }

        public void tick(double upwardSpeed) {
            this.y += upwardSpeed;
        }

        public float getAlpha(long nowMillis) {
            long age = nowMillis - spawnTime;
            if (age >= 1000L) return 0.0f;
            return 1.0f - (age / 1000.0f);
        }
    }

    private final List<DamageNumber> numbers = new ArrayList<>();

    public DamageIndicatorParticles() {
        super("damage-indicator-particles", "Damage Indicator Particles", "Floating RPG-style combat damage numbers rising above struck entities", Category.RENDER);
        addSettings(showCritsSpecial, riseSpeed, scale);
    }

    public synchronized void spawnDamageNumber(double x, double y, double z, float amount, boolean isCrit, long nowMillis) {
        numbers.add(new DamageNumber(x, y, z, amount, isCrit, nowMillis));
    }

    public synchronized void tickNumbers(long nowMillis) {
        double speed = riseSpeed.getValue();
        numbers.removeIf(n -> n.getAlpha(nowMillis) <= 0.0f);
        for (DamageNumber n : numbers) {
            n.tick(speed);
        }
    }

    public synchronized List<DamageNumber> getNumbers() {
        return Collections.unmodifiableList(new ArrayList<>(numbers));
    }

    public boolean isShowCritsSpecial() { return showCritsSpecial.getValue(); }
    public double getScale() { return scale.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D billboard floating numbers
    }
}
