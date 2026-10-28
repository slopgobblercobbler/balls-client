package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * FastBreakSequencer — Orchestrates optimal block break orders in excavation cuboids to prevent falling sand.
 */
public class FastBreakSequencer extends Module {
    public record BlockPos(int x, int y, int z) {}

    private final ModeSetting sortingAlgorithm = new ModeSetting("SortingAlgorithm", "Break prioritization strategy", "TopDown", "TopDown", "BottomUp", "NearestFirst");

    public FastBreakSequencer() {
        super("fast-break-sequencer", "Fast Break Sequencer", "Orchestrates optimal block break orders in excavation cuboids to prevent falling sand", Category.AUTOMATION);
        addSettings(sortingAlgorithm);
    }

    public ModeSetting getSortingAlgorithm() {
        return sortingAlgorithm;
    }

    public List<BlockPos> sequenceBreak(List<BlockPos> blocks, double playerX, double playerY, double playerZ) {
        if (blocks == null) return List.of();
        List<BlockPos> sorted = new ArrayList<>(blocks);
        String algo = sortingAlgorithm.getValue();

        if ("TopDown".equalsIgnoreCase(algo)) {
            sorted.sort((a, b) -> Integer.compare(b.y(), a.y()));
        } else if ("BottomUp".equalsIgnoreCase(algo)) {
            sorted.sort((a, b) -> Integer.compare(a.y(), b.y()));
        } else if ("NearestFirst".equalsIgnoreCase(algo)) {
            sorted.sort(Comparator.comparingDouble(p -> {
                double dx = p.x() - playerX;
                double dy = p.y() - playerY;
                double dz = p.z() - playerZ;
                return dx * dx + dy * dy + dz * dz;
            }));
        }
        return sorted;
    }
}
