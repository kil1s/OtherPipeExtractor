package org.schabi.newpipe.params.model;

import org.schabi.newpipe.map.entry.MapEntry;
import org.schabi.newpipe.map.map.BaseMap;

import java.util.List;

public abstract class BaseParams<K, O, V, L extends Iterable, E extends MapEntry> extends BaseMap<K, V, L, E> {
    public abstract boolean gotOption(O option);
    public abstract List<O> getOptions();
    public abstract void remove(O option);
    public abstract void add(O option);
}
