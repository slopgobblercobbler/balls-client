package com.clientmod.core.movement;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class MovementAndMacroTest {

    @Test
    void testAutoSprintStateTransitions() {
        AutoSprintController controller = new AutoSprintController();
        assertThat(controller.isEnabled()).isFalse();

        controller.onTick(true);
        assertThat(controller.shouldSprint()).isFalse();

        controller.setEnabled(true);
        controller.onTick(false);
        assertThat(controller.shouldSprint()).isFalse();

        controller.onTick(true);
        assertThat(controller.shouldSprint()).isTrue();

        controller.setEnabled(false);
        assertThat(controller.shouldSprint()).isFalse();
    }

    @Test
    void testMacroQueueExecutionWithDelay() {
        MacroExecutionEngine engine = new MacroExecutionEngine();
        AtomicBoolean actionExecuted = new AtomicBoolean(false);

        engine.queueAction(() -> actionExecuted.set(true));
        engine.setTickDelay(2);

        // Tick 1: delay decrements from 2 to 1
        engine.onTick();
        assertThat(actionExecuted.get()).isFalse();

        // Tick 2: delay decrements from 1 to 0
        engine.onTick();
        assertThat(actionExecuted.get()).isFalse();

        // Tick 3: action executes
        engine.onTick();
        assertThat(actionExecuted.get()).isTrue();
        assertThat(engine.getQueueSize()).isEqualTo(0);
    }
}
