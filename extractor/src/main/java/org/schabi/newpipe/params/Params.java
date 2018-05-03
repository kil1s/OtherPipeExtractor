package org.schabi.newpipe.params;

import org.schabi.newpipe.map.entry.BaseEntry;
import org.schabi.newpipe.map.entry.EntireEntry;
import org.schabi.newpipe.map.entry.KeyEntry;
import org.schabi.newpipe.map.entry.ProtoEntry;
import org.schabi.newpipe.params.entry.UnitedEntry;
import org.schabi.newpipe.params.model.BaseParams;

import java.util.ArrayList;
import java.util.List;


public class Params<K, O, V> extends BaseParams<K, O, V, List<ProtoEntry>, ProtoEntry> {
    protected List<UnitedEntry<ProtoEntry<O>, BaseEntry<K, V>>> listMap = new ArrayList<UnitedEntry<ProtoEntry<O>, BaseEntry<K, V>>>();

    @Override
    public boolean gotOption(O option) {
        return getOptions().contains(option);
    }

    @Override
    public List<O> getOptions() {
        List<O> options = new ArrayList<O>();
        for (UnitedEntry<ProtoEntry<O>, BaseEntry<K, V>> entry:listMap) {
            if (entry.isE1()) {
                options.add(entry.getE1().getKey());
            }
        }
        return options;
    }

    @Override
    public void remove(O option) {
        for (int i = 0; i < listMap.size(); i++) {
            UnitedEntry<ProtoEntry<O>, BaseEntry<K, V>> entry = listMap.get(i);
            if (entry.isE1() && option.equals(entry.getE1().getKey())) {
                listMap.remove(i);
                break;
            }
        }
    }

    @Override
    public void add(O option) {
        ProtoEntry<O> newEntry = new KeyEntry<O>(option);
        listMap.add(new UnitedEntry<ProtoEntry<O>, BaseEntry<K, V>>(newEntry, null));
    }

    @Override
    public boolean got(K key, V value) {
        for (UnitedEntry<ProtoEntry<O>, BaseEntry<K, V>> entry:listMap) {
            if (entry.isE2()) {
                BaseEntry<K, V> e2 = entry.getE2();
                if (e2.getKey() == key) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void add(K key, V value) {
        listMap.add(new UnitedEntry<ProtoEntry<O>, BaseEntry<K, V>>(null, new EntireEntry<K, V>(key, value)));
    }

    @Override
    public void remove(K key, V value) {
        for (int i = 0; i < listMap.size(); i++) {
            UnitedEntry<ProtoEntry<O>, BaseEntry<K, V>> entry = listMap.get(i);
            if (entry.isE2()) {
                BaseEntry<K, V> e2 = entry.getE2();
                if (e2.getKey() == key) {
                    listMap.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public List<V> get(K key) {
        List<V> entries = new ArrayList<V>();
        for (UnitedEntry<ProtoEntry<O>, BaseEntry<K, V>> entry:listMap) {
            if (entry.isE2()) {
                BaseEntry<K, V> e2 = entry.getE2();
                if (e2.getKey() == key) {
                    entries.add(e2.getValue());
                }
            }
        }
        return entries;
    }

    @Override
    public void clear() {
        listMap = new ArrayList<UnitedEntry<ProtoEntry<O>, BaseEntry<K, V>>>();
    }

    @Override
    public int size() {
        return listMap.size();
    }

    @Override
    public List<ProtoEntry> getEntries() {
        List<ProtoEntry> entries = new ArrayList<ProtoEntry>();
        for (UnitedEntry<ProtoEntry<O>, BaseEntry<K, V>> entry:listMap) {
            if (entry.isE1()) {
                entries.add(entry.getE1());
            }
            if (entry.isE2()) {
                entries.add(entry.getE2());
            }
        }
        return entries;
    }
}
