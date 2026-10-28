package com.ballsclient.core.module.render;

import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.module.render.camera.*;
import com.ballsclient.core.module.render.cosmetic.*;
import com.ballsclient.core.module.render.esp.*;
import com.ballsclient.core.module.render.hud.*;
import com.ballsclient.core.module.render.lighting.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Central registry catalog for all 100+ Render & Visuals modules.
 */
public final class RenderModuleCatalog {
    private static final List<Supplier<Module>> SUPPLIERS = new ArrayList<>();

    static {
        // --- 1. HUD & Tactical Overlays (25) ---
        register(CoordinatesHUD::new);
        register(ArmorDurabilityHUD::new);
        register(StatusEffectsHUD::new);
        register(SpatialRadar2D::new);
        register(TargetHUD::new);
        register(KeystrokesHUD::new);
        register(FPSHUD::new);
        register(ServerTPSHUD::new);
        register(PingHUD::new);
        register(MemoryHUD::new);
        register(EntityCounterHUD::new);
        register(CompassHUD::new);
        register(ActiveModulesListHUD::new);
        register(SpeedometerHUD::new);
        register(BiomeWeatherHUD::new);
        register(CrosshairCustomizer::new);
        register(HotbarCustomizer::new);
        register(ChatHUDCustomizer::new);
        register(ScoreboardHUD::new);
        register(BossBarHUD::new);
        register(ClockHUD::new);
        register(PotionHUD::new);
        register(TabListTweaker::new);
        register(BiomeHUD::new);
        register(HitMarkerOverlay::new);

        // --- 2. 3D World Overlays, ESPs & Tracers (28) ---
        register(EntityESP::new);
        register(StorageESP::new);
        register(Tracers::new);
        register(Breadcrumbs::new);
        register(Waypoints3D::new);
        register(BlockHighlight::new);
        register(ChunkBorders::new);
        register(VoidESP::new);
        register(ThermalVisionESP::new);
        register(SonarEchoLocator::new);
        register(Nametags3D::new);
        register(ItemESP::new);
        register(HoleESP::new);
        register(ArrowTracers::new);
        register(SpawnerESP::new);
        register(OreESP::new);
        register(OreHighlighter::new);
        register(PortalESP::new);
        register(BeaconBeamVisualizer::new);
        register(StructureBoundaries::new);
        register(PlayerHitboxViewer::new);
        register(DamageIndicatorParticles::new);
        register(SoundLocator3D::new);
        register(DeathPointMarker::new);
        register(PlayerGhosts::new);
        register(TotemPopVisualizer::new);
        register(ShulkerColorESP::new);
        register(SubChunkMeshAudit::new);

        // --- 3. Lighting, Atmosphere & Skybox (26) ---
        register(Fullbright::new);
        register(TimeChanger::new);
        register(WeatherColorChanger::new);
        register(CosmicSkybox::new);
        register(AmbienceColor::new);
        register(FogCustomizer::new);
        register(LowFire::new);
        register(AntiBlind::new);
        register(NightVisionEnhancer::new);
        register(UnderwaterVision::new);
        register(LavaVision::new);
        register(AuroraBorealis::new);
        register(StarfieldDensity::new);
        register(CustomSunMoon::new);
        register(DimensionAtmosphere::new);
        register(ShadowColor::new);
        register(LightLevelOverlay::new);
        register(MobSpawnHeatmap::new);
        register(VignetteRemover::new);
        register(PumpkinOverlayRemover::new);
        register(SunSizeModifier::new);
        register(MoonPhaseChanger::new);
        register(CustomGammaRamp::new);
        register(ShadowIntensityModifier::new);
        register(CloudHeightChanger::new);
        register(RainPuddleReflections::new);

        // --- 4. Camera, Perspective & Motion (26) ---
        register(SmoothZoom::new);
        register(Freecam::new);
        register(MotionBlur::new);
        register(CameraClip::new);
        register(CustomFOV::new);
        register(ThirdPersonDistance::new);
        register(ViewBobbingModifier::new);
        register(NoHurtCam::new);
        register(Perspective360::new);
        register(IsometricView::new);
        register(CinematicCameraEasing::new);
        register(ViewModelModifier::new);
        register(SmallShield::new);
        register(LowShield::new);
        register(OldCombatAnimations::new);
        register(HitColorCustomizer::new);
        register(PopUpDamageAnimation::new);
        register(DeathAnimationCustomizer::new);
        register(SpinBotVisual::new);
        register(UpsideDownPlayer::new);
        register(MinimalHand::new);
        register(CinematicCamera::new);
        register(AspectRationChanger::new);
        register(DynamicFovToggle::new);
        register(SmoothLookEasing::new);
        register(RollCameraEffect::new);

        // --- 5. Entity, Item & Cosmetic Visuals (28) ---
        register(RainbowEnchant::new);
        register(ItemPhysics::new);
        register(GlowingItems::new);
        register(CustomCapeRenderer::new);
        register(WingsCosmetic::new);
        register(HaloCosmetic::new);
        register(PetCosmetic::new);
        register(ShieldBannerOverlay::new);
        register(CustomArmorTrimGlow::new);
        register(GliderCosmetics::new);
        register(HeadRotator::new);
        register(SkeletonESP::new);
        register(ArmorBreakParticle::new);
        register(CrystalChams::new);
        register(PlayerChams::new);
        register(HandChams::new);
        register(BlockBreakVisualizer::new);
        register(ChestSearchHighlight::new);
        register(SoundWavesVisualizer::new);
        register(ChromaWaveformHUD::new);
        register(DamageParticleSpawner::new);
        register(PopChamsEffect::new);
        register(TrailParticles::new);
        register(SoundWaveVisualizer::new);
        register(SelfGlowCosmetic::new);
        register(HatCosmetic::new);
        register(ShieldBannerCustomizer::new);
        register(ParticleMultiplier::new);
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

    private RenderModuleCatalog() {}
}
