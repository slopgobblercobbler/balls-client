package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ItemClusterCentroid — Executes spatial clustering on floor items to guide player toward densest drops.
 */
public class ItemClusterCentroid extends Module {
    private final NumberSetting clusterRadius = new NumberSetting("ClusterRadius", "Spatial clustering radius threshold", 3.0, 1.0, 8.0, 0.5);

    public ItemClusterCentroid() {
        super("item-cluster-centroid", "Item Cluster Centroid", "Guides player toward densest item drop clusters", Category.INVENTORY);
        addSettings(clusterRadius);
    }

    public NumberSetting getClusterRadius() {
        return clusterRadius;
    }

    public static double[] computeCentroid(double[][] itemCoordinates, int[] counts) {
        if (itemCoordinates == null || itemCoordinates.length == 0) return new double[]{0.0, 0.0, 0.0};
        double sumX = 0, sumY = 0, sumZ = 0;
        int totalWeight = 0;

        for (int i = 0; i < itemCoordinates.length; i++) {
            int weight = (counts != null && i < counts.length) ? Math.max(1, counts[i]) : 1;
            sumX += itemCoordinates[i][0] * weight;
            sumY += itemCoordinates[i][1] * weight;
            sumZ += itemCoordinates[i][2] * weight;
            totalWeight += weight;
        }

        if (totalWeight <= 0) return new double[]{0.0, 0.0, 0.0};
        return new double[]{sumX / totalWeight, sumY / totalWeight, sumZ / totalWeight};
    }
}
