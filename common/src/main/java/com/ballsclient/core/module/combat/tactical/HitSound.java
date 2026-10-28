package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Hit Sound — Customizable tactical audio feedback on confirmed hits
 */
public class HitSound extends Module {
    private final ModeSetting soundType = new ModeSetting("SoundType", "Hit registration audio sample", "Ding", "Ding", "Bell", "Punch", "Cod", "Skeletal");
    private final NumberSetting volume = new NumberSetting("Volume", "Hit sound audio volume", 0.8, 0.1, 1.0, 0.05);
    private final NumberSetting pitch = new NumberSetting("Pitch", "Baseline hit sound pitch", 1.0, 0.5, 2.0, 0.05);

    public HitSound() {
        super("hit-sound", "Hit Sound", "Customizable tactical audio feedback on confirmed hits", Category.COMBAT);
        addSettings(soundType, volume, pitch);
    }

    public String getSoundType() {
        return soundType.getValue();
    }

    public double getVolume() {
        return volume.getValue();
    }

    public double getPitch() {
        return pitch.getValue();
    }

    public float calculateDynamicPitch(double comboCount, float basePitch) {
        return (float) Math.min(2.0, basePitch + comboCount * 0.05);
    }
}
