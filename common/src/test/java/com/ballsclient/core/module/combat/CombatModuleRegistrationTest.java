package com.ballsclient.core.module.combat;

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

@DisplayName("Combat Suite Registration & Invariants Test")
class CombatModuleRegistrationTest {

    @Test
    @DisplayName("Combat Suite: All 85+ concrete modules instantiate with unique IDs and Category.COMBAT")
    void testAllCombatModulesInstantiate() {
        List<Module> allCombatModules = CombatModuleCatalog.createAllInstances();
        assertThat(allCombatModules.size())
                .as("Combat module catalog must contain at least 85 modules")
                .isGreaterThanOrEqualTo(85);

        Set<String> uniqueIds = new HashSet<>();
        for (Module m : allCombatModules) {
            assertThat(m.getId())
                    .as("Module id cannot be blank")
                    .isNotBlank();
            assertThat(m.getId())
                    .as("Module id must be lowercase kebab-case: " + m.getId())
                    .isEqualTo(m.getId().toLowerCase());
            assertThat(m.getName())
                    .as("Module name cannot be blank for " + m.getId())
                    .isNotBlank();
            assertThat(m.getDescription())
                    .as("Module description cannot be null for " + m.getId())
                    .isNotNull();
            assertThat(m.getCategory())
                    .as("Module category must be Category.COMBAT for " + m.getId())
                    .isEqualTo(Category.COMBAT);
            assertThat(m.getKeybind())
                    .as("Module keybind cannot be null for " + m.getId())
                    .isNotNull();
            assertThat(m.getSettings())
                    .as("Module settings cannot be empty for " + m.getId())
                    .isNotEmpty();

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
    @DisplayName("Combat Suite: ModuleManager registers all catalog modules cleanly")
    void testModuleManagerRegistration() {
        EventBus bus = new EventBus();
        ModuleManager manager = new ModuleManager(bus);
        CombatModuleCatalog.registerAll(manager);

        assertThat(manager.getModules().size()).isGreaterThanOrEqualTo(85);
        assertThat(manager.getModulesByCategory(Category.COMBAT).size()).isGreaterThanOrEqualTo(85);
    }
}
