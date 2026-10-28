package com.ballsclient.core.setting;

import com.google.gson.JsonElement;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Strongly typed base setting container.
 *
 * @param <T> Type of encapsulated setting value
 */
public abstract class Setting<T> {
    private final String name;
    private final String description;
    private T value;
    private final T defaultValue;
    private Supplier<Boolean> visibility = () -> true;
    private final CopyOnWriteArrayList<Consumer<T>> changeListeners = new CopyOnWriteArrayList<>();

    public Setting(String name, String description, T defaultValue) {
        this.name = Objects.requireNonNull(name, "Setting name cannot be null");
        this.description = description != null ? description : "";
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getValue() {
        return value;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void setValue(T newValue) {
        if (newValue != null && !newValue.equals(this.value)) {
            this.value = newValue;
            notifyListeners();
        }
    }

    public void reset() {
        setValue(this.defaultValue);
    }

    public boolean isVisible() {
        return visibility.get();
    }

    public Setting<T> visibleWhen(Supplier<Boolean> condition) {
        this.visibility = condition != null ? condition : () -> true;
        return this;
    }

    public Setting<T> onChange(Consumer<T> listener) {
        if (listener != null) {
            changeListeners.add(listener);
        }
        return this;
    }

    protected void notifyListeners() {
        for (Consumer<T> listener : changeListeners) {
            try {
                listener.accept(this.value);
            } catch (Exception e) {
                // Prevent single listener crash from breaking setting mutation
            }
        }
    }

    public abstract JsonElement toJson();

    public abstract void fromJson(JsonElement element);
}
