package com.ballsclient.core.module.automation;

import com.ballsclient.core.module.automation.model.MacroAction;
import com.ballsclient.core.module.automation.model.MacroAction.CommandType;
import com.ballsclient.core.module.automation.model.MacroChord;
import com.ballsclient.core.module.automation.model.MacroScriptEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Macro Script DSL Tokenizer & Action Queue Engine Test")
class MacroScriptParserTest {

    public static List<MacroAction> tokenizeScript(String script) {
        return MacroScriptEngine.tokenizeScript(script);
    }

    @Test
    @DisplayName("Tokenize basic macro script sequence")
    void testScriptTokenization() {
        String script = """
                WAIT 5;
                CLICK RIGHT;
                SEND /home;
                KEYDOWN W 20;
                LOOK 90.0 0.0;
                SELECT 2
                """;

        List<MacroAction> steps = tokenizeScript(script);
        assertThat(steps).hasSize(6);

        assertThat(steps.get(0).type()).isEqualTo(CommandType.WAIT);
        assertThat(steps.get(0).args()).containsExactly("5");

        assertThat(steps.get(1).type()).isEqualTo(CommandType.CLICK);
        assertThat(steps.get(1).args()).containsExactly("RIGHT");

        assertThat(steps.get(2).type()).isEqualTo(CommandType.SEND);
        assertThat(steps.get(2).args()).containsExactly("/home");

        assertThat(steps.get(3).type()).isEqualTo(CommandType.KEYDOWN);
        assertThat(steps.get(3).args()).containsExactly("W", "20");

        assertThat(steps.get(4).type()).isEqualTo(CommandType.LOOK);
        assertThat(steps.get(4).args()).containsExactly("90.0", "0.0");

        assertThat(steps.get(5).type()).isEqualTo(CommandType.SELECT);
        assertThat(steps.get(5).args()).containsExactly("2");
    }

    @Test
    @DisplayName("Chord trigger bitmask matching")
    void testChordTriggerMatching() {
        // Keycodes: CTRL=341, SHIFT=340, B=66
        Set<Integer> chord = Set.of(341, 340, 66);
        MacroChord macroChord = new MacroChord("TestChord", chord);

        Set<Integer> pressedExact = new HashSet<>(Set.of(341, 340, 66));
        assertThat(macroChord.matches(pressedExact)).isTrue();
        assertThat(MacroChord.matchesChord(pressedExact, chord)).isTrue();

        Set<Integer> pressedSuperSet = new HashSet<>(Set.of(341, 340, 66, 87)); // extra W key
        assertThat(macroChord.matches(pressedSuperSet)).isTrue();
        assertThat(MacroChord.matchesChord(pressedSuperSet, chord)).isTrue();

        Set<Integer> pressedMissing = new HashSet<>(Set.of(341, 66)); // missing SHIFT
        assertThat(macroChord.matches(pressedMissing)).isFalse();
        assertThat(MacroChord.matchesChord(pressedMissing, chord)).isFalse();
    }

    @Test
    @DisplayName("MacroScriptEngine tick queue and wait countdown")
    void testEngineTickQueue() {
        MacroScriptEngine engine = new MacroScriptEngine();
        engine.enqueueScript("WAIT 2; CLICK LEFT");

        assertThat(engine.isRunning()).isTrue();
        // Tick 1: processes WAIT 2, sets waitTicksRemaining = 1
        MacroAction first = engine.tick();
        assertThat(first).isNotNull();
        assertThat(first.type()).isEqualTo(CommandType.WAIT);

        // Tick 2: waitTicksRemaining decrements to 0, returns null
        MacroAction second = engine.tick();
        assertThat(second).isNull();

        // Tick 3: wait expired, executes CLICK LEFT
        MacroAction third = engine.tick();
        assertThat(third).isNotNull();
        assertThat(third.type()).isEqualTo(CommandType.CLICK);
        assertThat(third.getFirstArg()).isEqualTo("LEFT");

        // Tick 4: queue empty
        MacroAction fourth = engine.tick();
        assertThat(fourth).isNull();
        assertThat(engine.isRunning()).isFalse();
    }
}
