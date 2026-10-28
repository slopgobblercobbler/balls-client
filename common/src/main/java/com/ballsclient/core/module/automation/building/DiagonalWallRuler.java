package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * DiagonalWallRuler — Renders 45-degree and 2:1 angle placement grids for fortress and castle walls.
 */
public class DiagonalWallRuler extends Module {
    public record StepOffset(int dx, int dz) {}

    private final ModeSetting diagonalRatio = new ModeSetting("DiagonalRatio", "Wall stepping ratio", "Ratio_1_1", "Ratio_1_1", "Ratio_2_1", "Ratio_3_1");

    public DiagonalWallRuler() {
        super("diagonal-wall-ruler", "Diagonal Wall Ruler", "Renders 45-degree and 2:1 angle placement grids for fortress and castle walls", Category.AUTOMATION);
        addSettings(diagonalRatio);
    }

    public ModeSetting getDiagonalRatio() {
        return diagonalRatio;
    }

    public List<StepOffset> generateSteps(int totalSegments) {
        List<StepOffset> steps = new ArrayList<>(totalSegments);
        String ratio = diagonalRatio.getValue();
        int stepX = "Ratio_3_1".equalsIgnoreCase(ratio) ? 3 : ("Ratio_2_1".equalsIgnoreCase(ratio) ? 2 : 1);
        int stepZ = 1;

        int curX = 0;
        int curZ = 0;
        for (int i = 0; i < totalSegments; i++) {
            curX += stepX;
            curZ += stepZ;
            steps.add(new StepOffset(curX, curZ));
        }
        return steps;
    }
}
