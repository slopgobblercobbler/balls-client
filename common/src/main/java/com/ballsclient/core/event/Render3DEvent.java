package com.ballsclient.core.event;

/**
 * Dispatched during 3D world rendering passes.
 */
public class Render3DEvent extends CancellableEvent {
    private final Object matrixStack;
    private final float tickDelta;

    public Render3DEvent(Object matrixStack, float tickDelta) {
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
    }

    public Object getMatrixStack() {
        return matrixStack;
    }

    public float getTickDelta() {
        return tickDelta;
    }
}
