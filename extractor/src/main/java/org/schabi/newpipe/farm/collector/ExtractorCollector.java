package org.schabi.newpipe.farm.collector;

import org.schabi.newpipe.extractor.Extractor;
import org.schabi.newpipe.farm.ErrorBuilder;

public abstract class ExtractorCollector<
        E extends Extractor, T, I, R, O extends InfoCollectorOption<?>> {
    public ExtractorCollector(
        @SuppressWarnings("unused") E extractor,
        @SuppressWarnings("unused") ErrorBuilder<T> builder
    ) {
        // template
    }

    public R options(I info, O... options) throws Exception {
        return all(info);
    }

    abstract public R all(I info);
}
