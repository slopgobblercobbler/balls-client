package com.ballsclient.core.module;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.event.TickEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Module Lifecycle State Machine Test Suite")
class ModuleLifecycleTest {

    static class TestCustomModule extends Module {
        final AtomicBoolean enabledCalled = new AtomicBoolean(false);
        final AtomicBoolean disabledCalled = new AtomicBoolean(false);
        final AtomicInteger tickCount = new AtomicInteger(0);

        TestCustomModule() {
            super("test-custom-module", "Test Custom Module", "Lifecycle testing module", Category.RENDER);
        }

        @Override
        protected void onEnable() {
            enabledCalled.set(true);
        }

        @Override
        protected void onDisable() {
            disabledCalled.set(true);
        }

        @Override
        public void onTick() {
            tickCount.incrementAndGet();
        }
    }

    @Test
    @DisplayName("Lifecycle: Transitions invoke onEnable and onDisable hooks and update enabled state")
    void testEnableDisableTransitions() {
        TestCustomModule mod = new TestCustomModule();
        assertThat(mod.isEnabled()).isFalse();

        mod.setEnabled(true);
        assertThat(mod.isEnabled()).isTrue();
        assertThat(mod.enabledCalled.get()).isTrue();

        // Enabling again is idempotent
        mod.enabledCalled.set(false);
        mod.setEnabled(true);
        assertThat(mod.enabledCalled.get()).isFalse();

        // Disabling
        mod.setEnabled(false);
        assertThat(mod.isEnabled()).isFalse();
        assertThat(mod.disabledCalled.get()).isTrue();
    }

    @Test
    @DisplayName("Toggle: toggle() flips state back and forth cleanly")
    void testToggle() {
        TestCustomModule mod = new TestCustomModule();
        assertThat(mod.isEnabled()).isFalse();

        mod.toggle();
        assertThat(mod.isEnabled()).isTrue();

        mod.toggle();
        assertThat(mod.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("Exception Safety: Unhandled exceptions in onEnable or onDisable do not propagate to caller")
    void testLifecycleExceptionSafety() {
        Module faulty = new Module("faulty-module", "Faulty", "Desc", Category.RENDER) {
            @Override
            protected void onEnable() {
                throw new IllegalStateException("Simulated onEnable explosion");
            }

            @Override
            protected void onDisable() {
                throw new IllegalStateException("Simulated onDisable explosion");
            }
        };

        assertThatCode(() -> faulty.setEnabled(true)).doesNotThrowAnyException();
        assertThat(faulty.isEnabled()).isTrue();

        assertThatCode(() -> faulty.setEnabled(false)).doesNotThrowAnyException();
        assertThat(faulty.isEnabled()).isFalse();
    }
}
