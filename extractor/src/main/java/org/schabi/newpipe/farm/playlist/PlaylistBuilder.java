package org.schabi.newpipe.farm.playlist;

import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import org.schabi.newpipe.extractor.playlist.PlaylistInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.farm.CollectorFactory;
import org.schabi.newpipe.farm.stream.InfoContainerBuilder;

import java.io.IOException;

public final class PlaylistBuilder extends InfoContainerBuilder<String, StreamInfoItem, PlaylistExtractor, PlaylistInfo> {
    public PlaylistBuilder(CollectorFactory<StreamInfoItem, InfoItemsCollector<StreamInfoItem, ?>> factory, NewPipe tracker) {
        super(factory, tracker);
    }

    @Override
    protected PlaylistExtractor createExtractor(StreamingService service, String url) throws ExtractionException, IOException {
        return service.getPlaylistExtractor(url);
    }

    public PlaylistBuilder details() {
        collect(new PlaylistDetailsCollector(extractor(), this));
        return this;
    }

    @Override
    protected PlaylistInfo create() throws ParsingException {
        return new PlaylistInfo(
            extractor().getServiceId(),
            extractor().getUIHandler(),
            extractor().getName()
        );
    }
}
