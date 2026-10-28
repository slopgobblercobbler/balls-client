package com.ballsclient.core.event;

/**
 * Interface for events that support cancellation.
 */
public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
