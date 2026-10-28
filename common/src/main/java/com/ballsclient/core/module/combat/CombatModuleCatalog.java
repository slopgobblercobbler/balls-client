package com.ballsclient.core.module.combat;

import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.module.combat.aim.*;
import com.ballsclient.core.module.combat.defense.*;
import com.ballsclient.core.module.combat.tactical.*;
import com.ballsclient.core.module.combat.weapon.*;
import com.ballsclient.core.module.combat.automation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Central registry catalog for all 85+ (90 concrete) Combat & Tactical modules.
 */
public final class CombatModuleCatalog {
    private static final List<Supplier<Module>> SUPPLIERS = new ArrayList<>();

    static {
        register(ProjectilePrediction::new);
        register(BowAimAssist::new);
        register(TriggerBot::new);
        register(SmoothAim::new);
        register(CrosshairTargetTracker::new);
        register(RaycastHitboxViewer::new);
        register(DynamicFOVScale::new);
        register(TargetLeadPredictor::new);
        register(AuraAim::new);
        register(SilentAim::new);
        register(HeadshotTracker::new);
        register(HitboxExpander::new);
        register(ShieldPiercingAim::new);
        register(TargetLock::new);
        register(TridentAimAssist::new);
        register(CrossbowSniper::new);
        register(FishingRodAim::new);
        register(SplashPotionAim::new);
        register(SmartTotemFailsafe::new);
        register(AutoTotem::new);
        register(DangerHorizon::new);
        register(ArmorAlert::new);
        register(ShieldDisabler::new);
        register(ShieldBreaker::new);
        register(BlastProtectionEvaluator::new);
        register(AntiSurround::new);
        register(AutoShield::new);
        register(DamagePredictionRadar::new);
        register(CrystalFailsafe::new);
        register(AnchorDefense::new);
        register(TotemPopCounter::new);
        register(AntiKnockback::new);
        register(HoleESP::new);
        register(AutoTrapDefense::new);
        register(OffhandFallback::new);
        register(SonicBoomDefense::new);
        register(ArmorSwapperChords::new);
        register(VelocityTracker::new);
        register(PearlTracker::new);
        register(CrystalHelper::new);
        register(AnchorHelper::new);
        register(HitSound::new);
        register(ReachDisplay::new);
        register(KnockbackModifier::new);
        register(ThreatAssessor::new);
        register(CombatStamina::new);
        register(KillStreakTracker::new);
        register(SurroundHelper::new);
        register(HitVectorVisualizer::new);
        register(BowSpamHelper::new);
        register(VisualRangeAlerter::new);
        register(TargetStatsOverlay::new);
        register(WebTrapper::new);
        register(CombatLogDisconnector::new);
        register(AutoWeapon::new);
        register(Criticals::new);
        register(FastBow::new);
        register(AxeStunner::new);
        register(SweepAttackOptimizer::new);
        register(DurabilityPreserver::new);
        register(WeaponChargeTracker::new);
        register(MaceSmashHelper::new);
        register(WindChargeLauncher::new);
        register(FireAspectMaximizer::new);
        register(KnockbackComboStick::new);
        register(ReachTweak::new);
        register(RapidCrossbow::new);
        register(ShieldChargeIndicator::new);
        register(TridentLoyaltyRecall::new);
        register(AttackCooldownOverlay::new);
        register(DamageAttributeScaler::new);
        register(WeaponSwitchBuffer::new);
        register(AutoArmor::new);
        register(AutoPot::new);
        register(SoupAssist::new);
        register(RefillHelper::new);
        register(QuickOffhand::new);
        register(OffhandTotemCycle::new);
        register(TargetSelector::new);
        register(CombatMacroChord::new);
        register(AutoGapple::new);
        register(ArmorRepairOptimizer::new);
        register(QuickMend::new);
        register(FastEat::new);
        register(AutoObsidian::new);
        register(AutoAnchorRefill::new);
        register(AntiFriendAttack::new);
        register(ArmorDropPreventer::new);
        register(AutoWeaponReload::new);
        register(BedAuraTactical::new);
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

    private CombatModuleCatalog() {}
}
