package com.ballsclient.core.module.movement;

import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.module.movement.aerial.*;
import com.ballsclient.core.module.movement.creative.*;
import com.ballsclient.core.module.movement.failsafe.*;
import com.ballsclient.core.module.movement.locomotion.*;
import com.ballsclient.core.module.movement.speed.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Central registry catalog for all 85+ (89 concrete) Movement & Locomotion modules.
 */
public final class MovementModuleCatalog {
    private static final List<Supplier<Module>> SUPPLIERS = new ArrayList<>();

    static {
        // --- 1. Locomotion & Ground Traversal (18 modules) ---
        register(AutoSprint::new);
        register(AutoJump::new);
        register(SafeWalk::new);
        register(Sneak::new);
        register(FastLadder::new);
        register(Dolphin::new);
        register(IceSpeed::new);
        register(SlipperyGround::new);
        register(InvertedControls::new);
        register(DirectionalSprint::new);
        register(AutoWalk::new);
        register(CobwebWalk::new);
        register(SlimeBounce::new);
        register(SoulSandSpeed::new);
        register(HoneyJump::new);
        register(TetherLocomotion::new);
        register(CrawlHelper::new);
        register(ScaffoldHelper::new);

        // --- 2. Aerial & Traversal Mechanics (18 modules) ---
        register(ElytraFly::new);
        register(AutoFireworkGlide::new);
        register(Jetpack::new);
        register(AirJump::new);
        register(Glide::new);
        register(HighJump::new);
        register(LongJump::new);
        register(WallClimb::new);
        register(BoatFly::new);
        register(AntiLevitation::new);
        register(LevitationFlight::new);
        register(Hover::new);
        register(TridentLaunch::new);
        register(WindChargeBooster::new);
        register(Parachute::new);
        register(ThermalUpdraft::new);
        register(HelicopterGlide::new);
        register(WingFlap::new);

        // --- 3. Failsafe & Environmental Mitigation (17 modules) ---
        register(NoFall::new);
        register(AntiVoid::new);
        register(WaterWalk::new);
        register(WebBypass::new);
        register(SlipPrevention::new);
        register(AutoFallReset::new);
        register(SafeLanding::new);
        register(KineticFailsafe::new);
        register(AntiCactus::new);
        register(AntiFire::new);
        register(AntiSuffocation::new);
        register(BorderFailsafe::new);
        register(PowderSnowBypass::new);
        register(BerryBushSafe::new);
        register(DamageTiltCompensation::new);
        register(MagmaBlockFailsafe::new);
        register(PortalLockFailsafe::new);

        // --- 4. Speed, Acceleration & Momentum (18 modules) ---
        register(BunnyHop::new);
        register(StrafeSpeed::new);
        register(GroundSpeed::new);
        register(EntitySpeed::new);
        register(Step::new);
        register(ReverseStep::new);
        register(SlowFall::new);
        register(FrictionModifier::new);
        register(FastFall::new);
        register(SprintBurst::new);
        register(YawStrafe::new);
        register(VelocityTuning::new);
        register(NoSlowdown::new);
        register(SpeedPotAmplifier::new);
        register(BoostPad::new);
        register(KineticSprint::new);
        register(SlopeSurfer::new);
        register(BrakePad::new);

        // --- 5. Creative & Novel Traversal Physics (18 modules) ---
        register(BouncyPhysics::new);
        register(SphereTrajectory::new);
        register(PendulumCamera::new);
        register(ZeroGMomentum::new);
        register(Spider::new);
        register(WallRun::new);
        register(ParkourHelper::new);
        register(Blink::new);
        register(PhaseDash::new);
        register(MomentumPreserver::new);
        register(GrapplingHook::new);
        register(AntiAFK::new);
        register(MagneticBoots::new);
        register(PortalSpeedJitter::new);
        register(HoverboardDrift::new);
        register(SuperJumpCharge::new);
        register(RicochetLeap::new);
        register(OrbitalAnchor::new);
    }

    private static void register(Supplier<Module> supplier) {
        SUPPLIERS.add(supplier);
    }

    public static void registerAll(ModuleManager manager) {
        if (manager == null) return;
        for (Supplier<Module> s : SUPPLIERS) {
            manager.register(s.get());
        }
    }

    public static List<Module> createAllInstances() {
        List<Module> list = new ArrayList<>(SUPPLIERS.size());
        for (Supplier<Module> s : SUPPLIERS) {
            list.add(s.get());
        }
        return Collections.unmodifiableList(list);
    }

    public static int getTotalModuleCount() {
        return SUPPLIERS.size();
    }

    private MovementModuleCatalog() {}
}
