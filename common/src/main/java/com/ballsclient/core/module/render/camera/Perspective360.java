package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.KeybindSetting;

/**
 * 360-degree freelook orbital camera rotation around player while maintaining forward movement direction.
 */
public class Perspective360 extends Module {
    private final KeybindSetting freelookKey = new KeybindSetting("FreelookKey", "Key to hold for 360 camera freelook", 86); // GLFW_KEY_V = 86
    private final BooleanSetting invertPitch = new BooleanSetting("InvertPitch", "Invert vertical pitch axis in freelook", false);

    private float cameraYaw = 0.0f;
    private float cameraPitch = 0.0f;

    public Perspective360() {
        super("perspective-360", "Perspective 360", "Orbital 360-degree camera freelook while running in a straight line", Category.RENDER);
        addSettings(freelookKey, invertPitch);
    }

    public void updateFreelook(float deltaYaw, float deltaPitch) {
        this.cameraYaw += deltaYaw;
        this.cameraPitch += (invertPitch.getValue() ? -deltaPitch : deltaPitch);
        this.cameraPitch = Math.max(-90.0f, Math.min(90.0f, this.cameraPitch));
    }

    public KeybindSetting getFreelookKey() { return freelookKey; }
    public boolean isInvertPitch() { return invertPitch.getValue(); }
    public float getCameraYaw() { return cameraYaw; }
    public float getCameraPitch() { return cameraPitch; }
}
