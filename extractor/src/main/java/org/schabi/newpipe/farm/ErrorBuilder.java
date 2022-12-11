package org.schabi.newpipe.farm;

import org.schabi.newpipe.extractor.NewPipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ErrorBuilder<I> {
    private List<Throwable> errors;

    public ErrorBuilder(@SuppressWarnings("unused") NewPipe tracker) {
        errors = new ArrayList<>();
    }

    public Collection<Throwable> errors() {
        return errors;
    }

    public void error(Throwable throwable) {
        this.errors.add(throwable);
    }

    public void error(Collection<Throwable> errors) {
        this.errors.addAll(errors);
    }

    abstract public I build() throws Exception;

    public void reset() {
        errors = new ArrayList<>();
    }
}
