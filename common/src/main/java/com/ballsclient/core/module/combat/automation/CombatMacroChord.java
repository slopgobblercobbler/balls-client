package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Combat Macro Chord — Multi-action macro chording engine executing complex combat combos in 1 tick
 */
public class CombatMacroChord extends Module {
    private final ModeSetting chordType = new ModeSetting("ChordType", "Predefined combat macro combo sequence", "AnchorBlast", "AnchorBlast", "CrystalPop", "GappleShield");
    private final NumberSetting interActionDelayMs = new NumberSetting("InterActionDelayMs", "Delay between chorded packet transmissions", 0.0, 0.0, 100.0, 5.0);

    public CombatMacroChord() {
        super("combat-macro-chord", "Combat Macro Chord", "Multi-action macro chording engine executing complex combat combos in 1 tick", Category.COMBAT);
        addSettings(chordType, interActionDelayMs);
    }

    public String getChordType() {
        return chordType.getValue();
    }

    public double getInterActionDelayMs() {
        return interActionDelayMs.getValue();
    }

    public boolean isChordExecutable(boolean hasAnchor, boolean hasGlowstone, boolean targetInRange) {
        return hasAnchor && hasGlowstone && targetInRange;
    }
}
