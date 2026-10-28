package com.ballsclient.core.module.player;

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

@DisplayName("Player & World Suite Registration & Invariants Test")
class PlayerModuleRegistrationTest {

    @Test
    @DisplayName("Player Suite: All 85+ concrete modules instantiate with unique IDs, Category.PLAYER, and settings")
    void testAllPlayerModulesInstantiate() {
        List<Module> allModules = PlayerModuleCatalog.createAllInstances();
        assertThat(allModules.size())
                .as("Player module catalog must contain at least 85 modules")
                .isGreaterThanOrEqualTo(85)
                .isEqualTo(90);

        Set<String> uniqueIds = new HashSet<>();
        for (Module m : allModules) {
            assertThat(m.getId())
                    .as("Module ID must be non-blank lowercase kebab-case")
                    .isNotBlank()
                    .matches("^[a-z0-9-]+$");
            assertThat(m.getName()).as("Module name must not be blank").isNotBlank();
            assertThat(m.getDescription()).as("Module description must not be null").isNotNull();
            assertThat(m.getCategory()).as("Module category must be Category.PLAYER").isEqualTo(Category.PLAYER);
            assertThat(m.getKeybind()).as("Module keybind must not be null").isNotNull();
            assertThat(m.getSettings()).as("Module settings list must not be empty").isNotEmpty();

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
    @DisplayName("Player Suite: ModuleManager registers all 90 catalog modules cleanly")
    void testModuleManagerRegistration() {
        EventBus bus = new EventBus();
        ModuleManager manager = new ModuleManager(bus);
        PlayerModuleCatalog.registerAll(manager);

        assertThat(manager.getModules().size()).isGreaterThanOrEqualTo(85).isEqualTo(90);
        assertThat(manager.getModulesByCategory(Category.PLAYER).size()).isGreaterThanOrEqualTo(85).isEqualTo(90);
    }

    @Test
    @DisplayName("Player Suite: Subpackage partitioning distributes exactly 18 modules per package")
    void testSubpackageModuleDistribution() {
        List<Module> allModules = PlayerModuleCatalog.createAllInstances();
        int worldCount = 0;
        int interactionCount = 0;
        int miningCount = 0;
        int utilityCount = 0;
        int automationCount = 0;

        for (Module m : allModules) {
            String pkg = m.getClass().getPackage().getName();
            if (pkg.endsWith(".world")) worldCount++;
            else if (pkg.endsWith(".interaction")) interactionCount++;
            else if (pkg.endsWith(".mining")) miningCount++;
            else if (pkg.endsWith(".utility")) utilityCount++;
            else if (pkg.endsWith(".automation")) automationCount++;
        }

        assertThat(worldCount).as("World subpackage must contain exactly 18 modules").isEqualTo(18);
        assertThat(interactionCount).as("Interaction subpackage must contain exactly 18 modules").isEqualTo(18);
        assertThat(miningCount).as("Mining subpackage must contain exactly 18 modules").isEqualTo(18);
        assertThat(utilityCount).as("Utility subpackage must contain exactly 18 modules").isEqualTo(18);
        assertThat(automationCount).as("Automation subpackage must contain exactly 18 modules").isEqualTo(18);
    }
}
