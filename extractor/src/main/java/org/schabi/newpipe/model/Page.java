package org.schabi.newpipe.model;

import org.schabi.newpipe.extractor.Info;
import org.schabi.newpipe.farm.collector.InfoCollectorOption;

import javax.annotation.Nullable;

public class Page<R> implements InfoCollectorOption<Info> {
    private R reference;
    private Movement movement;

    public Page(@Nullable R reference, Movement movement) {
        this.reference = reference;
        this.movement = movement;
    }

    public Movement getMovement() {
        return movement;
    }

    public boolean hasReference() {
        return reference != null;
    }

    public R getReference() {
        return reference;
    }
}
