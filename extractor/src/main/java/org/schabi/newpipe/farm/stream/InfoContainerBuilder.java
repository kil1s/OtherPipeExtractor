package org.schabi.newpipe.farm.stream;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.ListInfo;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.LinkHandler;
import org.schabi.newpipe.farm.CollectorFactory;
import org.schabi.newpipe.farm.InfoBuilder;
import org.schabi.newpipe.model.Movement;
import org.schabi.newpipe.model.Page;
import org.schabi.newpipe.farm.collector.ExtractorCollector;
import org.schabi.newpipe.farm.collector.InfoCollector;
import org.schabi.newpipe.farm.collector.InfoCollectorOption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class InfoContainerBuilder<ID, I extends InfoItem, E extends ListExtractor<I>, C extends ListInfo<I>> extends InfoBuilder<C> {
    private CollectorFactory<I, InfoItemsCollector<I, ?>> factory;
    private NewPipe tracker;
    private E extractor;
    private List<ExtractorCollector<?, ?, ?, ?, ?>> collectors;
    private List<Page<String>> pages;

    private boolean previous = false;
    private boolean next = false;
    private boolean more = false;

    public InfoContainerBuilder(CollectorFactory<I, InfoItemsCollector<I, ?>> factory, NewPipe tracker) {
        super(tracker);
        this.factory = factory;
        this.tracker = tracker;
        collectors = new ArrayList<>();
        pages = new ArrayList<>();
    }

    protected final E extractor() {
        return extractor;
    }
    
    protected final NewPipe tracker() {
        return tracker;
    }

    protected final void collect(ExtractorCollector<?, ?, ?, ?, ?> collector) {
        collectors.add(collector);
    }

    abstract protected E createExtractor(StreamingService service, ID identifier) throws ExtractionException, IOException;

    abstract protected C create() throws ParsingException;

    protected C collect(C info, InfoCollector<E, C, InfoCollectorOption<?>> collector) {
        return collector.all(info);
    }
    
    @SuppressWarnings("RedundantThrows")
    public InfoContainerBuilder<ID, I, E, C> align(ID identifier)
            throws IOException, ExtractionException
    {
        String url;
        if (identifier instanceof LinkHandler) {
            url = ((LinkHandler) identifier).getUrl();
        } else {
            url = (String) identifier;
        }
        return align(tracker.getServiceByUrl(url), identifier);
    }

    @SuppressWarnings("RedundantThrows")
    public InfoContainerBuilder<ID, I, E, C> align(StreamingService service, ID identifier)
            throws IOException, ExtractionException
    {
        extractor = createExtractor(service, identifier);
        extractor.fetchPage();
        return this;
    }

    public InfoContainerBuilder<ID, I, E, C> next() {
        if (next) {
            return this;
        }
        pages.add(new Page<String>(null, Movement.NEXT));
        next = true;
        return this;
    }

    public InfoContainerBuilder<ID, I, E, C> previous() {
        if (previous) {
            return this;
        }
        pages.add(new Page<String>(null, Movement.PREVIOUS));
        previous = true;
        return this;
    }

    public InfoContainerBuilder<ID, I, E, C> more() {
        if (more) {
            return this;
        }
        pages.add(new Page<String>(null, Movement.MORE));
        more = true;
        return this;
    }

    public InfoContainerBuilder<ID, I, E, C> page(Page<String> page) {
        pages.add(page);
        return this;
    }

    public InfoContainerBuilder<ID, I, E, C> content() {
        //noinspection Convert2Diamond
        collectors.add(new InfoItemListCollector<
            C,
            I,
            E,
            Page<String>
        >(factory, extractor, this));
        return this;
    }

    @SuppressWarnings("DuplicateThrows")
    @Override
    public C build() throws Exception, ExtractionException {
        C info = create();

        for (ExtractorCollector<?, ?, ?, ?, ?> collector : collectors) {
            if (collector instanceof InfoItemListCollector) {
                @SuppressWarnings("unchecked") InfoItemListCollector<
                    C,
                    I,
                    E,
                    Page<String>
                > collect = (InfoItemListCollector<
                    C,
                    I,
                    E,
                    Page<String>
                >) collector;
                ListExtractor.InfoItemsPage<I> items = collect.options(
                        info, (Page<String>[]) pages.toArray(new Page<?>[0])
                );
                info.setRelatedItems(items.getItems());
                if (items.hasNextPage()) {
                    info.setNextPageUrl(items.getNextPageUrl());
                }
                continue;
            }

            info = collect(info, (InfoCollector<E, C, InfoCollectorOption<?>>) collector);
        }

        return info;
    }

    @Override
    public void reset() {
        super.reset();
        collectors = new ArrayList<>();
        pages = new ArrayList<>();
    }
}
