package org.schabi.newpipe.extractor.search;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.ListInfo;
import org.schabi.newpipe.extractor.linkhandler.SearchQueryHandler;


public class SearchInfo extends ListInfo<InfoItem> {
    private String searchString;
    private String searchSuggestion;

    public SearchInfo(
            int serviceId,
            SearchQueryHandler qIHandler,
            String searchString
    ) {
        super(serviceId, qIHandler, "Search");
        this.searchString = searchString;
    }

    public void setSearchSuggestion(String searchSuggestion) {
        this.searchSuggestion = searchSuggestion;
    }

    public String getSearchString() {
        return searchString;
    }

    public String getSearchSuggestion() {
        return searchSuggestion;
    }
}
