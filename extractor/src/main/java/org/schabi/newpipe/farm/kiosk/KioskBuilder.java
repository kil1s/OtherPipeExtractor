package org.schabi.newpipe.farm.kiosk;

import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.kiosk.KioskInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.farm.CollectorFactory;
import org.schabi.newpipe.farm.stream.InfoContainerBuilder;

import java.io.IOException;

public final class KioskBuilder extends InfoContainerBuilder<String, StreamInfoItem, KioskExtractor, KioskInfo> {
    private String contentCountry;

    public KioskBuilder(CollectorFactory<StreamInfoItem, InfoItemsCollector<StreamInfoItem, ?>> factory, NewPipe tracker, String contentCountry) {
        super(factory, tracker);
        this.contentCountry = contentCountry;
    }

    @Override
    protected KioskExtractor createExtractor(StreamingService service, String url) throws ExtractionException, IOException {
        KioskExtractor extractor = service.getKioskList().getExtractorByUrl(url, null);
        extractor.setContentCountry(contentCountry);
        return extractor;
    }

    public KioskBuilder country(String contentCountry) {
        this.contentCountry = contentCountry;
        return this;
    }

    @Override
    protected KioskInfo create() throws ParsingException {
        return new KioskInfo(
            extractor().getServiceId(),
            extractor().getUIHandler(),
            extractor().getName()
        );
    }
}
