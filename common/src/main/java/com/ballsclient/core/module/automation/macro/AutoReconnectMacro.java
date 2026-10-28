package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoReconnectMacro — Detects server disconnection packets and executes automated login/pin sequence upon rejoin.
 */
public class AutoReconnectMacro extends Module {
    private final NumberSetting reconnectDelaySeconds = new NumberSetting("ReconnectDelaySeconds", "Wait time before attempting reconnect in seconds", 5.0, 1.0, 60.0, 1.0);
    private final BooleanSetting autoLoginCommand = new BooleanSetting("AutoLoginCommand", "Dispatches cached auth /login password upon joining", false);

    private boolean reconnectPending = false;
    private long reconnectTriggerTime = 0L;

    public AutoReconnectMacro() {
        super("auto-reconnect-macro", "Auto Reconnect Macro", "Detects server disconnection packets and executes automated login/pin sequence upon rejoin", Category.AUTOMATION);
        addSettings(reconnectDelaySeconds, autoLoginCommand);
    }

    public NumberSetting getReconnectDelaySeconds() {
        return reconnectDelaySeconds;
    }

    public BooleanSetting getAutoLoginCommand() {
        return autoLoginCommand;
    }

    public void triggerDisconnection(long currentTime) {
        this.reconnectPending = true;
        this.reconnectTriggerTime = currentTime;
    }

    public boolean isReadyToReconnect(long currentTime) {
        if (!reconnectPending) return false;
        long delayMillis = (long) (reconnectDelaySeconds.getValue() * 1000.0);
        return currentTime - reconnectTriggerTime >= delayMillis;
    }

    public void clearReconnect() {
        this.reconnectPending = false;
    }
}
