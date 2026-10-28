package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Eliminates violent camera tilt, shaking, and red screen flash when taking damage.
 */
public class NoHurtCam extends Module {
    private final BooleanSetting disableShake = new BooleanSetting("DisableShake", "Disable all damage camera tilt and shake", true);

    public NoHurtCam() {
        super("no-hurt-cam", "No Hurt Cam", "Eliminates camera shaking and tilting upon taking combat damage", Category.RENDER);
        addSettings(disableShake);
    }

    public boolean isDisableShake() { return disableShake.getValue(); }
}
