package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.MacroAction;
import com.ballsclient.core.module.automation.model.MacroScriptEngine;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.List;

/**
 * ScriptSequencePlayer — Interprets and executes DSL action scripts (WAIT, CLICK, KEYPRESS, KEYDOWN, SEND, LOOK).
 */
public class ScriptSequencePlayer extends Module {
    private final NumberSetting scriptSpeed = new NumberSetting("ScriptSpeed", "Script execution speed multiplier", 1.0, 0.25, 4.0, 0.25);
    private final BooleanSetting abortOnDamage = new BooleanSetting("AbortOnDamage", "Stops macro script if player takes damage", true);

    private final MacroScriptEngine engine = new MacroScriptEngine();

    public ScriptSequencePlayer() {
        super("script-sequence-player", "Script Sequence Player", "Interprets and executes DSL action scripts (WAIT, CLICK, KEY, SEND, LOOK)", Category.AUTOMATION);
        addSettings(scriptSpeed, abortOnDamage);
    }

    public NumberSetting getScriptSpeed() {
        return scriptSpeed;
    }

    public BooleanSetting getAbortOnDamage() {
        return abortOnDamage;
    }

    public MacroScriptEngine getEngine() {
        return engine;
    }

    public static List<MacroAction> parseScript(String dslScript) {
        return MacroScriptEngine.tokenizeScript(dslScript);
    }
}
