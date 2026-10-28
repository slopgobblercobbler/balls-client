package com.ballsclient.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Thread-safe, prioritized event dispatcher with cancellation support and exception isolation.
 */
public class EventBus {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class);

    private static final class HandlerRegistration {
        final Object target;
        final Method method;
        final EventPriority priority;
        final Class<?> eventType;

        HandlerRegistration(Object target, Method method, EventPriority priority, Class<?> eventType) {
            this.target = target;
            this.method = method;
            this.priority = priority;
            this.eventType = eventType;
        }

        void invoke(Event event) {
            try {
                method.invoke(target, event);
            } catch (Throwable t) {
                LOGGER.error("Error dispatching event {} to {}.{}: {}",
                        event.getClass().getSimpleName(),
                        target.getClass().getSimpleName(),
                        method.getName(),
                        t.getMessage(), t);
            }
        }
    }

    private final Map<Class<?>, CopyOnWriteArrayList<HandlerRegistration>> handlersByEvent = new ConcurrentHashMap<>();
    private final Set<Object> registeredListeners = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void register(Object listener) {
        if (listener == null || !registeredListeners.add(listener)) {
            return;
        }

        Class<?> clazz = listener.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isBridge() || method.isSynthetic() || Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe != null) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes.length == 1 && Event.class.isAssignableFrom(paramTypes[0])) {
                        try {
                            method.setAccessible(true);
                        } catch (Exception ignored) {
                        }
                        Class<?> eventType = paramTypes[0];
                        HandlerRegistration reg = new HandlerRegistration(listener, method, subscribe.priority(), eventType);
                        handlersByEvent.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(reg);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    public void unregister(Object listener) {
        if (listener == null || !registeredListeners.remove(listener)) {
            return;
        }
        for (CopyOnWriteArrayList<HandlerRegistration> list : handlersByEvent.values()) {
            list.removeIf(reg -> reg.target == listener);
        }
    }

    public boolean isRegistered(Object listener) {
        return registeredListeners.contains(listener);
    }

    public void post(Event event) {
        if (event == null) {
            return;
        }

        Class<?> eventClass = event.getClass();
        List<HandlerRegistration> matchedHandlers = null;

        for (Map.Entry<Class<?>, CopyOnWriteArrayList<HandlerRegistration>> entry : handlersByEvent.entrySet()) {
            if (entry.getKey().isAssignableFrom(eventClass)) {
                if (matchedHandlers == null) {
                    matchedHandlers = new ArrayList<>(entry.getValue());
                } else {
                    matchedHandlers.addAll(entry.getValue());
                }
            }
        }

        if (matchedHandlers == null || matchedHandlers.isEmpty()) {
            return;
        }

        // Sort by priority slot: HIGHEST (0) -> HIGH (1) -> NORMAL (2) -> LOW (3) -> LOWEST (4) -> MONITOR (5)
        matchedHandlers.sort(Comparator.comparingInt(h -> h.priority.getSlot()));

        boolean isCancellable = event instanceof Cancellable;
        Cancellable cancellable = isCancellable ? (Cancellable) event : null;

        for (HandlerRegistration handler : matchedHandlers) {
            if (isCancellable && cancellable.isCancelled() && handler.priority != EventPriority.MONITOR) {
                continue;
            }
            handler.invoke(event);
        }
    }

    public void clear() {
        registeredListeners.clear();
        handlersByEvent.clear();
    }
}
