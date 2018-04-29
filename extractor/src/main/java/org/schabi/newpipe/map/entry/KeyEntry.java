package org.schabi.newpipe.map.entry;

public class KeyEntry<K> extends ProtoEntry<K> {
    protected K key;

    public KeyEntry(K key) {
        this.key = key;
    }

    @Override
    public K getKey() {
        return key;
    }
}
