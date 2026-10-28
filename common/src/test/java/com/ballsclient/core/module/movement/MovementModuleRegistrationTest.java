package com.ballsclient.core.module.movement;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.setting.Setting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Movement Suite Registration & Invariants Test")
class MovementModuleRegistrationTest {

    @Test
    @DisplayName("Movement Suite: All 85+ concrete modules instantiate with unique IDs and Category.MOVEMENT")
    void testAllMovementModulesInstantiate() {
        List<Module> allMovementModules = MovementModuleCatalog.createAllInstances();
        assertThat(allMovementModules.size())
                .as("Movement module catalog must contain at least 85 modules")
                .isGreaterThanOrEqualTo(85);

        Set<String> uniqueIds = new HashSet<>();
        for (Module m : allMovementModules) {
            assertThat(m.getId()).isNotBlank();
            assertThat(m.getName()).isNotBlank();
            assertThat(m.getDescription()).isNotNull();
            assertThat(m.getCategory()).isEqualTo(Category.MOVEMENT);
            assertThat(m.getKeybind()).isNotNull();
            assertThat(m.getSettings()).isNotEmpty();

            // Verify unique setting names within module
            Set<String> settingNames = new HashSet<>();
            for (Setting<?> s : m.getSettings()) {
                assertThat(s.getName()).isNotBlank();
                assertThat(settingNames.add(s.getName().toLowerCase()))
                        .as("Duplicate setting '%s' in module '%s'", s.getName(), m.getId())
                        .isTrue();
            }

            assertThat(uniqueIds.add(m.getId()))
                    .as("Duplicate module ID found: " + m.getId())
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Movement Suite: ModuleManager registers all catalog modules cleanly")
    void testModuleManagerRegistration() {
        EventBus bus = new EventBus();
        ModuleManager manager = new ModuleManager(bus);
        MovementModuleCatalog.registerAll(manager);

        assertThat(manager.getModules().size()).isGreaterThanOrEqualTo(85);
        assertThat(manager.getModulesByCategory(Category.MOVEMENT).size()).isGreaterThanOrEqualTo(85);
    }
}
