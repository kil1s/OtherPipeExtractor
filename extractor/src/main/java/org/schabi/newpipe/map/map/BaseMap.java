package org.schabi.newpipe.map.map;

import org.schabi.newpipe.map.entry.MapEntry;
import org.schabi.newpipe.map.entry.ProtoEntry;

import java.util.Iterator;

/*
 * BaseMap - a map that is less strict then the java ones and can be used complete without any high level classes only Iterable is required
 */
public abstract class BaseMap<K, V, S extends Iterable, E extends MapEntry> implements Iterable {
    @Override
    public Iterator<E> iterator() {
        return getEntries().iterator();
    }

    public abstract boolean got(K key, V value);
    public abstract void add(K key, V value);
    public abstract void remove(K key, V value);
    public abstract E get(K key);

    public abstract void clear();
    public abstract int size();

    public abstract S getEntries();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Iterator entries = iterator();
        Iterator thatEntries = ((BaseMap) o).getEntries().iterator();
        while (thatEntries.hasNext()) {
            if (!entries.hasNext() || !entries.next().equals(thatEntries.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int var1 = 1;
        Iterator entries = iterator();
        while (entries.hasNext()) {
            var1 = 31 * var1 + entries.next().hashCode();
        }
        return var1;
    }
}
