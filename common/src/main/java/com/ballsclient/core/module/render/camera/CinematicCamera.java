package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Toggles smooth cinematic camera panning with customizable mouse dampening.
 */
public class CinematicCamera extends Module {
    private final NumberSetting damping = new NumberSetting("Damping", "Mouse pan smoothing dampening factor", 0.7, 0.1, 0.95, 0.05);

    public CinematicCamera() {
        super("cinematic-camera", "Cinematic Camera", "Cinematic camera mode with customizable rotational inertia dampening", Category.RENDER);
        addSettings(damping);
    }

    public double getDamping() { return damping.getValue(); }
}
