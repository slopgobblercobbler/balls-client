package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * VeinMinerHelper — Flood-fill graph search felling contiguous ore veins in one sequence.
 */
public class VeinMinerHelper extends Module {
    private final NumberSetting maxVeinSize = new NumberSetting("MaxVeinSize", "Maximum blocks excavated in single vein", 64.0, 1.0, 128.0, 1.0);
    private final BooleanSetting matchExactBlock = new BooleanSetting("MatchExactBlock", "Only mine exact matching block id", true);
    private final BooleanSetting diagonalSearch = new BooleanSetting("DiagonalSearch", "Search diagonal corner block neighbors", true);

    public VeinMinerHelper() {
        super("vein-miner-helper", "Vein Miner Helper", "Flood-fill graph search felling contiguous ore veins in one sequence", Category.PLAYER);
        addSettings(maxVeinSize, matchExactBlock, diagonalSearch);
    }

    public NumberSetting getMaxVeinSize() {
        return maxVeinSize;
    }

    public BooleanSetting getMatchExactBlock() {
        return matchExactBlock;
    }

    public BooleanSetting getDiagonalSearch() {
        return diagonalSearch;
    }

    public static int getVeinLimit(double maxVeinSize) {
        return (int) maxVeinSize;
    }

    public static boolean isConnectedVeinBlock(int x1, int y1, int z1, int x2, int y2, int z2, boolean allowDiagonals) {
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        int dz = Math.abs(z1 - z2);
        if (allowDiagonals) {
            return Math.max(dx, Math.max(dy, dz)) <= 1;
        } else {
            return (dx + dy + dz) == 1;
        }
    }
}
