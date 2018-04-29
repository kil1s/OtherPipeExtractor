package org.schabi.newpipe.map.entry;

import java.util.Objects;

public abstract class BaseEntry<K, V> extends ProtoEntry<K> {
    public abstract V getValue();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntry<?, ?> that = (BaseEntry<?, ?>) o;
        return Objects.equals(getKey(), that.getKey()) &&
                Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getValue());
    }
}
