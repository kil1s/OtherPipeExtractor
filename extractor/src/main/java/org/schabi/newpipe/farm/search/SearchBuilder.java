package org.schabi.newpipe.farm.search;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.SearchQueryHandler;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.search.SearchInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.farm.CollectorFactory;
import org.schabi.newpipe.farm.stream.InfoContainerBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class SearchBuilder<T> extends InfoContainerBuilder<T, InfoItem, SearchExtractor, SearchInfo> {
    private T query;
    private String contentCountry;
    private List<String> contentFilter = null;
    private String sortFilter = null;
    private String defaultSortFilter;

    public SearchBuilder(CollectorFactory<InfoItem, InfoItemsCollector<InfoItem, ?>> factory, NewPipe tracker, T query, String contentCountry, String defaultSortFilter) throws Exception {
        super(factory, tracker);
        if (!(query instanceof String || query instanceof SearchQueryHandler)) {
            throw new Exception("Only support as query " + SearchQueryHandler.class + " or " + String.class);
        }
        this.query = query;
        this.contentCountry = contentCountry;
        this.defaultSortFilter = defaultSortFilter;
    }

    @Override
    protected SearchExtractor createExtractor(StreamingService service, T query) throws ExtractionException, IOException {
        if (contentFilter == null && sortFilter == null) {
            if (query instanceof String) {
                return service.getSearchExtractor((String) query, contentCountry);
            }
            return service.getSearchExtractor((SearchQueryHandler) query, contentCountry);
        }
        String queryString;
        if (query instanceof SearchQueryHandler) {
            queryString = ((SearchQueryHandler) query).getSearchString();
        } else {
            queryString = (String) query;
        }
        return service.getSearchExtractor(queryString, contentFilter, sortFilter, contentCountry);
    }

    public SearchBuilder<T> country(String contentCountry) {
        this.contentCountry = contentCountry;
        return this;
    }

    private void filter() {
        if (contentFilter != null && sortFilter != null) {
            return;
        }
        contentFilter = new ArrayList<>();
        sortFilter = defaultSortFilter;
    }

    public SearchBuilder<T> content(String filter) {
        filter();
        contentFilter.add(filter);
        return this;
    }

    public SearchBuilder<T> content(Collection<String> filter) {
        filter();
        contentFilter.addAll(filter);
        return this;
    }

    public SearchBuilder<T> sort(String sort) {
        filter();
        sortFilter = sort;
        return this;
    }

    @Override
    public SearchInfo build() throws Exception {
        SearchInfo info = super.build();
        try {
            info.setSearchSuggestion(extractor().getSearchSuggestion());
        } catch (Exception e) {
            error(e);
        }
        return info;
    }

    @Override
    protected SearchInfo create() throws ParsingException {
        return new SearchInfo(
            extractor().getServiceId(),
            extractor().getUIHandler(),
            extractor().getSearchString()
        );
    }

    @Override
    public void reset() {
        super.reset();
        contentFilter = null;
        sortFilter = null;
    }
}
