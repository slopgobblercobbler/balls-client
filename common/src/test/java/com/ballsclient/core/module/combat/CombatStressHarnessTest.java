package com.ballsclient.core.module.combat;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.module.combat.aim.*;
import com.ballsclient.core.module.combat.defense.*;
import com.ballsclient.core.module.combat.tactical.*;
import com.ballsclient.core.module.combat.weapon.*;
import com.ballsclient.core.module.combat.automation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("Combat Suite Empirical Adversarial Stress Harness")
class CombatStressHarnessTest {

    // =========================================================================
    // 1. MATHEMATICAL EXTREME INPUTS: BALLISTIC TRAJECTORY SIMULATION
    // =========================================================================

    @Test
    @DisplayName("Ballistics Stress: Boundary launch speeds v0 = 0.0 and v0 = 100.0")
    void testBallisticsBoundaryLaunchSpeeds() {
        ProjectilePrediction predictor = new ProjectilePrediction();

        // Subcase 1: Zero launch speed (v0 = 0.0)
        List<ProjectilePrediction.TrajectoryPoint> zeroSpeedPath = predictor.simulateTrajectory(
                100.0, 64.0, -200.0, 0.0, 0.0, 0.0, 0.05, 0.99, 50
        );
        assertThat(zeroSpeedPath).hasSize(50);

        for (int i = 0; i < zeroSpeedPath.size(); i++) {
            ProjectilePrediction.TrajectoryPoint pt = zeroSpeedPath.get(i);
            // X and Z must remain strictly stationary
            assertThat(pt.x).as("Stationary X at tick " + i).isEqualTo(100.0);
            assertThat(pt.z).as("Stationary Z at tick " + i).isEqualTo(-200.0);
            assertThat(pt.vx).isEqualTo(0.0);
            assertThat(pt.vz).isEqualTo(0.0);
            assertThat(Double.isNaN(pt.y)).isFalse();
            assertThat(Double.isInfinite(pt.y)).isFalse();
            // Y must remain <= previous and accelerate downwards monotonically for i > 1
            if (i == 1) {
                assertThat(pt.y).as("Stationary Y on tick 1 due to v0=0").isEqualTo(zeroSpeedPath.get(0).y);
            } else if (i > 1) {
                assertThat(pt.y).as("Monotonic downward drop at tick " + i)
                        .isLessThan(zeroSpeedPath.get(i - 1).y);
            }
        }

        // Subcase 2: Extreme hyper-velocity (v0 = 100.0)
        List<ProjectilePrediction.TrajectoryPoint> hyperSpeedPath = predictor.simulateTrajectory(
                0.0, 100.0, 0.0, -45.0, 45.0, 100.0, 0.05, 0.99, 100
        );
        assertThat(hyperSpeedPath).hasSize(100);

        for (int i = 0; i < hyperSpeedPath.size(); i++) {
            ProjectilePrediction.TrajectoryPoint pt = hyperSpeedPath.get(i);
            assertThat(Double.isNaN(pt.x)).isFalse();
            assertThat(Double.isNaN(pt.y)).isFalse();
            assertThat(Double.isNaN(pt.z)).isFalse();
            assertThat(Double.isInfinite(pt.x)).isFalse();
            assertThat(Double.isInfinite(pt.y)).isFalse();
            assertThat(Double.isInfinite(pt.z)).isFalse();

            // Horizontal velocities must decelerate exponentially under drag 0.99
            if (i > 1) {
                ProjectilePrediction.TrajectoryPoint prev = hyperSpeedPath.get(i - 1);
                assertThat(Math.abs(pt.vx)).isLessThan(Math.abs(prev.vx));
                assertThat(Math.abs(pt.vz)).isLessThan(Math.abs(prev.vz));
            }
        }
    }

    @Test
    @DisplayName("Ballistics Stress: Vertical boundary launch angles (theta = 90 deg down, theta = -90 deg up)")
    void testBallisticsVerticalBoundaryLaunchAngles() {
        ProjectilePrediction predictor = new ProjectilePrediction();
        double v0 = 3.0;
        double gravity = 0.05;
        double drag = 0.99;

        // Subcase 1: Pitch = 90.0 degrees (Straight DOWN in Minecraft coordinate system)
        List<ProjectilePrediction.TrajectoryPoint> straightDownPath = predictor.simulateTrajectory(
                0.0, 200.0, 0.0, 90.0, 0.0, v0, gravity, drag, 60
        );
        assertThat(straightDownPath).hasSize(60);

        for (int i = 0; i < straightDownPath.size(); i++) {
            ProjectilePrediction.TrajectoryPoint pt = straightDownPath.get(i);
            // X and Z displacements must be virtually zero (< 1e-12)
            assertThat(Math.abs(pt.x)).as("Zero horizontal drift X at tick " + i).isLessThan(1e-12);
            assertThat(Math.abs(pt.z)).as("Zero horizontal drift Z at tick " + i).isLessThan(1e-12);
            // Initial vy must equal -v0
            if (i == 0) {
                assertThat(pt.vy).isCloseTo(-v0, within(1e-6));
            }
            // Continuous downward drop
            assertThat(pt.y).isLessThanOrEqualTo(200.0);
        }

        // Subcase 2: Pitch = -90.0 degrees (Straight UP in Minecraft coordinate system)
        List<ProjectilePrediction.TrajectoryPoint> straightUpPath = predictor.simulateTrajectory(
                0.0, 10.0, 0.0, -90.0, 0.0, v0, gravity, drag, 60
        );
        assertThat(straightUpPath).hasSize(60);

        double apexY = 10.0;
        for (int i = 0; i < straightUpPath.size(); i++) {
            ProjectilePrediction.TrajectoryPoint pt = straightUpPath.get(i);
            assertThat(Math.abs(pt.x)).as("Zero horizontal drift X at tick " + i).isLessThan(1e-12);
            assertThat(Math.abs(pt.z)).as("Zero horizontal drift Z at tick " + i).isLessThan(1e-12);
            if (pt.y > apexY) apexY = pt.y;
            if (i == 0) {
                assertThat(pt.vy).isCloseTo(v0, within(1e-6));
            }
        }
        // Projectile should reach an apex significantly above initial elevation
        assertThat(apexY).isGreaterThan(30.0);
    }

    @Test
    @DisplayName("Ballistics Stress: Boundary yaw rotations and 0-tick simulation")
    void testBallisticsYawRotationsAndEdgeParameters() {
        ProjectilePrediction predictor = new ProjectilePrediction();

        // 0 ticks should safely return an empty list
        List<ProjectilePrediction.TrajectoryPoint> emptyPath = predictor.simulateTrajectory(
                0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.05, 0.99, 0
        );
        assertThat(emptyPath).isEmpty();

        // 360 degree full yaw rotation sweep
        for (double yaw = -180.0; yaw <= 180.0; yaw += 45.0) {
            List<ProjectilePrediction.TrajectoryPoint> path = predictor.simulateTrajectory(
                    0.0, 10.0, 0.0, 0.0, yaw, 3.0, 0.05, 0.99, 10
            );
            assertThat(path).hasSize(10);
            for (ProjectilePrediction.TrajectoryPoint pt : path) {
                assertThat(Double.isNaN(pt.x)).isFalse();
                assertThat(Double.isNaN(pt.z)).isFalse();
            }
        }
    }

    // =========================================================================
    // 2. MATHEMATICAL EXTREME INPUTS: ARMOR & BLAST DAMAGE CALCULATION
    // =========================================================================

    @Test
    @DisplayName("Armor Stress: Extreme damage values (D = 0.0, D = 10^6) and extreme armor points")
    void testArmorAndBlastProtectionExtremes() {
        BlastProtectionEvaluator evaluator = new BlastProtectionEvaluator();

        // Subcase 1: D = 0.0 (Zero damage)
        double zeroDmg = evaluator.calculateNetBlastDamage(0.0, 20.0, 8.0, 16);
        assertThat(zeroDmg).isEqualTo(0.0);

        // Subcase 2: Extreme damage D = 1,000,000.0 with zero defense & zero toughness
        double massiveDmgUnarmored = evaluator.calculateNetBlastDamage(1_000_000.0, 0.0, 0.0, 0);
        assertThat(massiveDmgUnarmored).isEqualTo(1_000_000.0);

        // Subcase 3: Extreme damage D = 1,000,000.0 with maximum vanilla netherite (defense = 20, toughness = 12)
        // With massive damage, defense factor is pushed to minimum clamp bound: defense / 5.0 = 4.0
        // Post armor = 1,000,000 * (1 - 4/25) = 840,000
        // BlastProt EPF = 16 -> reduction = 16 / 25 = 64% -> net = 840,000 * 0.36 = 302,400.0
        double massiveDmgNetherite = evaluator.calculateNetBlastDamage(1_000_000.0, 20.0, 12.0, 16);
        assertThat(massiveDmgNetherite).isCloseTo(302_400.0, within(1e-4));

        // Subcase 4: Extreme armor points: defense = 100.0, toughness = 100.0
        // defense / 5.0 = 20.0, so defenseFactor clamps strictly at 20.0 (vanilla maximum 80% reduction)
        // Post-armor = rawDmg * (1 - 20/25) = rawDmg * 0.20
        // With EPF = 20 (max 80% enchantment reduction):
        // Net damage = rawDmg * 0.20 * 0.20 = rawDmg * 0.04 (96% total reduction)
        double extremeArmorDmg = evaluator.calculateNetBlastDamage(1_000.0, 100.0, 100.0, 20);
        assertThat(extremeArmorDmg).isCloseTo(40.0, within(1e-4));

        // Subcase 5: EPF clamp testing: negative EPF (-10) and oversized EPF (+100)
        double negEpfDmg = evaluator.calculateNetBlastDamage(100.0, 20.0, 0.0, -10);
        double zeroEpfDmg = evaluator.calculateNetBlastDamage(100.0, 20.0, 0.0, 0);
        assertThat(negEpfDmg).isEqualTo(zeroEpfDmg);

        double maxEpfDmg = evaluator.calculateNetBlastDamage(100.0, 20.0, 0.0, 20);
        double overEpfDmg = evaluator.calculateNetBlastDamage(100.0, 20.0, 0.0, 100);
        assertThat(overEpfDmg).isEqualTo(maxEpfDmg);

        // Subcase 6: Negative raw damage should clamp to 0.0
        double negativeDmg = evaluator.calculateNetBlastDamage(-50.0, 20.0, 8.0, 16);
        assertThat(negativeDmg).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Armor Stress: ArmorAlert durability edge cases (zero max, broken armor, over-repaired)")
    void testArmorAlertDurabilityExtremes() {
        ArmorAlert alert = new ArmorAlert();

        // Zero or negative max durability should not throw ArithmeticException / divide-by-zero
        assertThat(alert.isArmorCritical(0, 0, 20.0)).isFalse();
        assertThat(alert.isArmorCritical(10, -5, 20.0)).isFalse();

        // 0 durability remaining -> critical
        assertThat(alert.isArmorCritical(0, 500, 20.0)).isTrue();

        // Negative durability remaining -> critical
        assertThat(alert.isArmorCritical(-1, 500, 20.0)).isTrue();

        // Exact threshold boundary
        assertThat(alert.isArmorCritical(100, 500, 20.0)).isTrue();
        assertThat(alert.isArmorCritical(101, 500, 20.0)).isFalse();

        // Over-repaired durability (e.g. 600 / 500)
        assertThat(alert.isArmorCritical(600, 500, 20.0)).isFalse();
    }

    @Test
    @DisplayName("Combat Math: Mace Smash kinetic fall distance boundary scaling")
    void testMaceSmashDamageScaling() {
        MaceSmashHelper helper = new MaceSmashHelper();

        // Negative and zero fall distance produces 0 bonus damage
        assertThat(helper.calculateMaceBonusDamage(0.0)).isEqualTo(0.0);
        assertThat(helper.calculateMaceBonusDamage(-15.0)).isEqualTo(0.0);

        // Tier 1: 3 blocks -> 3 * 3 = 9.0
        assertThat(helper.calculateMaceBonusDamage(3.0)).isCloseTo(9.0, within(1e-4));

        // Tier 2: 8 blocks -> 9 + 5 * 2 = 19.0
        assertThat(helper.calculateMaceBonusDamage(8.0)).isCloseTo(19.0, within(1e-4));

        // Tier 3: 20 blocks -> 19 + 12 * 1 = 31.0
        assertThat(helper.calculateMaceBonusDamage(20.0)).isCloseTo(31.0, within(1e-4));

        // Extreme fall distance: 10,000 blocks
        assertThat(helper.calculateMaceBonusDamage(10000.0)).isCloseTo(19.0 + (10000.0 - 8.0), within(1e-4));
    }

    // =========================================================================
    // 3. MATHEMATICAL EXTREME INPUTS: TOTEM FAILSAFE LOGIC & ABSORPTION HEARTS
    // =========================================================================

    @Test
    @DisplayName("Totem Failsafe Stress: Negative HP, zero HP, massive incoming damage, absorption combinations")
    void testTotemFailsafeMathematicalExtremes() {
        SmartTotemFailsafe failsafe = new SmartTotemFailsafe();
        double threshold = 6.0;
        double safetyMargin = 2.0;

        // Subcase 1: Negative health (-10.0) -> must trigger failsafe
        assertThat(failsafe.shouldEquipTotem(-10.0, 0.0, 0.0, threshold, safetyMargin, false)).isTrue();

        // Subcase 2: Zero health (0.0) -> must trigger failsafe
        assertThat(failsafe.shouldEquipTotem(0.0, 0.0, 0.0, threshold, safetyMargin, false)).isTrue();

        // Subcase 3: Massive incoming damage (1,000,000.0) -> must trigger failsafe
        assertThat(failsafe.shouldEquipTotem(20.0, 0.0, 1_000_000.0, threshold, safetyMargin, false)).isTrue();

        // Subcase 4: Massive incoming damage with massive absorption (e.g. 500 absorption hearts)
        // incoming (1,000,000) > effectiveHp (20 + 500 = 520) -> must trigger
        assertThat(failsafe.shouldEquipTotem(20.0, 500.0, 1_000_000.0, threshold, safetyMargin, false)).isTrue();

        // Subcase 5: Massive absorption mitigates damage safely
        // effectiveHp = 20.0 + 100.0 = 120.0. safetyMargin = 2.0 -> threshold is 118.0.
        // incoming = 80.0 -> player will survive with 40 HP -> no totem waste
        assertThat(failsafe.shouldEquipTotem(20.0, 100.0, 80.0, threshold, safetyMargin, false)).isFalse();

        // Subcase 6: Offhand already holding totem -> must return FALSE even under lethal blow
        assertThat(failsafe.shouldEquipTotem(2.0, 0.0, 500.0, threshold, safetyMargin, true)).isFalse();

        // Subcase 7: Critical red health with high absorption
        // In vanilla, if red health is <= threshold (e.g. 4.0 <= 6.0), failsafe triggers emergency swap
        assertThat(failsafe.shouldEquipTotem(4.0, 16.0, 0.0, threshold, safetyMargin, false)).isTrue();

        // Subcase 8: Exact boundary condition
        // currentHealth = 20.0, absorption = 0.0 -> effectiveHp = 20.0
        // safetyMargin = 2.0 -> (effectiveHp - safetyMargin) = 18.0
        // incoming = 18.0 -> triggers
        assertThat(failsafe.shouldEquipTotem(20.0, 0.0, 18.0, threshold, safetyMargin, false)).isTrue();
        // incoming = 17.99 -> does not trigger
        assertThat(failsafe.shouldEquipTotem(20.0, 0.0, 17.99, threshold, safetyMargin, false)).isFalse();
    }

    // =========================================================================
    // 4. CONCURRENCY STRESS: 90 MODULES REGISTRATION & LOOKUP IN MODULEMANAGER
    // =========================================================================

    @Test
    @DisplayName("Concurrency Stress: Multi-threaded registration, lookup, and iteration of 90 modules")
    void testModuleManagerMultiThreadedConcurrency() throws Exception {
        int threadCount = 16;
        int operationsPerThread = 5000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        EventBus eventBus = new EventBus();
        ModuleManager manager = new ModuleManager(eventBus);

        // Pre-register all 90 modules
        CombatModuleCatalog.registerAll(manager);
        List<Module> sampleModules = CombatModuleCatalog.createAllInstances();
        assertThat(sampleModules).hasSizeGreaterThanOrEqualTo(85);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicBoolean errorDetected = new AtomicBoolean(false);
        AtomicInteger totalQueries = new AtomicInteger(0);
        List<Throwable> exceptions = new CopyOnWriteArrayList<>();

        Random random = new Random(42);

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    startLatch.await(); // Synchronized starting gun

                    for (int op = 0; op < operationsPerThread; op++) {
                        int action = (threadId + op) % 5;
                        switch (action) {
                            case 0: {
                                // Lookup by string ID
                                Module pick = sampleModules.get(op % sampleModules.size());
                                Module retrieved = manager.getModule(pick.getId());
                                if (retrieved == null) {
                                    throw new IllegalStateException("Module not found by ID: " + pick.getId());
                                }
                                break;
                            }
                            case 1: {
                                // Lookup by Class
                                ProjectilePrediction pp = manager.getModule(ProjectilePrediction.class);
                                if (pp == null) {
                                    throw new IllegalStateException("ProjectilePrediction not found by class");
                                }
                                SmartTotemFailsafe st = manager.getModule(SmartTotemFailsafe.class);
                                if (st == null) {
                                    throw new IllegalStateException("SmartTotemFailsafe not found by class");
                                }
                                break;
                            }
                            case 2: {
                                // Query category modules and iterate
                                List<Module> combatMods = manager.getModulesByCategory(Category.COMBAT);
                                if (combatMods.size() < 85) {
                                    throw new IllegalStateException("Insufficient combat modules: " + combatMods.size());
                                }
                                int count = 0;
                                for (Module m : combatMods) {
                                    if (m != null) count++;
                                }
                                if (count < 85) {
                                    throw new IllegalStateException("Null module in category iteration");
                                }
                                break;
                            }
                            case 3: {
                                // Full collection iteration
                                Collection<Module> all = manager.getModules();
                                int count = 0;
                                for (Module m : all) {
                                    if (m != null) count++;
                                }
                                if (count < 85) {
                                    throw new IllegalStateException("Insufficient modules in all collection: " + count);
                                }
                                break;
                            }
                            case 4: {
                                // Concurrent registration attempt of duplicate or fresh module
                                Module candidate = sampleModules.get(op % sampleModules.size());
                                manager.register(candidate); // Should be safely ignored or deduplicated
                                break;
                            }
                        }
                        totalQueries.incrementAndGet();
                    }
                } catch (Throwable ex) {
                    errorDetected.set(true);
                    exceptions.add(ex);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        // Fire start trigger
        startLatch.countDown();

        // Await completion with 30s timeout
        boolean finished = doneLatch.await(30, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertThat(finished).as("All concurrent tasks must finish within timeout").isTrue();
        assertThat(exceptions).as("Zero exceptions occurred during multi-threaded operations").isEmpty();
        assertThat(errorDetected.get()).isFalse();
        assertThat(totalQueries.get()).isEqualTo(threadCount * operationsPerThread);

        // Verify final state of ModuleManager
        assertThat(manager.getModules().size()).isEqualTo(sampleModules.size());
        assertThat(manager.getModulesByCategory(Category.COMBAT).size()).isEqualTo(sampleModules.size());
    }

    @Test
    @DisplayName("Concurrency Stress: Simultaneous multi-threaded manager creation and catalog registration")
    void testConcurrentCatalogRegistrationAcrossManagers() throws Exception {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<Throwable> failures = new CopyOnWriteArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    EventBus bus = new EventBus();
                    ModuleManager mm = new ModuleManager(bus);
                    CombatModuleCatalog.registerAll(mm);

                    assertThat(mm.getModules()).hasSizeGreaterThanOrEqualTo(85);
                    assertThat(mm.getModulesByCategory(Category.COMBAT)).hasSizeGreaterThanOrEqualTo(85);

                    // Test all module lookups
                    for (Module m : CombatModuleCatalog.createAllInstances()) {
                        assertThat(mm.getModule(m.getId())).isNotNull();
                    }
                } catch (Throwable t) {
                    failures.add(t);
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean done = latch.await(20, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertThat(done).isTrue();
        assertThat(failures).isEmpty();
    }
}
