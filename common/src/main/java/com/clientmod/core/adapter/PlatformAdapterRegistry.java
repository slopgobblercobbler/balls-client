package com.clientmod.core.adapter;

/**
 * Registry holding the active Platform Adapter for the running Minecraft version.
 */
public final class PlatformAdapterRegistry {
    private static volatile IPlatformAdapter adapter;

    private PlatformAdapterRegistry() {}

    public static void setAdapter(IPlatformAdapter newAdapter) {
        adapter = newAdapter;
    }

    public static IPlatformAdapter getAdapter() {
        if (adapter == null) {
            throw new IllegalStateException("Platform adapter not initialized!");
        }
        return adapter;
    }

    public static boolean isInitialized() {
        return adapter != null;
    }
}
