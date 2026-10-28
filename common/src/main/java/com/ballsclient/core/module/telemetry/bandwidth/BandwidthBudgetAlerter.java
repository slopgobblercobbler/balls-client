package com.ballsclient.core.module.telemetry.bandwidth;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BandwidthBudgetAlerter — Allows players on metered or tethered connections to configure data caps
 * and receive alerts when consumption exceeds thresholds.
 */
public class BandwidthBudgetAlerter extends Module {
    private final NumberSetting budgetLimitMb = new NumberSetting("BudgetLimitMb", "Session bandwidth budget quota in megabytes", 500.0, 50.0, 5000.0, 50.0);
    private final NumberSetting warningThresholdPercent = new NumberSetting("WarningThresholdPercent", "Quota consumption warning trigger percentage", 80.0, 50.0, 95.0, 5.0);
    private final BooleanSetting disconnectOnLimit = new BooleanSetting("DisconnectOnLimit", "Automatically disconnects from server when budget cap is reached", false);

    public BandwidthBudgetAlerter() {
        super("bandwidth-budget-alerter", "Bandwidth Budget Alerter", "Allows players on metered or tethered connections to configure data caps and receive alerts when consumption exceeds thresholds.", Category.NETWORK);
        addSettings(budgetLimitMb, warningThresholdPercent, disconnectOnLimit);
    }

    public NumberSetting getBudgetLimitMb() {
        return budgetLimitMb;
    }

    public NumberSetting getWarningThresholdPercent() {
        return warningThresholdPercent;
    }

    public BooleanSetting getDisconnectOnLimit() {
        return disconnectOnLimit;
    }
}
