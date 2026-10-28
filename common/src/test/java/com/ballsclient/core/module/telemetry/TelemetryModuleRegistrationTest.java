package com.ballsclient.core.module.telemetry;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.setting.Setting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Network & Telemetry Suite Registration & Invariant Test")
class TelemetryModuleRegistrationTest {

    @Test
    @DisplayName("Telemetry Suite: Exactly 50 concrete modules instantiate with unique IDs and Category.NETWORK")
    void testAllTelemetryModulesInstantiate() {
        List<Module> allModules = TelemetryModuleCatalog.createAllInstances();
        assertThat(allModules.size())
                .as("Telemetry module catalog must contain exactly 50 modules")
                .isEqualTo(50);

        Set<String> uniqueIds = new HashSet<>();
        Map<String, Integer> subpackageCounts = new HashMap<>();

        for (Module m : allModules) {
            assertThat(m.getId())
                    .as("Module ID cannot be blank")
                    .isNotBlank();
            assertThat(m.getId())
                    .as("Module ID must be lowercase kebab-case: %s", m.getId())
                    .isEqualTo(m.getId().toLowerCase(Locale.ROOT));
            assertThat(m.getId())
                    .as("Module ID must not contain spaces or underscores: %s", m.getId())
                    .doesNotContain(" ", "_");
            assertThat(m.getName())
                    .as("Module name cannot be blank for %s", m.getId())
                    .isNotBlank();
            assertThat(m.getDescription())
                    .as("Module description cannot be null for %s", m.getId())
                    .isNotNull();
            assertThat(m.getCategory())
                    .as("Module category must be Category.NETWORK for %s", m.getId())
                    .isEqualTo(Category.NETWORK);
            assertThat(m.getKeybind())
                    .as("Module keybind cannot be null for %s", m.getId())
                    .isNotNull();
            assertThat(m.getSettings())
                    .as("Module settings cannot be empty for %s", m.getId())
                    .isNotEmpty();

            Set<String> settingNames = new HashSet<>();
            for (Setting<?> s : m.getSettings()) {
                assertThat(s.getName()).isNotBlank();
                assertThat(settingNames.add(s.getName().toLowerCase(Locale.ROOT)))
                        .as("Duplicate setting '%s' in module '%s'", s.getName(), m.getId())
                        .isTrue();
            }

            assertThat(uniqueIds.add(m.getId()))
                    .as("Duplicate module ID found: %s", m.getId())
                    .isTrue();

            String pkg = m.getClass().getPackage().getName();
            String subpkg = pkg.substring(pkg.lastIndexOf('.') + 1);
            subpackageCounts.put(subpkg, subpackageCounts.getOrDefault(subpkg, 0) + 1);
        }

        // Verify symmetrical 10-module partition across 5 subpackages
        assertThat(subpackageCounts.get("packet"))
                .as("packet subpackage must contain exactly 10 modules")
                .isEqualTo(10);
        assertThat(subpackageCounts.get("ping"))
                .as("ping subpackage must contain exactly 10 modules")
                .isEqualTo(10);
        assertThat(subpackageCounts.get("tps"))
                .as("tps subpackage must contain exactly 10 modules")
                .isEqualTo(10);
        assertThat(subpackageCounts.get("bandwidth"))
                .as("bandwidth subpackage must contain exactly 10 modules")
                .isEqualTo(10);
        assertThat(subpackageCounts.get("diagnostics"))
                .as("diagnostics subpackage must contain exactly 10 modules")
                .isEqualTo(10);
    }

    @Test
    @DisplayName("Telemetry Suite: ModuleManager registers all 50 catalog modules cleanly")
    void testModuleManagerRegistration() {
        EventBus bus = new EventBus();
        ModuleManager manager = new ModuleManager(bus);
        TelemetryModuleCatalog.registerAll(manager);

        assertThat(manager.getModules().size()).isEqualTo(50);
        assertThat(manager.getModulesByCategory(Category.NETWORK).size()).isEqualTo(50);
        assertThat(TelemetryModuleCatalog.getTotalModuleCount()).isEqualTo(50);
    }
}
