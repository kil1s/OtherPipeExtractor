package org.schabi.newpipe.map.entry;

import java.util.Objects;

public abstract class ProtoEntry<K> implements MapEntry {
    public abstract K getKey();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntry<?, ?> that = (BaseEntry<?, ?>) o;
        return Objects.equals(getKey(), that.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }
}
