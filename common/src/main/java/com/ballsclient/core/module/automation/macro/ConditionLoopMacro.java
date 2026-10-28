package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ConditionLoopMacro — Executes repetitive action loops while verifying health, hunger, or inventory thresholds.
 */
public class ConditionLoopMacro extends Module {
    private final NumberSetting maxIterations = new NumberSetting("MaxIterations", "Safety limit for loop iterations", 100.0, 1.0, 10000.0, 10.0);
    private final NumberSetting pauseHealthThreshold = new NumberSetting("PauseHealthThreshold", "Pauses loop when player health drops below value", 8.0, 1.0, 20.0, 1.0);

    private int currentIteration = 0;

    public ConditionLoopMacro() {
        super("condition-loop-macro", "Condition Loop Macro", "Executes repetitive action loops while verifying health, hunger, or inventory thresholds", Category.AUTOMATION);
        addSettings(maxIterations, pauseHealthThreshold);
    }

    public NumberSetting getMaxIterations() {
        return maxIterations;
    }

    public NumberSetting getPauseHealthThreshold() {
        return pauseHealthThreshold;
    }

    public boolean canContinue(double currentHealth, int iterationsDone) {
        if (currentHealth < pauseHealthThreshold.getValue()) {
            return false;
        }
        return iterationsDone < maxIterations.getValue();
    }

    public void incrementIteration() {
        currentIteration++;
    }

    public void resetIteration() {
        currentIteration = 0;
    }

    public int getCurrentIteration() {
        return currentIteration;
    }
}
