package org.schabi.newpipe.params.entry;

import org.schabi.newpipe.map.entry.MapEntry;

public class UnitedEntry<E1 extends MapEntry, E2 extends MapEntry> implements MapEntry {
    protected E1 e1;
    protected E2 e2;

    public UnitedEntry(E1 e1, E2 e2) {
        if (e1 != null && e2 != null) {
            throw new NullPointerException("e1 and e2 is not null need at least one to be null");
        }
        if (e1 == null && e2 == null) {
            throw new NullPointerException("e1 and e2 is null need at least one to be not null");
        }
        this.e1 = e1;
        this.e2 = e2;
    }

    public boolean isE1() {
        return e1 != null;
    }

    public boolean isE2() {
        return e2 != null;
    }

    public E1 getE1() {
        return e1;
    }

    public E2 getE2() {
        return e2;
    }
}
