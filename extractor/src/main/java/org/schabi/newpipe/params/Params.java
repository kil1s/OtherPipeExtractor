package org.schabi.newpipe.params;

import org.schabi.newpipe.map.entry.BaseEntry;
import org.schabi.newpipe.map.entry.KeyEntry;
import org.schabi.newpipe.map.entry.ProtoEntry;
import org.schabi.newpipe.params.entry.UnitedEntry;
import org.schabi.newpipe.params.model.BaseParams;

import java.util.ArrayList;
import java.util.List;

/*
public class Params<K, O, V> extends BaseParams<K, O, V, List<ProtoEntry>> {
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
        return false;
    }

    @Override
    public void add(K key, V value) {

    }

    @Override
    public void remove(K key, V value) {

    }

    @Override
    public ProtoEntry get(K key) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public List<ProtoEntry> getEntries() {
        List<ProtoEntry> entries = new ArrayList<ProtoEntry>();
        /*for (:listMap) {

        }*
        return null;
    }
}*/
