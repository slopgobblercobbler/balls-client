package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.MacroChord;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.Set;

/**
 * MacroChordTrigger — Triggers complex macros on simultaneous multi-key chords.
 */
public class MacroChordTrigger extends Module {
    private final NumberSetting chordWindowMs = new NumberSetting("ChordWindowMs", "Temporal tolerance window for simultaneous keys in milliseconds", 150.0, 50.0, 500.0, 25.0);
    private final BooleanSetting allowMouseButtons = new BooleanSetting("AllowMouseButtons", "Permits mouse button codes inside chord combinations", true);

    public MacroChordTrigger() {
        super("macro-chord-trigger", "Macro Chord Trigger", "Triggers complex macros on simultaneous multi-key chords (e.g. CTRL+SHIFT+B)", Category.AUTOMATION);
        addSettings(chordWindowMs, allowMouseButtons);
    }

    public NumberSetting getChordWindowMs() {
        return chordWindowMs;
    }

    public BooleanSetting getAllowMouseButtons() {
        return allowMouseButtons;
    }

    public static boolean matchesChord(Set<Integer> currentlyPressedKeys, Set<Integer> requiredChordKeys) {
        return MacroChord.matchesChord(currentlyPressedKeys, requiredChordKeys);
    }
}
