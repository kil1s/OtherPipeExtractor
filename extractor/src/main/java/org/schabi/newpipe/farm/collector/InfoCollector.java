package org.schabi.newpipe.farm.collector;

import org.schabi.newpipe.extractor.Extractor;
import org.schabi.newpipe.extractor.Info;
import org.schabi.newpipe.farm.ErrorBuilder;

public abstract class InfoCollector<E extends Extractor, I extends Info, O extends InfoCollectorOption<?>> extends ExtractorCollector<E, I, I, I, O> {
    public InfoCollector(E extractor, ErrorBuilder<I> builder) {
        super(extractor, builder);
    }
}
