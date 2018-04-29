package org.schabi.newpipe.map.entry;

public class EntireEntry<K, V> extends BaseEntry<K, V> {
    protected K key;
    protected V value;

    public EntireEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
