package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Cancels portal nausea distortion and auto-egresses after teleport.
 */
public class PortalLockFailsafe extends Module {
    private final BooleanSetting cancelNausea = new BooleanSetting("CancelNausea", "Suppress portal wobble and nausea screen shader", true);
    private final BooleanSetting autoEgress = new BooleanSetting("AutoEgress", "Automatically walk 1 block forward to exit portal collision box", true);

    public PortalLockFailsafe() {
        super("portal-lock-failsafe", "Portal Lock Failsafe", "Cancels portal nausea distortion and auto-egresses after teleport", Category.MOVEMENT);
        addSettings(cancelNausea, autoEgress);
    }

    public boolean isCancelNausea() { return cancelNausea.getValue(); }
    public boolean isAutoEgress() { return autoEgress.getValue(); }

    public boolean shouldCancelNausea() { return isEnabled() && isCancelNausea(); }
    public boolean shouldEgress(boolean inPortal, boolean destinationLoaded) {
        return isEnabled() && isAutoEgress() && inPortal && destinationLoaded;
    }
}
