package org.schabi.newpipe.map.map;

import org.schabi.newpipe.map.entry.EntireEntry;

import java.util.ArrayList;
import java.util.List;

public class ListMap<K, V> extends BaseMap<K, V, List, EntireEntry> {
    protected List<EntireEntry<K, V>> list = new ArrayList<EntireEntry<K, V>>();

    @Override
    public boolean got(K key, V value) {
        for (EntireEntry<K, V> entry:list) {
            if (key.equals(entry.getKey()) && value.equals(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(K key, V value) {
        list.add(new EntireEntry<K, V>(key, value));
    }

    @Override
    public void remove(K key, V value) {
        for (int i = 0; i < list.size(); i++) {
            EntireEntry<K, V> entry = list.get(i);
            if (entry.getKey().equals(key) && entry.getValue().equals(value)) {
                list.remove(i);
            }
        }
    }

    @Override
    public EntireEntry<K, V> get(K key) {
        for (EntireEntry<K, V> value:list) {
            if (key.equals(value.getKey())) {
                return value;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        list = new ArrayList<EntireEntry<K, V>>();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public List<EntireEntry<K, V>> getEntries() {
        return list;
    }
}
