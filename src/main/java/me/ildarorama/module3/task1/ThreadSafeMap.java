package me.ildarorama.module3.task1;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThreadSafeMap<K, V> implements Map<K,V> {
    private Map<K, V> map = new HashMap<>();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        synchronized (map) {
            return map.put(key, value);
        }
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        synchronized (map) {
            return List.copyOf(map.values());
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}
