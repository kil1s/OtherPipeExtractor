package org.schabi.newpipe.farm;

import org.schabi.newpipe.extractor.Info;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class InfoBuilder<I extends Info> extends ErrorBuilder<I> {
    public InfoBuilder(NewPipe tracker) {
        super(tracker);
    }
}
