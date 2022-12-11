package org.schabi.newpipe.farm.stream;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.ListInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;
import org.schabi.newpipe.farm.CollectorFactory;
import org.schabi.newpipe.farm.ErrorBuilder;
import org.schabi.newpipe.model.Movement;
import org.schabi.newpipe.model.Page;

public final class InfoItemListCollector<
        T extends ListInfo<I>,
        I extends InfoItem,
        L extends ListExtractor<I>,
        P extends Page<String>
 > extends org.schabi.newpipe.farm.collector.InfoItemListCollector<T, I, L, P> {
    private CollectorFactory<I, InfoItemsCollector<I, ?>> factory;
    private L extractor;
    private ErrorBuilder<T> builder;

    public InfoItemListCollector(CollectorFactory<I, InfoItemsCollector<I, ?>> factory, L extractor, ErrorBuilder<T> builder) {
        super(extractor, builder);
        this.factory = factory;
        this.extractor = extractor;
        this.builder = builder;
    }

    private InfoItemsCollector<I, ?> push(
        InfoItemsCollector<I, ?> collector,
        ListExtractor.InfoItemsPage<I> list
    ) {
        for (I item : list.getItems()) {
            collector.addItem(item);
        }
        return collector;
    }

    @Override
    public ListExtractor.InfoItemsPage<I> options(T info, P...pages) throws Exception {
        if (pages.length == 0) {
            return super.options(info, pages);
        } else if (pages.length == 1) {
            P page = pages[0];
            if (page.hasReference()) {
                return extractor.getPage(page.getReference());
            }
            if (page.getMovement() == Movement.PREVIOUS) {
                throw new Exception("the previous url is not supported yet");
            }
            return extractor.getPage(extractor.getNextPageUrl());
        }

        String nextPageUrl = null;
        InfoItemsCollector<I, ?> collector = factory.collector(
            extractor.getServiceId()
        );
        for (P page : pages){
            ListExtractor.InfoItemsPage<I> list;
            if (page.hasReference()) {
                list  = extractor.getPage(page.getReference());
                if (list.hasNextPage()) {
                    nextPageUrl = list.getNextPageUrl();
                }
                collector = push(collector, list);
            }

            if (page.getMovement() == Movement.PREVIOUS) {
                throw new Exception("the previous url is not supported yet");
            }
            if (nextPageUrl == null) {
                continue;
            }

            list = extractor.getPage(nextPageUrl);
            if (list.hasNextPage()) {
                nextPageUrl = list.getNextPageUrl();
            }
            collector = push(collector, list);
        }

        //noinspection unchecked
        return new ListExtractor.InfoItemsPage<I>(
                (org.schabi.newpipe.extractor.InfoItemsCollector<I, ?>) collector,
                nextPageUrl
        );
    }
}
