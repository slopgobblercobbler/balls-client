package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.SchematicVolume;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.function.BiFunction;

/**
 * SchematicVerifier — Audits entire build volume block-by-block against world state and generates error report.
 */
public class SchematicVerifier extends Module {
    public record VerificationReport(int totalBlocks, int correctBlocks, int missingBlocks, int mismatchedBlocks, double completionPercentage) {}

    private final BooleanSetting verifyProperties = new BooleanSetting("VerifyProperties", "Checks block state properties such as facing and half", true);
    private final BooleanSetting exportErrorReport = new BooleanSetting("ExportErrorReport", "Dumps mismatch telemetry to chat or log", false);

    public SchematicVerifier() {
        super("schematic-verifier", "Schematic Verifier", "Audits entire build volume block-by-block against world state and generates error report", Category.AUTOMATION);
        addSettings(verifyProperties, exportErrorReport);
    }

    public BooleanSetting getVerifyProperties() {
        return verifyProperties;
    }

    public BooleanSetting getExportErrorReport() {
        return exportErrorReport;
    }

    public static VerificationReport auditBuild(SchematicVolume volume, BiFunction<Integer, int[], String> worldBlockGetter) {
        if (volume == null) {
            return new VerificationReport(0, 0, 0, 0, 100.0);
        }
        int total = 0;
        int correct = 0;
        int missing = 0;
        int mismatched = 0;

        for (int y = 0; y < volume.getHeight(); y++) {
            for (int z = 0; z < volume.getLength(); z++) {
                for (int x = 0; x < volume.getWidth(); x++) {
                    String req = volume.getBlockState(x, y, z);
                    if (!req.equals("minecraft:air") && !req.equals("air")) {
                        total++;
                        String current = worldBlockGetter != null ? worldBlockGetter.apply(0, new int[]{x, y, z}) : null;
                        if (current == null || current.equals("minecraft:air") || current.equals("air")) {
                            missing++;
                        } else if (current.equalsIgnoreCase(req)) {
                            correct++;
                        } else {
                            mismatched++;
                        }
                    }
                }
            }
        }
        double pct = total > 0 ? (correct * 100.0) / total : 100.0;
        return new VerificationReport(total, correct, missing, mismatched, pct);
    }
}
