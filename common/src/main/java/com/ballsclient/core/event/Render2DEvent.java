package com.ballsclient.core.event;

/**
 * Dispatched during 2D HUD rendering passes.
 */
public class Render2DEvent extends CancellableEvent {
    private final Object context;
    private final float tickDelta;
    private final int screenWidth;
    private final int screenHeight;

    public Render2DEvent(Object context, float tickDelta) {
        this(context, tickDelta, 0, 0);
    }

    public Render2DEvent(Object context, float tickDelta, int screenWidth, int screenHeight) {
        this.context = context;
        this.tickDelta = tickDelta;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public Object getContext() {
        return context;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
