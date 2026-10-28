package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Renders player coordinates, Nether ratio, chunk-relative offset, and facing direction.
 */
public class CoordinatesHUD extends Module {
    private final BooleanSetting nether = new BooleanSetting("Nether", "Show Nether relative coordinates (X/8, Z/8)", true);
    private final BooleanSetting chunkRel = new BooleanSetting("ChunkRel", "Show sub-chunk relative offset (0-15)", false);
    private final BooleanSetting showBiome = new BooleanSetting("ShowBiome", "Show current biome name", true);
    private final ColorSetting color = new ColorSetting("Color", "Text color for coordinates readout", 0xFFFFFFFF);

    private double posX;
    private double posY;
    private double posZ;
    private String facing = "North";
    private String biomeName = "Plains";

    public CoordinatesHUD() {
        super("coordinates-hud", "Coordinates HUD", "Displays XYZ coordinates, facing, and Nether coordinates", Category.RENDER);
        addSettings(nether, chunkRel, showBiome, color);
    }

    public void updatePosition(double x, double y, double z, float yaw, String biome) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.biomeName = biome != null ? biome : "Plains";
        float normalizedYaw = (yaw % 360 + 360) % 360;
        if (normalizedYaw >= 315 || normalizedYaw < 45) {
            this.facing = "South [+Z]";
        } else if (normalizedYaw >= 45 && normalizedYaw < 135) {
            this.facing = "West [-X]";
        } else if (normalizedYaw >= 135 && normalizedYaw < 225) {
            this.facing = "North [-Z]";
        } else {
            this.facing = "East [+X]";
        }
    }

    public String formatPrimaryCoordinates() {
        return String.format("XYZ: %.1f / %.1f / %.1f (%s)", posX, posY, posZ, facing);
    }

    public String formatNetherCoordinates() {
        return String.format("Nether: %.1f / %.1f / %.1f", posX / 8.0, posY, posZ / 8.0);
    }

    public String formatChunkRelative() {
        int cx = ((int) Math.floor(posX)) & 15;
        int cy = ((int) Math.floor(posY)) & 15;
        int cz = ((int) Math.floor(posZ)) & 15;
        return String.format("Chunk Rel: %d, %d, %d", cx, cy, cz);
    }

    public double getPosX() { return posX; }
    public double getPosY() { return posY; }
    public double getPosZ() { return posZ; }
    public String getFacing() { return facing; }
    public String getBiomeName() { return biomeName; }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render pass: coordinates HUD renders text string via PAL adapter
    }
}
