package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Eliminates visual blindness fog, nausea screen distortion, and warden darkness effects.
 */
public class AntiBlind extends Module {
    private final BooleanSetting blindness = new BooleanSetting("Blindness", "Disable black blindness sphere", true);
    private final BooleanSetting nausea = new BooleanSetting("Nausea", "Disable nauseating screen wobble", true);
    private final BooleanSetting darkness = new BooleanSetting("Darkness", "Disable warden pulsating darkness shader", true);

    public AntiBlind() {
        super("anti-blind", "Anti Blind", "Negates blindness, nausea screen distortion, and darkness shaders", Category.RENDER);
        addSettings(blindness, nausea, darkness);
    }

    public boolean isBlindnessDisabled() { return blindness.getValue(); }
    public boolean isNauseaDisabled() { return nausea.getValue(); }
    public boolean isDarknessDisabled() { return darkness.getValue(); }
}
