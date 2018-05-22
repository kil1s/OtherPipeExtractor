package org.schabi.newpipe.extractor.services.dtube;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.search.InfoItemsSearchCollector;
import org.schabi.newpipe.extractor.search.SearchEngine;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItemExtractor;

import java.io.IOException;
import java.net.URLEncoder;

public class DTubeSearchEngine extends SearchEngine {
    private StreamingService service;

    public DTubeSearchEngine(StreamingService service) {
        super(service.getServiceId());
        this.service = service;
    }

    @Override
    public InfoItemsSearchCollector search(String query, int page, String contentCountry, Filter filter) throws IOException, ExtractionException {
        InfoItemsSearchCollector collector = getInfoItemSearchCollector();
        String urlQuery = "meta.video.info.title:* AND "+ query+" ";
        String url = DTubeParsingHelper.ASKSTEEM_ENDPOINT+"/search?q="+urlQuery+"&pg="+page+"&include=meta";
        for (StreamInfoItemExtractor extractor:DTubeParsingHelper.getAskSteemStreamExtractorsByUrl(url, service)) {
            collector.commit(extractor);
        }
        return collector;
    }
}
