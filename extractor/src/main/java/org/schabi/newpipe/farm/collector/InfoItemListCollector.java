package org.schabi.newpipe.farm.collector;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.ListInfo;
import org.schabi.newpipe.farm.ErrorBuilder;

public class InfoItemListCollector<
        T extends ListInfo<I>, // target
        I extends InfoItem, // info (channel etc.)
        L extends ListExtractor<I>,
        O extends InfoCollectorOption<?>
    > extends ExtractorCollector<L, T, T, ListExtractor.InfoItemsPage<I>, O> {

    private L extractor;
    private ErrorBuilder<T> builder;

    public InfoItemListCollector(L extractor, ErrorBuilder<T> builder) {
        super(extractor, builder);
        this.extractor = extractor;
        this.builder = builder;
    }

    final protected L extractor() {
        return extractor;
    }

    final protected ErrorBuilder<T> builder() {
        return builder;
    }

    @Override
    public ListExtractor.InfoItemsPage<I> all(T info) {
        try {
            ListExtractor.InfoItemsPage<I> page = extractor.getInitialPage();
            builder.error(page.getErrors());

            return page;
        } catch (Exception e) {
            builder.error(e);
            return ListExtractor.InfoItemsPage.emptyPage();
        }
    }
}
