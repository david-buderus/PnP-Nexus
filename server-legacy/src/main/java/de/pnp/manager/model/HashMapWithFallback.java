package de.pnp.manager.model;

import java.util.HashMap;
import java.util.Map;

public class HashMapWithFallback<K, V> extends HashMap<K, V> {

    /**
     * The value used if a key has no mapping.
     * @see #get(Object)
     */
    protected V fallbackValue;

    public HashMapWithFallback(int initialCapacity, float loadFactor, V fallbackValue) {
        super(initialCapacity, loadFactor);
        this.fallbackValue = fallbackValue;
    }

    public HashMapWithFallback(int initialCapacity, V fallbackValue) {
        super(initialCapacity);
        this.fallbackValue = fallbackValue;
    }

    public HashMapWithFallback(V fallbackValue) {
        this.fallbackValue = fallbackValue;
    }

    public HashMapWithFallback(Map<? extends K, ? extends V> m, V fallbackValue) {
        super(m);
        this.fallbackValue = fallbackValue;
    }

    public V getFallbackValue() {
        return fallbackValue;
    }

    public void setFallbackValue(V fallbackValue) {
        this.fallbackValue = fallbackValue;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@link #fallbackValue} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@link #fallbackValue}.  (There can be at most one such mapping.)
     *
     * <p>A return value of {@link #fallbackValue} does not <i>necessarily</i>
     * indicate that the map contains no mapping for the key; it's also
     * possible that the map explicitly maps the key to {@link #fallbackValue}.
     * The {@link #containsKey containsKey} operation may be used to
     * distinguish these two cases.
     *
     * @see #put(Object, Object)
     */
    @Override
    public V get(Object key) {
        if (containsKey(key)) {
            return super.get(key);
        } else {
            return fallbackValue;
        }
    }
}
